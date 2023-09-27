package org.kainos.ea.validator;

import org.kainos.ea.cli.UpdateJobRoleRequest;

import javax.validation.ValidationException;
import java.util.regex.Pattern;

public class UpdateJobRoleValidator {
    String urlRegex = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";

    public void validate(UpdateJobRoleRequest jobRole)
    {
        if (jobRole == null) {
            throw new ValidationException("Job role is null");
        }
        if (jobRole.getJobRoleName() == null || jobRole.getJobRoleName().length() > 64) {
            throw new ValidationException("Job role name is null or length is greater than 64.");
        }
        if (jobRole.getJobSpecSummary() == null) {
            throw new ValidationException("Job role spec summary is null.");
        }
        if (jobRole.getBand() > 32767) {
            throw new ValidationException("Job role band exceeds size limit.");
        }
        if (jobRole.getCapabilityID() > 32767) {
            throw new ValidationException("Job role capability exceeds size limit.");
        }
        if (jobRole.getResponsibilities() == null) {
            throw new ValidationException("Job role responsibilities is null.");
        }
        if (jobRole.getSharePoint() == null
                || jobRole.getSharePoint().length() > 255
                || !patternMatches(jobRole.getSharePoint(),urlRegex)) {
            throw new ValidationException("Share point url is invalid. Please supply a valid URL.");
        }
    }

    public static boolean patternMatches(String url, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(url)
                .matches();
    }
}
