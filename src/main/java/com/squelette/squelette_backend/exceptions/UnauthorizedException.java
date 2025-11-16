package com.squelette.squelette_backend.exceptions;

public class UnauthorizedException extends CustomException {

    public UnauthorizedException(CodeMessage codeMessage) {
        super(codeMessage, null);
    }

    public UnauthorizedException(CodeMessage codeMessage, Throwable cause) {
        super(codeMessage, cause);
    }
}