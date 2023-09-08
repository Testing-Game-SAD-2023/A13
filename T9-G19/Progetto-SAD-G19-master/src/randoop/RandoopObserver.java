package randoop;

import interfaces.IObserver;

public class RandoopObserver implements IObserver {
    public void update() {
        // Stampa notifica
        System.out.println("Notifica: Il file batch è stato eseguito con successo.");
    }

}
