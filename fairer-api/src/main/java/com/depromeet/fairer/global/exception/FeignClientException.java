package com.depromeet.fairer.global.exception;

import java.util.Collection;
import java.util.Map;

public class FeignClientException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    private final int status;
    private final String errorMessage;
    private final Map<String, Collection<String>> headers;

    public FeignClientException(int status, String errorMessage, Map<String, Collection<String>> headers) {
        super(errorMessage);
        this.status = status;
        this.errorMessage = errorMessage;
        this.headers = headers;
    }

    /**
     * Http status code
     * @return
     */
    public Integer getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * FeignResponse Headers
     * @return
     */
    public Map<String, Collection<String>> getHeaders() {
        return headers;
    }
}
