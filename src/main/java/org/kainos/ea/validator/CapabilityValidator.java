package org.kainos.ea.validator;

import org.kainos.ea.cli.CreateCapabilityRequest;

import javax.validation.ValidationException;

public class CapabilityValidator {

    public void validate(CreateCapabilityRequest capabilityRequest) {

        if (capabilityRequest.getCapabilityName() == null) {
            throw new ValidationException("Capability name is null");
        }
        if (capabilityRequest.getCapabilityName().trim().length() == 0) {
            throw new ValidationException("Capability name is empty");
        }
        if (capabilityRequest.getCapabilityName().length() > 64) {
            throw new ValidationException("Capability name exceeds size limit");
        }
    }
}
