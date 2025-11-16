package com.squelette.squelette_backend.exceptions;

public class ForbiddenException extends CustomException {

    public ForbiddenException(CodeMessage codeMessage) {
        super(codeMessage, null);
    }

    public ForbiddenException(CodeMessage codeMessage, Throwable cause) {
        super(codeMessage, cause);
    }
}
