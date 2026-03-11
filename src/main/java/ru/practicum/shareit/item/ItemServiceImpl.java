package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public ItemDto add(Long userId, ItemDto dto) {
        try {
            userService.findById(userId);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required");
        }
        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description is required");
        }
        if (dto.getAvailable() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Available is required");
        }

        Item item = ItemMapper.toItem(dto, userId);
        Item saved = itemRepository.save(item);
        return ItemMapper.toItemDto(saved);
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto dto) {
        try {
            userService.findById(userId);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));

        if (!item.getOwnerId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not the owner");
        }

        if (dto.getName() != null) {
            item.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            item.setDescription(dto.getDescription());
        }
        if (dto.getAvailable() != null) {
            item.setAvailable(dto.getAvailable());
        }

        Item updated = itemRepository.save(item);
        return ItemMapper.toItemDto(updated);
    }

    @Override
    public ItemDto get(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getUserItems(Long userId) {
        try {
            userService.findById(userId);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return itemRepository.findByOwnerId(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        return itemRepository.search(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}