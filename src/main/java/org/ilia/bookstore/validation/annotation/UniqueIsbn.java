package org.ilia.bookstore.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.ilia.bookstore.validation.implementation.UniqueIsbnImpl;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = UniqueIsbnImpl.class)
@Target(TYPE)
@Retention(RUNTIME)
public @interface UniqueIsbn {

    String message() default "This ISBN number already exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}