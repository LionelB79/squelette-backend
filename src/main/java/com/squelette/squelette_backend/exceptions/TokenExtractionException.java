package com.squelette.squelette_backend.exceptions;

public class TokenExtractionException extends CustomException {

    public TokenExtractionException(CodeMessage codeMessage) {
        super(codeMessage, null);
    }

    public TokenExtractionException(CodeMessage codeMessage, Throwable cause) {
        super(codeMessage, cause);
    }
}