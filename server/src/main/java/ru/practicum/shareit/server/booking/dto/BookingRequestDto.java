package ru.practicum.shareit.server.booking.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BookingRequestDto {
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}