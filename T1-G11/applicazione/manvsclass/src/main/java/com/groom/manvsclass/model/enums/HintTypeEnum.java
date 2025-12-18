package com.groom.manvsclass.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum HintTypeEnum {

    GENERIC("generic"),
    CLASS("class");

    private final String value;

    HintTypeEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static HintTypeEnum fromValue(String text) {
        for (HintTypeEnum b : HintTypeEnum.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Tipo di Hint non valido: '" + text + "'. Valori accettati: generic, class.");
    }
}