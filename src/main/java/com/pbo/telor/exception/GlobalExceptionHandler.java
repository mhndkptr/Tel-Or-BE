package com.pbo.telor.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.pbo.telor.dto.common.BaseResponse;
import com.pbo.telor.dto.common.ValidationDetail;
import com.pbo.telor.utils.ResponseUtil;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<BaseResponse<Object>> handleNotFound(NotFoundException ex) {
        return ResponseUtil.error(
                HttpStatus.NOT_FOUND,
                ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BaseResponse<Object>> handleBadRequest(BadRequestException ex) {
        return ResponseUtil.error(
                HttpStatus.BAD_REQUEST,
                ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BaseResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseUtil.error(
                HttpStatus.BAD_REQUEST,
                "TYPE_MISMATCH",
                "Parameter type mismatch: " + ex.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<BaseResponse<Object>> handleUsernameNotFound(UsernameNotFoundException ex) {
        return ResponseUtil.error(HttpStatus.UNAUTHORIZED, "USER_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<BaseResponse<Object>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseUtil.error(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", "Email or password is incorrect");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<BaseResponse<Object>> handleAuthenticationException(AuthenticationException ex) {
        return ResponseUtil.error(HttpStatus.UNAUTHORIZED, "Authentication required");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse<Object>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseUtil.error(HttpStatus.FORBIDDEN, "ACCESS_DENIED",
                "You do not have permission to access this resource");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Object>> handleGeneric(Exception ex) {
        String rootMessage = Optional.ofNullable(ex.getCause())
                .map(Throwable::getMessage)
                .orElse(ex.getMessage());

        String finalMessage = (rootMessage != null && !rootMessage.isBlank())
                ? rootMessage
                : "Unexpected error occurred";

        return ResponseUtil.error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                finalMessage);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {
        List<ValidationDetail> validations = new ArrayList<>();

        Object targetObject = ex.getBindingResult().getTarget();

        for (FieldError err : ex.getBindingResult().getFieldErrors()) {
            String field = err.getField();
            String defaultMessage = err.getDefaultMessage();

            if (defaultMessage != null && defaultMessage.contains("Failed to convert")) {
                try {
                    BeanWrapper wrapper = new BeanWrapperImpl(targetObject);
                    Class<?> fieldType = wrapper.getPropertyType(field);

                    if (fieldType != null && fieldType.isEnum()) {
                        String allowed = Arrays.stream(fieldType.getEnumConstants())
                                .map(Object::toString)
                                .collect(Collectors.joining(", "));

                        validations.add(new ValidationDetail(
                                field,
                                "Invalid value. Allowed value: " + allowed));
                        continue;
                    }
                } catch (Exception ignored) {
                }
            }

            validations.add(new ValidationDetail(field, defaultMessage));
        }

        return ResponseUtil.error(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                "Some fields are invalid",
                validations);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<BaseResponse<Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String rawMessage = ex.getMostSpecificCause().getMessage();
        List<ValidationDetail> validations = new ArrayList<>();

        if (rawMessage != null) {
            if (rawMessage.contains("violates not-null constraint")) {
                Pattern pattern = Pattern.compile("null value in column \"(.*?)\"");
                Matcher matcher = pattern.matcher(rawMessage);

                if (matcher.find()) {
                    String fieldName = matcher.group(1);
                    validations.add(new ValidationDetail(fieldName, fieldName + " must not be null"));
                } else {
                    validations.add(new ValidationDetail(null, "A required field is missing"));
                }
            } else if (rawMessage.contains("duplicate key value")) {
                Pattern pattern = Pattern.compile("\\((.*?)\\)=");
                Matcher matcher = pattern.matcher(rawMessage);

                if (matcher.find()) {
                    String fieldName = matcher.group(1);
                    validations.add(new ValidationDetail(fieldName, fieldName + " already exists"));
                } else {
                    validations.add(new ValidationDetail(null, "Duplicate or invalid data"));
                }
            }

            else {
                validations.add(new ValidationDetail(null, "Data integrity violation: " + rawMessage));
            }
        } else {
            validations.add(new ValidationDetail(null, "Unknown data integrity error"));
        }

        return ResponseUtil.error(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                "Some fields are invalid",
                validations);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<BaseResponse<Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return ResponseUtil.error(
                HttpStatus.METHOD_NOT_ALLOWED,
                "Request method '" + ex.getMethod() + "' not supported for this endpoint");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<BaseResponse<Object>> handleNotFound(NoHandlerFoundException ex) {
        return ResponseUtil.error(
                HttpStatus.NOT_FOUND,
                "Endpoint not found: " + ex.getRequestURL());
    }
}