package likelion.festival.exception;

import lombok.Getter;

@Getter
public class ApiError {
    private final String status = "error";
    private final String code;
    private final String message;
    private final String detail;

    public ApiError(String code, String message) {
        this.code = code;
        this.message = message;
        this.detail = null;
    }

    public ApiError(String code, String message, String detail) {
        this.code = code;
        this.message = message;
        this.detail = detail;
    }

    public static ApiError of(ErrorCode errorCode) {
        return new ApiError(errorCode.getCode(), errorCode.getMessage());
    }

    public static ApiError of(ErrorCode errorCode, String detail) {
        return new ApiError(errorCode.getCode(), errorCode.getMessage(), detail);
    }
}

