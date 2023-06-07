package com.myorg.booklibrary.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.myorg.booklibrary.entity.ResetPasswordDTO;
import com.myorg.booklibrary.entity.User;
import com.myorg.booklibrary.entity.UserDTO;
import com.myorg.booklibrary.exception.EntityNotFoundException;
import com.myorg.booklibrary.exception.PasswordMismatchException;
import com.myorg.booklibrary.repository.UserRepository;
import com.myorg.booklibrary.service.UserService;
import com.myorg.booklibrary.constants.Constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User getUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return unwrapUser(user, userId);
    }

    @Override
    public User getUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return unwrapUser(user);
    }

    @Override
    public List<UserDTO> getUsers() {
        ArrayList<UserDTO> list = new ArrayList<UserDTO>();
        List<User> users = (List<User>) userRepository.findAll();
        users.forEach(user -> {
            list.add(new UserDTO(user.getId(), user.getUsername(), user.getRole()));
        });
        return list;
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole().toLowerCase());
        if (user.getId() == null) {
            user.setRole(Constants.user);
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User updateRole(String role, Long userId) {
        User user = getUser(userId);
        user.setRole(role);
        return userRepository.save(user);
    }

    @Override
    public User updateRole(String role, String username) {
        User user = getUser(username);
        user.setRole(role);
        return userRepository.save(user);
    }

    @Override
    public User resetPassword(ResetPasswordDTO credentials) {
        User user = getUser(credentials.getUsername());
        if (!bCryptPasswordEncoder.matches(credentials.getPassword(), user.getPassword())) {
            throw new PasswordMismatchException(user);
        }
        user.setPassword(bCryptPasswordEncoder.encode(credentials.getNewPassword()));
        return userRepository.save(user);

    }

    static User unwrapUser(Optional<User> entity) {
        if (entity.isPresent())
            return entity.get();
        else
            throw new EntityNotFoundException(User.class);
    }

    static User unwrapUser(Optional<User> entity, Long id) {
        if (entity.isPresent())
            return entity.get();
        else
            throw new EntityNotFoundException(id, User.class);
    }
}
