package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private Long nextId = 1L;

    public Optional<User> getUser(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public User addUser(User newUser) {
        checkEmail(newUser.getEmail(), null);
        newUser.setId(nextId++);
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    public User updateUser(Long id, User updatedUser) {
        User existingUser = users.get(id);
        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }

        if (!existingUser.getEmail().equals(updatedUser.getEmail())) {
            checkEmail(updatedUser.getEmail(), id);
        }

        updatedUser.setId(id);
        users.put(id, updatedUser);
        return updatedUser;
    }

    public void deleteUser(Long id) {
        users.remove(id);
    }

    public void checkEmailForServices(String email, Long currentUserId) {
        checkEmail(email, currentUserId);
    }

    private void checkEmail(String email, Long previousId) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                if (previousId == null || !user.getId().equals(previousId)) {
                    throw new RuntimeException("Email already exists");
                }
            }
        }
    }
}