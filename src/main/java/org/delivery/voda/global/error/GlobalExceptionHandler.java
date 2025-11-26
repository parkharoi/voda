package org.delivery.voda.global.error;

import lombok.extern.slf4j.Slf4j;
import org.delivery.voda.global.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  protected ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
    log.error("BusinessException: {}", e.getErrorCode().getMessage());
    ErrorCode errorCode = e.getErrorCode();

    return ResponseEntity
        .status(errorCode.getStatus())
        .body(ApiResponse.error(errorCode.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
    log.error("Exception: {}", e.getMessage());
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.error("서버 내부 오류가 발생했습니다."));
  }
}
