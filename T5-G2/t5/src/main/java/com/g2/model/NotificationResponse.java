package com.g2.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NotificationResponse {
    private List<Notification> content;
    private Pageable pageable;
    private int totalPages;
    private long totalElements;
    private boolean last;
    private int numberOfElements;
    private boolean first;
    private boolean empty;
}
