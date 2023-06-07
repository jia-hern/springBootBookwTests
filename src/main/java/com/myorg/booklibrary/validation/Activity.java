package com.myorg.booklibrary.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ActivityValidator.class)
public @interface Activity {
    String message() default "Activity should be borrow or return";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
