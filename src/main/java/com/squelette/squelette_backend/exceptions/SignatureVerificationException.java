package com.squelette.squelette_backend.exceptions;

public class SignatureVerificationException extends CustomException {

    public SignatureVerificationException(CodeMessage codeMessage) {
        super(codeMessage, null);
    }

    public SignatureVerificationException(CodeMessage codeMessage, Throwable cause) {
        super(codeMessage, cause);
    }
}
