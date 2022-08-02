package com.depromeet.fairer.global.exception;


public class FairerException extends RuntimeException {
    public FairerException(Exception e) {
        super(e);
    }

    public FairerException() {
        super();
    }

    public FairerException(String message) {
        super(message);
    }
}
