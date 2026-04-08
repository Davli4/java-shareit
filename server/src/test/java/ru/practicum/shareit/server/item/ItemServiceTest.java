package ru.practicum.shareit.server.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.server.exception.NotFoundException;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.item.dto.ItemMapper;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void createItemShouldReturnItemDto() {
        Long userId = 1L;
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Drill");
        itemDto.setDescription("Powerful drill");
        itemDto.setAvailable(true);

        User user = new User();
        user.setId(userId);

        Item item = new Item();
        item.setId(1L);
        item.setName("Drill");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemMapper.toEntity(itemDto, user)).thenReturn(item);
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(itemMapper.toDto(item)).thenReturn(itemDto);

        ItemDto result = itemService.create(userId, itemDto);

        assertNotNull(result);
        assertEquals("Drill", result.getName());
    }

    @Test
    void createItemShouldThrowExceptionWhenUserNotFound() {
        Long userId = 1L;
        ItemDto itemDto = new ItemDto();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.create(userId, itemDto));
    }
}