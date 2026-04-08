package ru.practicum.shareit.server.item.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.server.item.model.Comment;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.user.model.User;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    public CommentDto toDto(Comment comment) {
        if (comment == null) return null;
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setText(comment.getComment());
        dto.setAuthorName(comment.getAuthor().getName());
        dto.setCreated(comment.getCreated().toString());
        return dto;
    }

    public Comment toEntity(String text, Item item, User author, LocalDateTime created) {
        Comment comment = new Comment();
        comment.setComment(text);
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(created);
        return comment;
    }
}