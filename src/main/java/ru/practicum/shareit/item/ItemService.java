package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto add(Long userId, ItemDto dto);

    ItemDto update(Long userId, Long itemId, ItemDto dto);

    ItemDto get(Long itemId);

    List<ItemDto> getUserItems(Long userId);

    List<ItemDto> search(String text);
}