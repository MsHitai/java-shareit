package ru.practicum.shareit.booking;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Booking {
    private long bookingId;
    private long userId;
    private long itemId;
    private LocalDateTime orderedOn;
    private LocalDateTime returnedOn;
    private Status status;
}
