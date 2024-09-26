package com.g2.Components;

public abstract class GenericLogicComponent {

    /**
     * Questo metodo esegue la logica del componente nel modo in cui è stata
     * definita quindi se restituisce True non ci sono stati problemi, nel caso
     * false invece devo segnalare.
     *
     * @return
     */
    public abstract boolean executeLogic();

    /**
     * Con questo metodo segnalo che una logica all'esecuzione ha generato un
     * evento in sostanza così posso segnale un mancato requisito a livello
     * logico come ad esempio autorizzazioni, dati necessari ma che non ci sono
     * oppure che non ci si è loggati.
     *
     * @return
     */
    public abstract String getErrorCode();

}
