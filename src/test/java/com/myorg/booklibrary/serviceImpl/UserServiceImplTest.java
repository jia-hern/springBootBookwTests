package com.myorg.booklibrary.serviceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.myorg.booklibrary.repository.UserRepository;
import com.myorg.booklibrary.service.impl.UserServiceImpl;
import com.myorg.booklibrary.entity.ResetPasswordDTO;
import com.myorg.booklibrary.entity.User;
import com.myorg.booklibrary.entity.UserDTO;
import com.myorg.booklibrary.exception.EntityNotFoundException;
import com.myorg.booklibrary.exception.PasswordMismatchException;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    UserRepository userRepository;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    UserServiceImpl userServiceImpl;

    @DisplayName("Test Find all")
    @Test
    void findAll() {
        List<User> users = Arrays.asList(
                new User("admin", "password"),
                new User("user", "password2"));

        when(userRepository.findAll()).thenReturn(users);

        List<UserDTO> foundUsers = userServiceImpl.getUsers();

        verify(userRepository, times(1)).findAll();
        assertThat(foundUsers).hasSize(2);
        assertEquals("admin", foundUsers.get(0).getUsername());
        assertEquals("user", foundUsers.get(1).getUsername());
    }

    @DisplayName("Test get user by Id")
    @Test
    void getUserById() {
        User user = new User("user", "password");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userServiceImpl.getUser(1L);
        verify(userRepository, times(1)).findById(1L);
        assertThat(foundUser).isNotNull();
        assertEquals(user, foundUser);
    }

    @DisplayName("Test Find By invalid Id")
    @Test
    void findByInvalidId() {

        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, ()->{
            userServiceImpl.getUser(999L);
        });

        verify(userRepository, times(1)).findById(999L);
        assertThat(userRepository.findById(999L)).isEmpty();
    }

    @DisplayName("Test get user by username")
    @Test
    void getUserByUsername() {
        User user = new User("user", "password");

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));

        User foundUser = userServiceImpl.getUser("user");
        verify(userRepository, times(1)).findByUsername("user");
        assertThat(foundUser).isNotNull();
        assertEquals(user, foundUser);
    }

    @DisplayName("Test Find By invalid username")
    @Test
    void findByInvalidUsername() {

        when(userRepository.findByUsername("unknown_user")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, ()->{
            userServiceImpl.getUser("unknown_user");
        });

        verify(userRepository, times(1)).findByUsername("unknown_user");
        assertThat(userRepository.findByUsername("unknown_user")).isEmpty();
    }

    @DisplayName("Test Save")
    @Test
    void save() {
        User user = new User("user", "password");

        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn("asd12345");
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userServiceImpl.saveUser(user);

        verify(userRepository, times(1)).save(any(User.class));

        assertThat(savedUser).isNotNull();
    }

    @DisplayName("Test Update")
    @Test
    void update() {
        User user = new User("user", "password");

        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn("asd12345");
        when(userRepository.save(user)).thenReturn(user);

        user.setUsername("user2");
        User savedUser = userServiceImpl.saveUser(user);

        verify(userRepository, times(1)).save(user);

        assertThat(savedUser).isNotNull();
        assertEquals("user2", savedUser.getUsername());
    }

    @DisplayName("Test Delete By Id")
    @Test
    void deleteById() {
        userServiceImpl.deleteUser(1L);
        verify(userRepository).deleteById(1L);
    }

    @DisplayName("Test reset password")
    @Test
    void resetPassword() {
        User user = new User("user", "password");
        ResetPasswordDTO credentials = new ResetPasswordDTO("user", "password", "newPassword");

        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.matches(credentials.getPassword(), user.getPassword())).thenReturn(Boolean.TRUE);
        when(bCryptPasswordEncoder.encode(credentials.getNewPassword())).thenReturn("newPassword");

        User savedUser = userServiceImpl.resetPassword(credentials);

        verify(userRepository, times(1)).save(user);
        assertThat(savedUser).isNotNull();
        assertEquals("newPassword", savedUser.getPassword());
    }

    @DisplayName("Test reset invalid password")
    @Test
    void resetInvalidPassword() {
        User user = new User("user", "password");
        ResetPasswordDTO credentials = new ResetPasswordDTO("user", "wrongPassword", "newPassword");

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));

        assertThrows(PasswordMismatchException.class, () -> {
            userServiceImpl.resetPassword(credentials);
        });

        verify(userRepository, times(1)).findByUsername("user");
    }

    @DisplayName("Test update role by username")
    @Test
    void updateRoleByUsername() {
        User user = new User("user", "password");

        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));

        User updatedUser = userServiceImpl.updateRole("admin", "user");

        verify(userRepository, times(1)).findByUsername("user");
        assertThat(updatedUser).isNotNull();
        assertEquals("admin", updatedUser.getRole());
    }

    @DisplayName("Test update role")
    @Test
    void updateRole() {
        User user = new User("user", "password");
        Long id = user.getId();

        when(userRepository.save(user)).thenReturn(user);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User updatedUser = userServiceImpl.updateRole("admin", id);

        verify(userRepository, times(1)).findById(id);
        assertThat(updatedUser).isNotNull();
        assertEquals("admin", updatedUser.getRole());
    }
}