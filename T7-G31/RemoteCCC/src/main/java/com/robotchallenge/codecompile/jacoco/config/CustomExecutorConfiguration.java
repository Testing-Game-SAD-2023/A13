package com.robotchallenge.codecompile.jacoco.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import testrobotchallenge.commons.models.dto.score.JacocoCoverageDTO;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.logging.Logger;

@Configuration
public class CustomExecutorConfiguration {

    private static final int CORE_POOL_SIZE = 1;
    private static final int MAX_POOL_SIZE = 1;
    private static final int MAX_QUEUE_SIZE = 8;
    private static final int EXECUTION_TIME_THRESHOLD = 120_000; // 2 minuti
    private static final int MAX_QUEUE_TIME = 60_000; // 1 minuto

    private static final Logger logger = Logger.getLogger(CustomExecutorConfiguration.class.getName());

    public interface TimedRunnable extends Runnable {
        long getEnqueueTime();
    }

    static class ExecutionTimeMonitor {
        private final Queue<Long> executionTimes = new LinkedList<>();

        public synchronized void addExecutionTime(long time) {
            executionTimes.add(time);
            if (executionTimes.size() > 10) executionTimes.poll();
        }

        public synchronized long getAverageExecutionTime() {
            return (long) executionTimes.stream().mapToLong(Long::longValue).average().orElse(0);
        }
    }

    public static class TimedTask<V> implements Callable<V>, TimedRunnable, Runnable {
        private final long enqueueTime;
        private final Callable<V> task;
        private final ExecutionTimeMonitor monitor;
        private final long maxQueueTime;
        private final long maxExecutionTime;
        private final CompletableFuture<V> future;

        public TimedTask(Callable<V> task, ExecutionTimeMonitor monitor, long maxQueueTime, long maxExecutionTime, CompletableFuture<V> future) {
            this.enqueueTime = System.currentTimeMillis();
            this.task = task;
            this.monitor = monitor;
            this.maxQueueTime = maxQueueTime;
            this.maxExecutionTime = maxExecutionTime;
            this.future = future;
        }

        @Override
        public long getEnqueueTime() {
            return enqueueTime;
        }

        @Override
        public V call() throws Exception {
            if (future.isCancelled() || future.isCompletedExceptionally()) {
                throw new RejectedExecutionException("Task scaduta prima dell'esecuzione");
            }
            V result = runInternal();
            future.complete(result);
            return result;
        }

        @Override
        public void run() {
            try {
                call();
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        }

        public void expire() {
            future.completeExceptionally(new TimeoutException("Task rimossa per timeout in coda"));
        }

        private V runInternal() throws Exception {
            long queueTime = System.currentTimeMillis() - enqueueTime;

            if (queueTime > maxQueueTime) {
                throw new TimeoutException("Tempo massimo di attesa in coda superato");
            }

            long executionTimeout = maxExecutionTime + maxQueueTime - queueTime;
            long start = System.currentTimeMillis();

            ExecutorService timeoutExecutor = Executors.newSingleThreadExecutor();
            Future<V> resultFuture = timeoutExecutor.submit(task);

            try {
                V result = resultFuture.get(executionTimeout, TimeUnit.MILLISECONDS);
                monitor.addExecutionTime(System.currentTimeMillis() - start);
                return result;
            } catch (TimeoutException e) {
                resultFuture.cancel(true);
                throw new TimeoutException("Timeout durante l'esecuzione del task");
            } finally {
                timeoutExecutor.shutdownNow();
            }
        }

        public CompletableFuture<V> getFuture() {
            return future;
        }
    }

    // Lock per sincronizzare l'accesso alla coda
    private static final Object lock = new Object();

    public static class ExpiringTaskCleaner {
        private ExpiringTaskCleaner() {
            throw new IllegalStateException("Cleaner class");
        }

        public static void startCleanerThread(BlockingQueue<Runnable> queue, int maxWaitMillis, int intervalMillis) {
            logger.warning("[ExpiringTaskCleaner] Avvio cleaner");

            Thread cleanerThread = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(intervalMillis);
                        long now = System.currentTimeMillis();

                        synchronized (lock) {
                            Iterator<Runnable> iterator = queue.iterator();
                            StringBuilder sb = new StringBuilder("[ExpiringTaskCleaner] Stato coda:\n");

                            int removed = checkQueue(iterator, now, maxWaitMillis, sb);

                            int total = queue.size();
                            sb.append(String.format("[ExpiringTaskCleaner] Totale: %d | Rimosse: %d | Attive: %d%n",
                                    total, removed, total - removed));

                            logger.warning(sb::toString); // il semplice logger.warning(sb.toString()) restituiva una issue con SonarQube IDE
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }, "expiring-task-cleaner");

