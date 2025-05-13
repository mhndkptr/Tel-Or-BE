package com.pbo.telor.utils;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pbo.telor.dto.common.BaseResponse;
import com.pbo.telor.dto.common.ErrorResponse;
import com.pbo.telor.dto.common.PaginationResponse;
import com.pbo.telor.dto.common.ValidationDetail;

public class ResponseUtil {
    public static <T> ResponseEntity<BaseResponse<T>> ok(T data, String message) {
        return ResponseEntity.ok(
            BaseResponse.<T>builder()
                .status(HttpStatus.OK.value())
                .code(HttpStatus.OK.name())
                .message(message)
                .data(data)
                .build()
        );
    }

    public static <T> ResponseEntity<BaseResponse<T>> paged(T data, PaginationResponse pagination, String message) {
        return ResponseEntity.ok(
            BaseResponse.<T>builder()
                .status(HttpStatus.OK.value())
                .code(HttpStatus.OK.name())
                .message(message)
                .data(data)
                .pagination(pagination)
                .build()
        );
    }

    public static <T> ResponseEntity<BaseResponse<T>> error(HttpStatus status, String errorName, String message, List<ValidationDetail> validations) {
        return ResponseEntity.status(status)
            .body(BaseResponse.<T>builder()
                .status(status.value())
                .code(status.name())
                .message(message)
                .error(new ErrorResponse(errorName, message, validations))
                .build()
        );
    }

   public static <T> ResponseEntity<BaseResponse<T>> error(HttpStatus status, String errorName, String message) {
        return ResponseEntity.status(status)
            .body(BaseResponse.<T>builder()
                .status(status.value())
                .code(status.name())
                .message("Unexpected error occurred") 
                .error(new ErrorResponse(errorName, message, null))
                .build());
    }
}