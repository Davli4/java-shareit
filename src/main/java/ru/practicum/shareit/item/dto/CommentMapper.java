package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;

@Component
public class CommentMapper {
    public CommentDto toDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setText(comment.getComment().trim());
        dto.setAuthorName(comment.getAuthor().getName());
        dto.setCreated(comment.getCreated());
        return dto;
    }

    public Comment toEntity(String text, User author, Item item, LocalDateTime created) {
        Comment comment = new Comment();
        comment.setComment(text);
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreated(created);
        return comment;
    }
}