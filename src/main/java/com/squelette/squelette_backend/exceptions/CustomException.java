package com.squelette.squelette_backend.exceptions;

public class CustomException extends Exception {

    private final CodeMessage codeMessage;

    public CustomException(CodeMessage codeMessage) {
        super(codeMessage.getMessage(), null);
        this.codeMessage = codeMessage;
    }

    public CustomException(CodeMessage codeMessage, Throwable cause) {
        super(codeMessage.getMessage(), cause);
        this.codeMessage = codeMessage;
    }

    public CodeMessage getCodeMessage() {
        return codeMessage;
    }
}