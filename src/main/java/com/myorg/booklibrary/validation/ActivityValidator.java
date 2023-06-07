package com.myorg.booklibrary.validation;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ActivityValidator implements ConstraintValidator<Activity, String> {
    List<String> activities = Arrays.asList("borrow", "return");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null)
            return false;
        for (String activity : activities) {
            if (value.toLowerCase().equals(activity))
                return true;
        }
        return false;

    }
}
