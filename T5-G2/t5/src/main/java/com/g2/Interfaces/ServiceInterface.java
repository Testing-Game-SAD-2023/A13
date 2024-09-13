package com.g2.Interfaces;

//Interfaccia che devono implementare tutti i servizi per incapsularli in una sola classe che fa da dispatch
public interface ServiceInterface {
    Object handleRequest(String action, Object... params);
}