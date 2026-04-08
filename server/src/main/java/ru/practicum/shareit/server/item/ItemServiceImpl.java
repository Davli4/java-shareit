package ru.practicum.shareit.server.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.server.booking.Booking;
import ru.practicum.shareit.server.booking.BookingRepository;
import ru.practicum.shareit.server.booking.BookingStatus;
import ru.practicum.shareit.server.booking.dto.BookingMapper;
import ru.practicum.shareit.server.exception.NotFoundException;
import ru.practicum.shareit.server.item.dto.CommentMapper;
import ru.practicum.shareit.server.item.dto.ItemMapper;
import ru.practicum.shareit.server.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.server.item.model.Comment;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.item.dto.CommentDto;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public ItemDto create(Long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        Item item = itemMapper.toEntity(itemDto, user);
        return itemMapper.toDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found with id: " + itemId));

        if (!item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("User with id " + userId + " is not the owner of item " + itemId);
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        return itemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public ItemWithBookingsDto getById(Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found with id: " + itemId));

        ItemWithBookingsDto dto = itemMapper.toDtoWithBookings(item);

        if (item.getOwner().getId().equals(userId)) {
            LocalDateTime now = LocalDateTime.now();

            List<Booking> lastBookings = bookingRepository.findLastBookingsByItem(itemId, now);
            if (!lastBookings.isEmpty()) {
                dto.setLastBooking(bookingMapper.toDto(lastBookings.get(0)));
            }

            List<Booking> nextBookings = bookingRepository.findNextBookingsByItem(itemId, now);
            if (!nextBookings.isEmpty()) {
                dto.setNextBooking(bookingMapper.toDto(nextBookings.get(0)));
            }
        }

        List<Comment> comments = commentRepository.findByItemIdOrderByCreatedDesc(itemId);
        dto.setComments(comments.stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList()));

        return dto;
    }

    @Override
    public List<ItemWithBookingsDto> getAllByOwner(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        List<Item> items = itemRepository.findByOwnerId(userId);
        LocalDateTime now = LocalDateTime.now();

        return items.stream()
                .map(item -> {
                    ItemWithBookingsDto dto = itemMapper.toDtoWithBookings(item);

                    List<Booking> lastBookings = bookingRepository.findLastBookingsByItem(item.getId(), now);
                    if (!lastBookings.isEmpty()) {
                        dto.setLastBooking(bookingMapper.toDto(lastBookings.get(0)));
                    }

                    List<Booking> nextBookings = bookingRepository.findNextBookingsByItem(item.getId(), now);
                    if (!nextBookings.isEmpty()) {
                        dto.setNextBooking(bookingMapper.toDto(nextBookings.get(0)));
                    }

                    List<Comment> comments = commentRepository.findByItemIdOrderByCreatedDesc(item.getId());
                    dto.setComments(comments.stream()
                            .map(commentMapper::toDto)
                            .collect(Collectors.toList()));

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        return itemRepository.search(text).stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found with id: " + itemId));

        List<Booking> completedBookings = bookingRepository.findCompletedBookingsByItemAndUser(
                itemId, userId, BookingStatus.APPROVED, LocalDateTime.now());

        if (completedBookings.isEmpty()) {
            throw new IllegalArgumentException("User has not booked this item or booking is not completed");
        }

        Comment comment = commentMapper.toEntity(commentDto.getText(), item, user, LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);

        return commentMapper.toDto(savedComment);
    }
}