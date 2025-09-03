package likelion.festival.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ApiException 처리
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handleApiException(ApiException ex) {
        log.warn("ApiException occurred: {}", ex.getMessage());

        ApiError apiError = ex.getDetail() != null
                ? ApiError.of(ex.getErrorCode(), ex.getDetail())
                : ApiError.of(ex.getErrorCode());

        return ResponseEntity
                .status(ex.getErrorCode().getHttpStatus())
                .body(apiError);
    }

    // Validation 예외 처리 (@Valid, @Validated)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.warn("Validation failed: {}", ex.getMessage());

        String detail = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((a, b) -> a + ", " + b)
                .orElse("유효성 검증 실패");

        ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;
        ApiError apiError = ApiError.of(errorCode, detail);

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(apiError);
    }

    // 잘못된 URL 경로 요청 처리
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> handleNoResourceFound(NoResourceFoundException ex) {
        log.warn("Request to non-existent endpoint: {} {}", ex.getHttpMethod(), ex.getResourcePath());

        ErrorCode errorCode = ErrorCode.ENDPOINT_NOT_FOUND;
        ApiError apiError = ApiError.of(errorCode);

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(apiError);
    }

    // 일반 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception ex) {
        log.error("Unexpected error occurred", ex);

        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        ApiError apiError = ApiError.of(errorCode);

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(apiError);
    }
}
