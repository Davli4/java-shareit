package ru.practicum.shareit.server.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.server.booking.dto.BookingDto;
import ru.practicum.shareit.server.booking.dto.BookingMapper;
import ru.practicum.shareit.server.booking.dto.BookingRequestDto;
import ru.practicum.shareit.server.exception.ItemNotAvailableException;
import ru.practicum.shareit.server.exception.NotFoundException;
import ru.practicum.shareit.server.item.ItemRepository;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.user.UserRepository;
import ru.practicum.shareit.server.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void createBookingShouldReturnBookingDto() {
        Long userId = 1L;
        BookingRequestDto requestDto = new BookingRequestDto();
        requestDto.setItemId(1L);
        requestDto.setStart(LocalDateTime.now().plusDays(1));
        requestDto.setEnd(LocalDateTime.now().plusDays(2));

        User booker = new User();
        booker.setId(userId);

        Item item = new Item();
        item.setId(1L);
        item.setAvailable(true);
        User owner = new User();
        owner.setId(2L);
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(1L);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(bookingMapper.toEntity(requestDto, item, booker, BookingStatus.WAITING)).thenReturn(booking);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingMapper.toDto(booking)).thenReturn(bookingDto);

        BookingDto result = bookingService.create(userId, requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void createBookingShouldThrowExceptionWhenItemNotAvailable() {
        Long userId = 1L;
        BookingRequestDto requestDto = new BookingRequestDto();
        requestDto.setItemId(1L);

        User booker = new User();
        booker.setId(userId);

        Item item = new Item();
        item.setId(1L);
        item.setAvailable(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThrows(ItemNotAvailableException.class, () -> bookingService.create(userId, requestDto));
    }

    @Test
    void getBookingByIdShouldReturnBookingDto() {
        Long userId = 1L;
        Long bookingId = 1L;

        User booker = new User();
        booker.setId(userId);

        User owner = new User();
        owner.setId(2L);

        Item item = new Item();
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBooker(booker);
        booking.setItem(item);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(bookingId);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingMapper.toDto(booking)).thenReturn(bookingDto);

        BookingDto result = bookingService.getById(userId, bookingId);

        assertNotNull(result);
        assertEquals(bookingId, result.getId());
    }

    @Test
    void getBookingByIdShouldThrowExceptionWhenUserNotBookerOrOwner() {
        Long userId = 3L;
        Long bookingId = 1L;

        User booker = new User();
        booker.setId(1L);

        User owner = new User();
        owner.setId(2L);

        Item item = new Item();
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setItem(item);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(NotFoundException.class, () -> bookingService.getById(userId, bookingId));
    }
}