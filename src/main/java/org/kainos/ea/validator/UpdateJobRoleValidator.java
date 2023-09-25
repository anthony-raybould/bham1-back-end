package org.kainos.ea.validator;

import org.kainos.ea.cli.UpdateJobRoleRequest;

import java.util.regex.Pattern;

public class UpdateJobRoleValidator {
    String urlRegex = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";

    public boolean validate(UpdateJobRoleRequest jobRole)
    {
        if (jobRole == null) {
            return false;
        }
        if (jobRole.getJobRoleName() == null || jobRole.getJobRoleName().length() > 64) {
            return false;
        }
        if (jobRole.getJobSpecSummary() == null) {
            return false;
        }
        if (jobRole.getBand() > 32767) {
            return false;
        }
        if (jobRole.getCapabilityID() > 32767) {
            return false;
        }
        if (jobRole.getResponsibilities() == null) {
            return false;
        }
        if (jobRole.getSharePoint() == null
                || jobRole.getSharePoint().length() > 255
                || !patternMatches(jobRole.getSharePoint(),urlRegex)) {
            return false;
        }
        return true;
    }

    public static boolean patternMatches(String url, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(url)
                .matches();
    }
}
