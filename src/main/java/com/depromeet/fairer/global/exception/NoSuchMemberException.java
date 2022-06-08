package com.depromeet.fairer.global.exception;

import java.util.function.Supplier;

public class NoSuchMemberException extends RuntimeException {
    public NoSuchMemberException(String message) {
        super(message);
    }
}
