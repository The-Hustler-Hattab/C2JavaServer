package com.mtattab.c2cServer.annotations;

import com.mtattab.c2cServer.validator.SessionExistsValidators;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = SessionExistsValidators.class)
@Target({ PARAMETER })
@Retention(RUNTIME)
public  @interface SessionExistsValidator {

    String message() default "ReverseShell session not found ";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};


}
