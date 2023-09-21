package org.kainos.ea.validator;

import org.kainos.ea.cli.UpdateJobRoleRequest;

public class UpdateJobRoleValidator {
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
        if (jobRole.getBandID() > 32767) {
            return false;
        }
        if (jobRole.getCapabilityID() > 32767) {
            return false;
        }
        if (jobRole.getResponsibilities() == null) {
            return false;
        }
        if (jobRole.getSharePoint() == null || jobRole.getSharePoint().length() > 255) {
            return false;
        }
        return true;
    }
}
