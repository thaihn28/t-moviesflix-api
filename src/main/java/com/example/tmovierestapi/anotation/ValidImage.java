package com.example.tmovierestapi.anotation;

import com.example.tmovierestapi.anotation.validator.ImageFileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ImageFileValidator.class})
public @interface ValidImage {

    String message() default "Invalid image file (Only PNG or JPG/JPEG images are allowed)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}