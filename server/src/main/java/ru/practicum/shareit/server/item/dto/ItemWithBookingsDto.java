package ru.practicum.shareit.server.item.dto;

import lombok.Data;
import ru.practicum.shareit.server.booking.dto.BookingDto;
import java.util.List;

@Data
public class ItemWithBookingsDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<CommentDto> comments;
}