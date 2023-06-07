package com.myorg.booklibrary.service;

import java.util.List;

import com.myorg.booklibrary.entity.ResetPasswordDTO;
import com.myorg.booklibrary.entity.User;
import com.myorg.booklibrary.entity.UserDTO;

public interface UserService {
    User getUser(Long userId);

    User getUser(String username);

    List<UserDTO> getUsers();

    User saveUser(User user);

    void deleteUser(Long userId);

    User updateRole(String role, Long userId);

    User updateRole(String role, String username);

    User resetPassword(ResetPasswordDTO credentials);
}
