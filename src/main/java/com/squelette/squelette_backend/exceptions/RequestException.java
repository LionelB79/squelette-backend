package com.squelette.squelette_backend.exceptions;

import lombok.Getter;

@Getter
public class RequestException extends CustomException {

    public RequestException(CodeMessage codeMessage) {
        super(codeMessage, null);
    }

    public RequestException(CodeMessage codeMessage, Throwable cause) {
        super(codeMessage, cause);
    }
}