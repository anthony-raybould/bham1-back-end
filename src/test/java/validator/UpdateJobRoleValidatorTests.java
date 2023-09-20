package validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kainos.ea.cli.JobBandResponse;
import org.kainos.ea.cli.JobCapabilityResponse;
import org.kainos.ea.cli.UpdateJobRoleRequest;
import org.kainos.ea.validator.UpdateJobRoleValidator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateJobRoleValidatorTests {
    private UpdateJobRoleValidator validator;
    private UpdateJobRoleRequest validJobRole;
    @BeforeEach
    public void setUp() {
        validator = new UpdateJobRoleValidator();
        validJobRole = new UpdateJobRoleRequest("ValidName",
                "ValidSummary",
                new JobBandResponse(1, "ValidBand"),
                new JobCapabilityResponse(1, "ValidCapability"),
                "ValidResponsibilities",
                "ValidSharePoint"
        );
    }

    @Test
    public void testValidate_NullObject_ReturnsFalse() {
        assertFalse(validator.validate(null));
    }

    @Test
    public void testValidate_NullJobRoleName_ReturnsFalse() {
        validJobRole.setJobRoleName(null);
        assertFalse(validator.validate(validJobRole));
    }

    @Test
    public void testValidate_LongJobRoleName_ReturnsFalse() {
        validJobRole.setJobRoleName("ThisIsAReallyLongJobRoleNameThatExceedsTheMaximumAllowedLength ThisIsALongSharePointLinkThatExceedsTheMaximumAllowedLength");
        assertFalse(validator.validate(validJobRole));
    }

    @Test
    public void testValidate_NullJobSpecSummary_ReturnsFalse() {
        validJobRole.setJobSpecSummary(null);
        assertFalse(validator.validate(validJobRole));
    }

    @Test
    public void testValidate_BandIDGreaterThanMax_ReturnsFalse() {
        validJobRole.setBand(new JobBandResponse(32768, "InvalidBand"));
        assertFalse(validator.validate(validJobRole));
    }

    @Test
    public void testValidate_CapabilityIDGreaterThanMax_ReturnsFalse() {
        validJobRole.setCapability(new JobCapabilityResponse(32768, "InvalidCapability"));
        assertFalse(validator.validate(validJobRole));
    }

    @Test
    public void testValidate_NullResponsibilities_ReturnsFalse() {
        validJobRole.setResponsibilities(null);
        assertFalse(validator.validate(validJobRole));
    }

    @Test
    public void testValidate_LongSharePoint_ReturnsFalse() {
        validJobRole.setSharePoint("ThisIsALongSharePointLinkThatExceedsTheMaximumAllowedLength ThisIsALongSharePointLinkThatExceedsTheMaximumAllowedLength ThisIsALongSharePointLinkThatExceedsTheMaximumAllowedLength ThisIsALongSharePointLinkThatExceedsTheMaximumAllowedLength ThisIsALongSharePointLinkThatExceedsTheMaximumAllowedLength ThisIsALongSharePointLinkThatExceedsTheMaximumAllowedLength");
        assertFalse(validator.validate(validJobRole));
    }

    @Test
    public void testValidate_ValidObject_ReturnsTrue() {
        assertTrue(validator.validate(validJobRole));
    }
}
