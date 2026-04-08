package ru.practicum.shareit.server.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.user.model.User;

@Component
public class ItemMapper {

    public ItemDto toDto(Item item) {
        if (item == null) return null;
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setRequestId(item.getRequestId());
        return dto;
    }

    public ItemWithBookingsDto toDtoWithBookings(Item item) {
        if (item == null) return null;
        ItemWithBookingsDto dto = new ItemWithBookingsDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        return dto;
    }

    public Item toEntity(ItemDto dto, User owner) {
        if (dto == null) return null;
        Item item = new Item();
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        item.setOwner(owner);
        if (dto.getRequestId() != null) {
            item.setRequestId(dto.getRequestId());
        }
        return item;
    }
}