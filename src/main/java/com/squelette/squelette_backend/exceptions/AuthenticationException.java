package com.squelette.squelette_backend.exceptions;

public class AuthenticationException extends CustomException {

    public AuthenticationException(CodeMessage codeMessage) {
        super(codeMessage, null);
    }

    public AuthenticationException(CodeMessage codeMessage, Throwable cause) {
        super(codeMessage, cause);
    }
}
