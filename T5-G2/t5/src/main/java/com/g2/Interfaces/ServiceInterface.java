package com.g2.Interfaces;

//Interfaccia che devono implementare tutti i servizi per inserirli nel dispatcher
public interface ServiceInterface {
    Object handleRequest(String action, Object... params);
}