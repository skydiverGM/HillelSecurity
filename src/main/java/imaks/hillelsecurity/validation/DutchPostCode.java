package imaks.hillelsecurity.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {DutchPostCodeValidator.class})
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DutchPostCode {

    public String regex() default "^\\d{4}\\s?[A-Z]{2}$";
    public String message() default "Invalid post code";
    public Class<?>[] groups() default {};
    public Class<? extends Payload>[] payload() default {};
}
