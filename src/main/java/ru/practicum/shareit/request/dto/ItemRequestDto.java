package ru.practicum.shareit.request.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemRequestDto {
    private long requestId;
    private String description;
    private long requesterId;
    private LocalDateTime createdOn;
}
