package com.g2.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pageable {
    private boolean unsorted;
    private boolean sorted;
    private boolean empty;
}
