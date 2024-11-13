package com.nuneddine.server.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseEntity> handleCustomException(CustomException e) {
        return ErrorResponseEntity.toResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponseEntity> handleMaxSizeException(MaxUploadSizeExceededException e) {
        CustomException customException = new CustomException(ErrorCode.FILE_SIZE_EXCEEDED);
        return ErrorResponseEntity.toResponseEntity(customException.getErrorCode());
    }
}
