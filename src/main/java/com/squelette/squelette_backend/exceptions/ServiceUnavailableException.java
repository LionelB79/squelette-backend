package com.squelette.squelette_backend.exceptions;

import lombok.Getter;

@Getter
public class ServiceUnavailableException extends CustomException {

    public ServiceUnavailableException(CodeMessage codeMessage) {
        super(codeMessage, null);
    }

    public ServiceUnavailableException(CodeMessage codeMessage, Throwable cause) {
        super(codeMessage, cause);
    }
}