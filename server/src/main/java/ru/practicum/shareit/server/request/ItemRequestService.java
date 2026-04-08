package ru.practicum.shareit.server.request;

import ru.practicum.shareit.server.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.server.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(Long userId, CreateItemRequestDto requestDto);

    List<ItemRequestDto> getUserRequests(Long userId);

    List<ItemRequestDto> getAllRequests(Long userId);

    ItemRequestDto getById(Long userId, Long requestId);
}