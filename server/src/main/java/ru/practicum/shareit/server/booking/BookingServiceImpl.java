package ru.practicum.shareit.server.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.server.booking.dto.BookingDto;
import ru.practicum.shareit.server.booking.dto.BookingMapper;
import ru.practicum.shareit.server.booking.dto.BookingRequestDto;
import ru.practicum.shareit.server.exception.ForbiddenException;
import ru.practicum.shareit.server.exception.ItemNotAvailableException;
import ru.practicum.shareit.server.exception.NotFoundException;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.item.ItemRepository;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingDto create(Long userId, BookingRequestDto requestDto) {

        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        Item item = itemRepository.findById(requestDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Item not found with id: " + requestDto.getItemId()));

        if (!item.getAvailable()) {
            throw new ItemNotAvailableException("Item is not available");
        }

        if (item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Owner cannot book own item");
        }

        if (requestDto.getStart().isAfter(requestDto.getEnd()) ||
                requestDto.getStart().equals(requestDto.getEnd())) {
            throw new NotFoundException("Invalid booking dates");
        }

        if (requestDto.getStart().isBefore(LocalDateTime.now())) {
            throw new NotFoundException("Start date cannot be in the past");
        }

        Booking booking = bookingMapper.toEntity(requestDto, item, booker, BookingStatus.WAITING);
        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDto approve(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found with id: " + bookingId));

        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new ForbiddenException("User is not the owner of this item");
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new NotFoundException("Booking status is already " + booking.getStatus());
        }

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found with id: " + bookingId));

        if (!booking.getBooker().getId().equals(userId) &&
                !booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("User is not booker or owner");
        }

        return bookingMapper.toDto(booking);
    }

    @Override
    public List<BookingDto> getAllByBooker(Long userId, String state) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with id: " + userId);
        }

        List<Booking> bookings;
        LocalDateTime localDateTime = LocalDateTime.now();

        switch (state.toUpperCase()) {
            case "ALL":
                bookings = bookingRepository.findByBookerIdOrderByStartDateDesc(userId);
                break;
            case "CURRENT":
                bookings = bookingRepository.findCurrentByBooker(userId, localDateTime);
                break;
            case "PAST":
                bookings = bookingRepository.findPastByBooker(userId, localDateTime);
                break;
            case "FUTURE":
                bookings = bookingRepository.findFutureByBooker(userId, localDateTime);
                break;
            case "WAITING":
                bookings = bookingRepository.findByBookerIdAndStatusOrderByStartDateDesc(userId, BookingStatus.WAITING);
                break;
            case "REJECTED":
                bookings = bookingRepository.findByBookerIdAndStatusOrderByStartDateDesc(userId, BookingStatus.REJECTED);
                break;
            default:
                throw new NotFoundException("Unknown state: " + state);
        }

        return bookings.stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getAllByOwner(Long userId, String state) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with id: " + userId);
        }

        List<Booking> bookings;
        LocalDateTime now = LocalDateTime.now();

        switch (state.toUpperCase()) {
            case "ALL":
                bookings = bookingRepository.findByOwnerId(userId);
                break;
            case "CURRENT":
                bookings = bookingRepository.findCurrentByOwner(userId, now);
                break;
            case "PAST":
                bookings = bookingRepository.findPastByOwner(userId, now);
                break;
            case "FUTURE":
                bookings = bookingRepository.findFutureByOwner(userId, now);
                break;
            case "WAITING":
                bookings = bookingRepository.findByItemOwnerIdAndStatusOrderByStartDateDesc(userId, BookingStatus.WAITING);
                break;
            case "REJECTED":
                bookings = bookingRepository.findByItemOwnerIdAndStatusOrderByStartDateDesc(userId, BookingStatus.REJECTED);
                break;
            default:
                throw new NotFoundException("Unknown state: " + state);
        }

        return bookings.stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }
}