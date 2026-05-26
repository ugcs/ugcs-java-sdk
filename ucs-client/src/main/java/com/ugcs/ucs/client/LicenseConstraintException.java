package com.ugcs.ucs.client;

public class LicenseConstraintException extends UcsException {
    LicenseConstraintException(String msg) {
        super(msg, ErrorCodes.LICENSE_CONSTRAINT);
    }
}
