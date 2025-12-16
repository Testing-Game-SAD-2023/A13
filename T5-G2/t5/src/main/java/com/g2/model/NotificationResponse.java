package com.g2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationResponse {
    private List<Notification> content;
    private Pageable pageable;
    private int totalPages;
    private long totalElements;
    private boolean last;
    private int size;
    private int number;
    private Sort sort;
    private int numberOfElements;
    private boolean first;
    private boolean empty;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sort {
        private boolean empty;
        private boolean sorted;
        private boolean unsorted;
    }
}
