package com.squelette.squelette_backend.exceptions;

import lombok.Getter;

@Getter
public class InternalException extends CustomException {

    public InternalException(CodeMessage codeMessage) {
        super(codeMessage, null);
    }

    public InternalException(CodeMessage codeMessage, Throwable cause) {
        super(codeMessage, cause);
    }
}