            cleanerThread.setDaemon(true);
            cleanerThread.start();
        }

        private static int checkQueue(Iterator<Runnable> iterator, long now, long maxWaitMillis, StringBuilder sb) {
            int removed = 0;

            while (iterator.hasNext()) {
                Runnable r = iterator.next();
                if (r instanceof TimedRunnable timed) {
                    long wait = now - timed.getEnqueueTime();
                    long remaining = maxWaitMillis - wait;

                    boolean isExpired = wait > maxWaitMillis;
                    String status = isExpired ? "RIMOSSA" : "IN_CODA";

                    sb.append(String.format(" - Task: %s | Attesa: %d ms | Tempo residuo: %d ms | Stato: %s%n",
                            r.getClass().getSimpleName(), wait, remaining, status));

                    if (isExpired) {
                        iterator.remove();
                        removed++;
                        if (r instanceof TimedTask<?> tt) {
                            tt.expire();
                            logger.warning("[ExpiringTaskCleaner] Task scaduto rimosso dalla coda");
                        }
                    }
                }
            }

            return removed;
        }
    }


    // Espandiamo la classe ExpiringBlockingQueue
    public static class ExpiringBlockingQueue extends LinkedBlockingQueue<Runnable> {
        private final int maxWaitTimeMillis;

        public ExpiringBlockingQueue(int capacity, int maxWaitTimeMillis) {
            super(capacity);
            this.maxWaitTimeMillis = maxWaitTimeMillis;
        }

        @Override
        public boolean offer(Runnable r) {
            if (r instanceof TimedRunnable timed) {
                long waitTime = System.currentTimeMillis() - timed.getEnqueueTime();
                if (waitTime > maxWaitTimeMillis) {
                    logger.warning(() -> "Task rifiutato in offer(): già scaduto (" + waitTime + " ms)"); // Stampato tramite Supplier per rimuovere l'issue di SonarQube IDE
                    return false;
                }
            }
            return super.offer(r);
        }

        @Override
        public Runnable poll() {
            synchronized (lock) {  // Sincronizzazione sull'accesso alla coda
                Runnable r;
                while ((r = super.poll()) != null) {
                    if (r instanceof TimedRunnable timed) {
                        long waitTime = System.currentTimeMillis() - timed.getEnqueueTime();
                        if (waitTime > maxWaitTimeMillis) {
                            logger.warning(() -> "Task scaduto rimosso in poll() (" + waitTime + " ms)");
                            if (r instanceof TimedTask<?> tt) tt.expire();
                            continue;
                        }
                    }
                    return r;
                }
                return null;
            }
        }
    }

    static class SmartRejectHandler implements RejectedExecutionHandler {
        private final ExecutionTimeMonitor monitor;

        public SmartRejectHandler(ExecutionTimeMonitor monitor) {
            this.monitor = monitor;
        }

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            long avg = monitor.getAverageExecutionTime();
            String reason = "";

            if (executor.getQueue().size() >= MAX_QUEUE_SIZE) {
                reason = "coda piena";
            } else if (avg > EXECUTION_TIME_THRESHOLD) {
                reason = "esecuzioni lente";
            } else {
                reason = "altro motivo";
            }

            String finalReason = reason;
            logger.warning(() -> ("Task rifiutato: " + finalReason));
            throw new RejectedExecutionException("Task rifiutato: " + reason);
        }

    }

    @Bean
    public CustomExecutorService compileExecutor() {
        ExecutionTimeMonitor monitor = new ExecutionTimeMonitor();
        BlockingQueue<Runnable> queue = new ExpiringBlockingQueue(MAX_QUEUE_SIZE, MAX_QUEUE_TIME);

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                60L,
                TimeUnit.SECONDS,
                queue,
                new SmartRejectHandler(monitor)
        );
        executor.allowCoreThreadTimeOut(true);

        ExpiringTaskCleaner.startCleanerThread(queue, MAX_QUEUE_TIME, 10_000);

        return new CustomExecutorService(executor, monitor);
    }

    public static class CustomExecutorService {
        private final ThreadPoolExecutor executor;
        private final ExecutionTimeMonitor monitor;

        public CustomExecutorService(ThreadPoolExecutor executor, ExecutionTimeMonitor monitor) {
            this.executor = executor;
            this.monitor = monitor;
        }

        public Future<JacocoCoverageDTO> submitTask(Callable<JacocoCoverageDTO> userTask) {
            CompletableFuture<JacocoCoverageDTO> future = new CompletableFuture<>();
            TimedTask<JacocoCoverageDTO> timedTask = new TimedTask<>(userTask, monitor, MAX_QUEUE_TIME, EXECUTION_TIME_THRESHOLD, future);

            executor.execute(timedTask);

            return future;
        }
    }
}
