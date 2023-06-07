package com.myorg.booklibrary.exception;

import com.myorg.booklibrary.entity.User;

public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException(User user) {
        super("The user with username '" + user.getUsername() + "'password does not match");
    }
}
