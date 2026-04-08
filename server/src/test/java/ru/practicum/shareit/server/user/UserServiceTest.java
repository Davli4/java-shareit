package ru.practicum.shareit.server.user;



import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.server.exception.ConflictException;
import ru.practicum.shareit.server.exception.NotFoundException;
import ru.practicum.shareit.server.user.dto.UserDto;
import ru.practicum.shareit.server.user.dto.UserMapper;
import ru.practicum.shareit.server.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUserShouldReturnUserDto() {
        UserDto userDto = new UserDto();
        userDto.setName("Test");
        userDto.setEmail("test@example.com");

        User user = new User();
        user.setId(1L);
        user.setName("Test");
        user.setEmail("test@example.com");

        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);
        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto result = userService.create(userDto);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void createUserShouldThrowExceptionWhenEmailExists() {
        UserDto userDto = new UserDto();
        userDto.setEmail("existing@example.com");

        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(true);

        assertThrows(ConflictException.class, () -> userService.create(userDto));
    }

    @Test
    void getUserByIdShouldReturnUserDto() {
        User user = new User();
        user.setId(1L);
        user.setName("Test");

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Test");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto result = userService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getUserByIdShouldThrowExceptionWhenNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getById(1L));
    }
}