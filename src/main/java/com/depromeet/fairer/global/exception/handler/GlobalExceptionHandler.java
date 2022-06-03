package com.depromeet.fairer.global.exception.handler;

import com.depromeet.fairer.global.exception.BadRequestException;
import com.depromeet.fairer.global.exception.CannotJoinTeamException;
import com.depromeet.fairer.global.exception.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     * HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     * 주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("handleMethodArgumentNotValidException", e);
        return exceptionResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    /**
     * ModelAttribute 으로 binding error 발생시 BindException 발생한다.
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponseDto> handleBindException(BindException e, HttpServletRequest request) {
        log.error("handleBindException", e);

        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sb = new StringBuilder();
        if(bindingResult.hasErrors()){
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage()).append("\n");
            }
        }
        return exceptionResponseEntity(sb.toString(), HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    /**
     * enum type 일치하지 않아 binding 못할 경우 발생
     * 주로 @RequestParam enum 으로 binding 못했을 경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponseDto> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        log.error("handleMethodArgumentTypeMismatchException", e);
        return exceptionResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    /**
     * 잘못된 요청이 왔을 때 발생
     */
    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<ErrorResponseDto> handleBadRequestException(BadRequestException e, HttpServletRequest request) {
        log.error("handleBadRequestException", e);
        return exceptionResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponseDto> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        return exceptionResponseEntity(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED, request.getRequestURI());
    }

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponseDto> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        log.error("handleAccessDeniedException", e);
        return exceptionResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN, request.getRequestURI());
    }

    /**
     * 어떤 객체를 생성하지 못할 경우 발생
     */
    @ExceptionHandler(CannotJoinTeamException.class)
    protected ResponseEntity<ErrorResponseDto> handleCannotJoinTeamException(CannotJoinTeamException e, HttpServletRequest request) {
        log.error("CannotJoinTeamException", e);
        return exceptionResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE, request.getRequestURI());
    }

    /**
     * exception 발생사 ResponseEntity 로 변환 후 반환
     * @param message 에러 메세지
     * @param status http 상태
     * @param requestURI 요청한 uri
     * @return ResponseEntity<ErrorResponseDto>
     */
    private ResponseEntity<ErrorResponseDto> exceptionResponseEntity(String message, HttpStatus status, String requestURI) {
        ErrorResponseDto em = ErrorResponseDto.builder()
                .errorMessage(message)
                .code(status.value())
                .referrerUrl(requestURI)
                .build();
        return ResponseEntity.status(status).body(em);
    }
}
