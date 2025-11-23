package com.groom.manvsclass.exception;

import java.util.List;

public class DuplicatedTitlesException extends RuntimeException {

    public DuplicatedTitlesException(String duplicatedMessage) {

        super(duplicatedMessage);
    }
}