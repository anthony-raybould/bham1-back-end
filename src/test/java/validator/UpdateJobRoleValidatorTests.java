package validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.kainos.ea.cli.UpdateJobRoleRequest;
import org.kainos.ea.validator.UpdateJobRoleValidator;

import javax.validation.ValidationException;
import static org.junit.jupiter.api.Assertions.*;

public class UpdateJobRoleValidatorTests {
    private UpdateJobRoleValidator validator;
    private UpdateJobRoleRequest validJobRole;
    @BeforeEach
    public void setUp() {
        validator = new UpdateJobRoleValidator();
        validJobRole = new UpdateJobRoleRequest("ValidName",
                "ValidSummary",
                1,
                1,
                "ValidResponsibilities",
                "http://www.valid_url.com"
        );
    }

    @Test
    public void testValidate_NullObject_ThrowsException() {
        assertThrows(ValidationException.class, () -> validator.validate(null));
    }

    @Test
    public void testValidate_NullJobRoleName_ReturnsFalse() {
        validJobRole.setJobRoleName(null);
        assertThrows(ValidationException.class, () -> validator.validate(validJobRole));
    }

    @Test
    public void testValidate_LongJobRoleName_ReturnsFalse() {
        validJobRole.setJobRoleName("ThisIsAReallyLongJobRoleNameThatExceedsTheMaximumAllowedLength ThisIsALongSharePointLinkThatExceedsTheMaximumAllowedLength");
        assertThrows(ValidationException.class, () -> validator.validate(validJobRole));
    }

    @Test
    public void testValidate_NullJobSpecSummary_ReturnsFalse() {
        validJobRole.setJobSpecSummary(null);
        assertThrows(ValidationException.class, () -> validator.validate(validJobRole));
    }

    @Test
    public void testValidate_BandIDGreaterThanMax_ReturnsFalse() {
        validJobRole.setBand(32768);
        assertThrows(ValidationException.class, () -> validator.validate(validJobRole));
    }

    @Test
    public void testValidate_CapabilityIDGreaterThanMax_ReturnsFalse() {
        validJobRole.setCapability(32768);
        assertThrows(ValidationException.class, () -> validator.validate(validJobRole));
    }

    @Test
    public void testValidate_NullResponsibilities_ReturnsFalse() {
        validJobRole.setResponsibilities(null);
        assertThrows(ValidationException.class, () -> validator.validate(validJobRole));

    }

    @Test
    public void testValidate_LongSharePoint_ReturnsFalse() {
        validJobRole.setSharePoint("ThisIsALongSharePointLinkThatExceedsTheMaximumAllowedLength ThisIsALongSharePointLinkThatExceedsTheMaximumAllowedLength ThisIsALongSharePointLinkThatExceedsTheMaximumAllowedLength ThisIsALongSharePointLinkThatExceedsTheMaximumAllowedLength ThisIsALongSharePointLinkThatExceedsTheMaximumAllowedLength ThisIsALongSharePointLinkThatExceedsTheMaximumAllowedLength");
        assertThrows(ValidationException.class, () -> validator.validate(validJobRole));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "  ",
            "ftp://www.example.com",
            "http://example",
            "http://www.example@"
    })
    public void testValidate_InvalidUrls_ReturnsFalse(String url)
    {
        validJobRole.setSharePoint(url);
        assertThrows(ValidationException.class, () -> validator.validate(validJobRole));
    }
    @ParameterizedTest
    @ValueSource(strings = {
            "https://www.something.com/",
            "http://www.something.com/",
            "https://www.something.edu.co.in",
            "http://www.url-with-path.com/path",
            "https://www.url-with-querystring.com/?url=has-querystring",
            "http://url-without-www-subdomain.com/",
            "https://mail.google.com"
    })
    public void testValidate_validUrls_ReturnsTrue(String url)
    {
        validJobRole.setSharePoint(url);
        assertDoesNotThrow(() -> validator.validate(validJobRole));
    }
    @Test
    public void testValidate_ValidObject_ReturnsTrue() {
        assertDoesNotThrow(() -> validator.validate(validJobRole));
    }
}
