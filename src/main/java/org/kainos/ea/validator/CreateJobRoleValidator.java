package org.kainos.ea.validator;

import org.kainos.ea.cli.CreateJobRoleRequest;

import java.util.regex.Pattern;

public class CreateJobRoleValidator {

    String urlRegex = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";
    public String isValidJobRole(CreateJobRoleRequest jobRole) {
        if (jobRole.getJobRoleName() == null || jobRole.getJobRoleName().length() > 64) {
            return "Job role name should be less than 64 characters";
        }
        if (jobRole.getJobSpecSummary() == null) {
            return "Job spec summary needs to be added";
        }
        if (jobRole.getBand() > 32767) {
            return "Invalid bandId";
        }
        if (jobRole.getCapability() > 32767) {
            return "Invalid capabilityId";
        }
        if (jobRole.getResponsibilities() == null) {
            return "Job role responsibilities need to be added";
        }

        if (jobRole.getSharePoint() == null
                || jobRole.getSharePoint().length() > 255
                || !patternMatches(jobRole.getSharePoint(),urlRegex)) {
            return "Invalid sharepoint link";
        }
        return null;
    }

    public static boolean patternMatches(String url, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(url)
                .matches();
    }
}
