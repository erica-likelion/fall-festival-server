package likelion.festival.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

        ApiError apiError = ApiError.of(ErrorCode.VALIDATION_FAILED, detail);
        return ResponseEntity.badRequest().body(apiError);
    }

    // 일반 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception ex) {
        log.error("Unexpected error occurred", ex);

        ApiError apiError = ApiError.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return ResponseEntity.internalServerError().body(apiError);
    }
}
