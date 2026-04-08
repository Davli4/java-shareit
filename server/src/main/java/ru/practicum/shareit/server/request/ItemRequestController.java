package ru.practicum.shareit.server.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.server.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.server.request.dto.ItemRequestDto;
import ru.practicum.shareit.server.util.HttpHeaders;

import java.util.List;

import static ru.practicum.shareit.server.util.HttpHeaders.X_SHARER_USER_ID;

@RestController
@RequestMapping("/server/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService requestService;

    @PostMapping
    public ItemRequestDto create(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                 @RequestBody CreateItemRequestDto requestDto) {
        return requestService.create(userId, requestDto);
    }

    @GetMapping
    public List<ItemRequestDto> getUserRequests(@RequestHeader(X_SHARER_USER_ID) Long userId) {
        return requestService.getUserRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests(@RequestHeader(X_SHARER_USER_ID) Long userId) {
        return requestService.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getById(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                  @PathVariable Long requestId) {
        return requestService.getById(userId, requestId);
    }
}