package ru.practicum.shareit.server.booking;

import ru.practicum.shareit.server.booking.dto.BookingDto;
import ru.practicum.shareit.server.booking.dto.BookingRequestDto;

import java.util.List;

public interface BookingService {
    BookingDto create(Long userId, BookingRequestDto requestDto);

    BookingDto approve(Long userId, Long bookingId, Boolean approved);

    BookingDto getById(Long userId, Long bookingId);

    List<BookingDto> getAllByBooker(Long userId, String state);

    List<BookingDto> getAllByOwner(Long userId, String state);
}