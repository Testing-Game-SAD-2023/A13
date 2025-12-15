# Modulo Broker
Il modulo NotificationBroker si occupa della configurazione di RabbitMQ, 
definendo gli **exchange**, le **code** e le **routing key** che dovranno 
essere utilizzate dal broker.

## Come funzionano exchange e code?
Gli exchange sono dei gateway per i messaggi del produttore, e si occupano
di instradare ogni messaggio alla coda corretta sulla base della routing key,
ossia una stringa che viene utilizzata come identificativo di una coda (ogni coda
può avere più routing key). La logica di instradamento dipende dal tipo di 
exchange utilizzato. Di base, RabbitMQ fornisce 4 tipi di exchange:
- **Fanout**: fa un broadcast del messaggio, inviandolo quindi ad ogni client
connesso a quell'exchange.
- **Direct**: invia il messaggio alla coda con la routing key specificata.
- **Topic**: invia il messaggio alla coda che soddisfa i criteri specificati
nella routing key. I due criteri che possono essere specificati nella chiave
sono:
  - '*' che può essere sostituita *una* parola qualsiasi.
  **Esempio**: se la chiave è *.orange, allora black.orange e green.orange
  sono entrambi criteri validi, mentre black.green.orange e orange 
  sono invalidi.
  - '#' può essere sostituita con zero o più parole.
    **Esempio**: se la chiave è #.orange, allora black.orange, orange
    e black.green.orange sono tutti validi.

## Come creo un exchange?
Per creare un exchange, devi inserire all'interno del codice un Bean
contenente la classe che rappresenta l'exchange. **Esempio**:
```java
private final String EXCHANGE_NAME = "myExchange";
private final boolean DURABLE = true;
private final boolean AUTO_DESTROY = false;
@Bean
public TopicExchange myExchange() {
    return new TopicExchange(EXCHANGE_NAME, DURABLE, AUTO_DESTROY);
}
```
Ovviamente, per istanziare un exchange diverso (come un fanout), basta
cambiare la classe ritornata.

## Come creo una coda?
La creazione di una coda è molto simile alla creazione di un exchange,
infatti, anche in questo caso bisogna creare un Bean contenente la coda
e un altro Bean per fare il binding della coda con l'exchange e la
routing key. **Esempio**:
```java
private final String QUEUE_NAME = "myQueue";
private final boolean DURABLE = true;
private final String ROUTING_KEY = "myRoutingKey";
@Bean
public Queue myQueue() {
    return new Queue(QUEUE_NAME, DURABLE);
}
@Bean
public Binding binding(Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue)
            .to(exchange)
            .with(ROUTING_KEY);
}
```

## Come invio un messaggio sulla coda?
Per inviare messaggi, è necessario utilizzare il metodo convertAndSend
della classe RabbitTemplate, specificando l'exchange, la routing key e la
classe che si vuole inviare. **Esempio**:
```java
private final RabbitTemplate rabbitTemplate;
public void sendMessage(String exchange, String routingKey) {
    rabbitTemplate.convertAndSend(exchange,routingKey,"Hello World!");
}
```
NOTA: l'invio di un messaggio con RabbitTemplate non prevede nessun meccanismo
di risposta. Inoltre, la consumazione del messaggio su una **coda** è competitivo,
anche se si utilizzano topic. Per questo motivo, bisogna fare attenzione se ad una
singola coda sono connessi più client.

## Come invio un messaggio con risposta?
Per ricevere una risposta dal client è necessario utilizzare un altro template,
cioè l'AsyncRabbitTemplate. Dato che la comunicazione avviene in maniera asincrone,
bisogna gestire la risposta tramite Future, decidendo se aggiungere un callback, o
se bloccare il Thread finché la risposta non arriva (aggiungendo ovviamente un timeout).
**Esempio**:
```java
private final AsyncRabbitTemplate asyncRabbitTemplate;
public void sendMessageWithCallback(String exchange, String routingKey) {
    // Il parametro di CompletableFuture indica il tipo di ritorno
    CompletableFuture<String> future = asyncRabbitTemplate.convertSendAndReceive(exchange,routingKey,"Hello World!");
    // Gestione della risposta con callback
    future.thenAccept(response -> {
        logger.info("Ricevuta risposta: {}", response);
    })
    .exceptionally(ex ->{
        logger.warn("Errore: {}", ex);            
    });
}
public void sendMessageWithoutCallback(String exchange, String routingKey) {
    // Il parametro di CompletableFuture indica il tipo di ritorno
    CompletableFuture<String> future = asyncRabbitTemplate.convertSendAndReceive(exchange,routingKey,"Hello World!");
    // Blocco il thread finché non arriva la risposta (con timeout di 60 secondi)
    String response = future.get(60, TimeUnit.SECONDS);
    logger.info("Ricevuta risposta: {}", response);
}
```

Dal lato del ricevitore, invece, devo definire un listener sulla coda dove
arriverà il messaggio, inviando la risposta con un *return*. **Esempio**:
```java
private final String LISTENER_QUEUE = "myQueue";
@RabbitListener(queues = LISTENER_QUEUE)
public String sendReply(String message) {
    logger.info("Ricevuto messaggio: {}", message);
    return "Hello Back!";
}
```
In questo caso, RabbitMQ capirà automaticamente a quale coda deve inviare la
risposta.