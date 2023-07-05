package ru.practicum.shareit.booking;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Booking {
    private long bookingId;
    private long userId;
    private long itemId;
    private LocalDateTime orderedOn;
    private LocalDateTime returnedOn;
    /*status — статус бронирования. Может принимать одно из следующих
значений: WAITING — новое бронирование, ожидает одобрения, APPROVED —
Дополнительные советы ментора 2
бронирование подтверждено владельцем, REJECTED — бронирование
отклонено владельцем, CANCELED — бронирование отменено создателем.*/
}
