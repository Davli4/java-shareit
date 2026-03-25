package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import java.util.List;

public interface BookingService {
    BookingDto create(Long userId, BookingRequestDto requestDto);

    BookingDto approve(Long userId, Long bookingId, Boolean approved);

    BookingDto getById(Long userId, Long bookingId);

    List<BookingDto> getAllByBooker(Long userId, String state);

    List<BookingDto> getAllByOwner(Long userId, String state);
}