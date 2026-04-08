package ru.practicum.shareit.server.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.server.booking.dto.BookingDto;
import ru.practicum.shareit.server.booking.dto.BookingRequestDto;

import java.util.List;

import static ru.practicum.shareit.server.util.HttpHeaders.X_SHARER_USER_ID;

@RestController
@RequestMapping("/server/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@RequestHeader(X_SHARER_USER_ID) Long userId,
                             @RequestBody BookingRequestDto requestDto) {
        return bookingService.create(userId, requestDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@RequestHeader(X_SHARER_USER_ID) Long userId,
                              @PathVariable Long bookingId,
                              @RequestParam Boolean approved) {
        return bookingService.approve(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@RequestHeader(X_SHARER_USER_ID) Long userId,
                              @PathVariable Long bookingId) {
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getUserBookings(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                            @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllByBooker(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                             @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllByOwner(userId, state);
    }
}