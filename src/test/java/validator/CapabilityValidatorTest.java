package validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kainos.ea.cli.CreateCapabilityRequest;
import org.kainos.ea.validator.CapabilityValidator;

import javax.validation.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

public class CapabilityValidatorTest {

    private CapabilityValidator capabilityValidator;

    @BeforeEach
    public void setUp() {
        capabilityValidator = new CapabilityValidator();
    }

    @Test
    public void validate_validCapabilityName_shouldNotThrowException() {
        CreateCapabilityRequest request = new CreateCapabilityRequest("Engineering");
        assertDoesNotThrow(() -> capabilityValidator.validate(request));
    }

    @Test
    public void validate_nullCapabilityName_shouldThrowValidationException() {
        CreateCapabilityRequest request = new CreateCapabilityRequest(null);
        assertThrows(ValidationException.class, () -> capabilityValidator.validate(request));
    }

    @Test
    public void validate_emptyCapabilityName_shouldThrowValidationException() {
        CreateCapabilityRequest request = new CreateCapabilityRequest("");
        assertThrows(ValidationException.class, () -> capabilityValidator.validate(request));
    }

    @Test
    public void validate_tooLongCapabilityName_shouldThrowValidationException() {
        CreateCapabilityRequest request = new CreateCapabilityRequest(
                "TooLongTooLongTooLongTooLongTooLong" +
                        "TooLongTooLongTooLongTooLongTooLong");
        assertThrows(ValidationException.class, () -> capabilityValidator.validate(request));
    }
}
