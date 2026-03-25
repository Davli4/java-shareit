package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.getUsers();
    }

    public User findById(Long id) {
        return userRepository.getUser(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));
    }

    public User addUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new RuntimeException("Email is null or empty");
        }
        if (!user.getEmail().contains("@") || !user.getEmail().contains(".")) {
            throw new RuntimeException("Invalid email address format");
        }

        try {
            return userRepository.addUser(user);
        } catch (RuntimeException e) {
            if ("Email already exists".equals(e.getMessage())) {
                throw new RuntimeException("Email already exists");
            }
            throw e;
        }
    }

    public User updateUser(Long id, User user) {
        User updateUser = findById(id);

        if (user.getName() != null) {
            updateUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            if (user.getEmail().isBlank()) {
                throw new RuntimeException("Email is null or empty");
            }
            userRepository.checkEmailForServices(user.getEmail(), id);
            updateUser.setEmail(user.getEmail());
        }
        return userRepository.updateUser(id, updateUser);
    }

    public void deleteUser(Long id) {
        findById(id);
        userRepository.deleteUser(id);
    }
}