package com.ugcs.ucs.client;

public class LicenseLimitationExceededException extends LicenseConstraintException {
    LicenseLimitationExceededException(String msg) {
        super(msg);
    }
}
