package ru.practicum.shareit.server.item.dto;

import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private String text;
    private String authorName;
    private String created;
}