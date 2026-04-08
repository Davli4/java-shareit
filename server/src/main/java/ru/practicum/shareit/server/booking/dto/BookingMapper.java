package ru.practicum.shareit.server.booking.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.server.booking.Booking;
import ru.practicum.shareit.server.booking.BookingStatus;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.user.model.User;

@Component
public class BookingMapper {

    public BookingDto toDto(Booking booking) {
        if (booking == null) return null;
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setStart(booking.getStartDate());
        dto.setEnd(booking.getEndDate());
        dto.setItem(booking.getItem());
        dto.setBooker(booking.getBooker());
        dto.setStatus(booking.getStatus());
        return dto;
    }

    public Booking toEntity(BookingRequestDto dto, Item item, User booker, BookingStatus status) {
        Booking booking = new Booking();
        booking.setStartDate(dto.getStart());
        booking.setEndDate(dto.getEnd());
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(status);
        return booking;
    }
}