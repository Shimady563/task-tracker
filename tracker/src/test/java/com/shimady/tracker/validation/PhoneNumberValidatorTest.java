package com.shimady.tracker.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class PhoneNumberValidatorTest {

    private final PhoneNumberValidator validator = new PhoneNumberValidator();

    @Mock
    private ConstraintValidatorContext context;

    @Test
    public void shouldValidateValidPhoneNumberWithPlus7() {
        assertTrue(validator.isValid("+71234567890", context));
    }

    @Test
    public void shouldValidateValidPhoneNumberWith8() {
        assertTrue(validator.isValid("81234567890", context));
    }

    @Test
    public void shouldInvalidateShortPhoneNumberShort() {
        assertFalse(validator.isValid("8123456789", context));
    }

    @Test
    public void shouldInvalidateLongPhoneNumber() {
        assertFalse(validator.isValid("+712345678901", context));
    }

    @Test
    public void shouldInvalidatePhoneNumberWithPrefixWithoutPlus() {
        assertFalse(validator.isValid("71234567890", context));
    }

    @Test
    public void shouldInvalidatePhoneNumberWithWrongDigitPrefix() {
        assertFalse(validator.isValid("91234567890", context));
    }

    @Test
    public void shouldInvalidatePhoneNumberWithNoPrefix() {
        assertFalse(validator.isValid("1234567890", context));
    }

    @Test
    public void shouldInvalidateNullPhoneNumber() {
        assertFalse(validator.isValid(null, context));
    }

    @Test
    public void shouldInvalidateEmptyPhoneNumber() {
        assertFalse(validator.isValid("", context));
    }

    @Test
    public void shouldInvalidatePhoneNumberWithDisallowedCharacters() {
        assertFalse(validator.isValid("+7(123)456-78-90", context));
    }
}
