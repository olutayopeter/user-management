package com.fastcredit.usermanagement.utils;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = UserValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StrongPassword {
    String message() default "Password must be strong (contain uppercase, lowercase, and special character).";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
