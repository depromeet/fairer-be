package com.depromeet.fairer.global.exception;

import javax.naming.CannotProceedException;

public class CannotCreateException extends RuntimeException {
    public CannotCreateException(String explanation) {
        super(explanation);
    }
}
