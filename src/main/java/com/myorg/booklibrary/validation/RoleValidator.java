package com.myorg.booklibrary.validation;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RoleValidator implements ConstraintValidator<Role, String> {
    List<String> roles = Arrays.asList("user", "admin");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null)
            return false;
        for (String role : roles) {
            if (value.toLowerCase().equals(role))
                return true;
        }
        return false;

    }
}
