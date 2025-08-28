package likelion.festival.exception;

import lombok.Getter;

@Getter
public class ApiSuccess<T> {
    private final String status = "success";
    private final T data;
    private final String message;

    public ApiSuccess(T data) {
        this.data = data;
        this.message = null;
    }

    public ApiSuccess(T data, String message) {
        this.data = data;
        this.message = message;
    }

    public static <T> ApiSuccess<T> of(T data) {
        return new ApiSuccess<>(data);
    }

    public static <T> ApiSuccess<T> of(T data, String message) {
        return new ApiSuccess<>(data, message);
    }
}

