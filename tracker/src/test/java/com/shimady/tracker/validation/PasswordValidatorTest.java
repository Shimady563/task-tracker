package com.shimady.tracker.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class PasswordValidatorTest {

    private final PasswordValidator validator = new PasswordValidator();

    @Mock
    private ConstraintValidatorContext context;

    @Test
    public void shouldValidateValidPassword() {
        assertTrue(validator.isValid("StrongPass1$", context));
    }

    @Test
    public void shouldInvalidateShortPassword() {
        assertFalse(validator.isValid("A1@abc", context));
    }

    @Test
    public void shouldInvalidatePasswordWithoutLowercaseCharacter() {
        assertFalse(validator.isValid("ABCDEFG1@", context));
    }

    @Test
    public void shouldInvalidatePasswordWithoutUppercaseCharacter() {
        assertFalse(validator.isValid("abcdefg1@", context));
    }

    @Test
    public void shouldInvalidatePasswordWithoutDigit() {
        assertFalse(validator.isValid("Abcdefgh@", context));
    }

    @Test
    public void shouldInvalidatePasswordWithoutSpecialCharacter() {
        assertFalse(validator.isValid("Abcdefgh1", context));
    }

    @Test
    public void shouldInvalidatePasswordWithDisallowedCharacter() {
        assertFalse(validator.isValid("Abcdef1@~", context));
    }

    @Test
    public void shouldInvalidatePasswordWithNonAlphanumericCharacters() {
        assertFalse(validator.isValid("Abcdef1@\n", context));
    }

    @Test
    public void shouldInvalidateNullPassword() {
        assertFalse(validator.isValid(null, context));
    }

    @Test
    public void shouldInvalidatePasswordWithSpace() {
        assertFalse(validator.isValid("Abc def1@", context));
    }

    @Test
    public void shouldInvalidateEmptyPassword() {
        assertFalse(validator.isValid("", context));
    }
}

