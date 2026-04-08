package ru.practicum.shareit.server.request.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.server.item.dto.ItemMapper;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemRequestMapper {

    private final ItemMapper itemMapper;

    public ItemRequestDto toDto(ItemRequest request) {
        if (request == null) return null;
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());
        return dto;
    }

    public ItemRequestDto toDtoWithItems(ItemRequest request, List<Item> items) {
        if (request == null) return null;
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());
        if (items != null) {
            dto.setItems(items.stream()
                    .map(itemMapper::toDto)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public ItemRequest toEntity(CreateItemRequestDto dto, User requester) {
        ItemRequest request = new ItemRequest();
        request.setDescription(dto.getDescription());
        request.setRequester(requester);
        request.setCreated(LocalDateTime.now());
        return request;
    }
}