package imaks.hillelsecurity.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DutchPostCodeValidator implements ConstraintValidator<DutchPostCode, String> {

    private String regex;

    @Override
    public void initialize(DutchPostCode dutchCode) {
        regex = dutchCode.regex();
    }

    @Override
    public boolean isValid(String input, ConstraintValidatorContext constraintValidatorContext) {
        return input.matches("^\\d{4}\\s?[A-Z]{2}$");
    }
}
