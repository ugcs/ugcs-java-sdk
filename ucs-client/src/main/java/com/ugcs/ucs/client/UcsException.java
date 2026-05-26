package com.ugcs.ucs.client;

import org.jetbrains.annotations.Nullable;

public class UcsException extends Exception {
    private final @Nullable Integer errorCode;


    @Nullable
    public Integer getErrorCode() {
        return errorCode;
    }


    UcsException(String msg, @Nullable Integer errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }


    public static class ErrorCodes {
        public static int LICENSE_CONSTRAINT = 1001;
    }
}