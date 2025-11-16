package com.squelette.squelette_backend.exceptions;

import lombok.Getter;

@Getter
public class NotFoundException extends CustomException {

    public NotFoundException(CodeMessage codeMessage) {
        super(codeMessage, null);
    }

    public NotFoundException(CodeMessage codeMessage, Throwable cause) {
        super(codeMessage, cause);
    }
}
