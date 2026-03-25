package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.util.HttpHeaders;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final ItemRepository itemRepository;
    @PostMapping
    public BookingDto create(@RequestHeader(value = HttpHeaders.X_SHARER_USER_ID, required = false) Long userId,
                             @Valid @RequestBody BookingRequestDto requestDto) {
        if (userId == null) {
            throw new NotFoundException("User not found");
        }

        if (requestDto.getItemId() == null) {
            throw new NotFoundException("Item not found");
        }

        if (!itemRepository.existsById(requestDto.getItemId())) {
            throw new NotFoundException("Item not found");
        }

        return bookingService.create(userId, requestDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@RequestHeader(value = HttpHeaders.X_SHARER_USER_ID) Long userId,
                              @PathVariable Long bookingId,
                              @RequestParam Boolean approved) {
        return bookingService.approve(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@RequestHeader(value = HttpHeaders.X_SHARER_USER_ID) Long userId,
                              @PathVariable Long bookingId) {
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getAllByBooker(@RequestHeader(value = HttpHeaders.X_SHARER_USER_ID) Long userId,
                                           @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllByBooker(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwner(@RequestHeader(value = HttpHeaders.X_SHARER_USER_ID) Long userId,
                                          @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllByOwner(userId, state);
    }
}
