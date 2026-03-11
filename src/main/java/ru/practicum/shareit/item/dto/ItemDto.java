package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.Booking;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
