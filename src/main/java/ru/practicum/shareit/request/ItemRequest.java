package ru.practicum.shareit.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemRequest {
    private long requestId;
    private String description;
    private long requesterId;
    private LocalDateTime createdOn;
}
