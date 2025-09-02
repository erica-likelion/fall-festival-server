package likelion.festival.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 400 Bad Request
    INVALID_ARGUMENT(HttpStatus.BAD_REQUEST, "E400001", "잘못된 요청 파라미터입니다."),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "E400002", "유효성 검증에 실패했습니다."),

    // 404 Not Found
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "E404001", "메뉴를 찾을 수 없습니다."),
    PUB_NOT_FOUND(HttpStatus.NOT_FOUND, "E404002", "주점을 찾을 수 없습니다."),
    ARTIST_NOT_FOUND(HttpStatus.NOT_FOUND, "E404003", "가수를 찾을 수 없습니다."),
    PERFORMANCE_NOT_FOUND(HttpStatus.NOT_FOUND, "E404004", "공연을 찾을 수 없습니다."),
    FORTUNE_NOT_FOUND(HttpStatus.NOT_FOUND, "E404005", "운세를 찾을 수 없습니다."),
    NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND, "E404006", "공지사항을 찾을 수 없습니다."),
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "E404007", "현재 진행중인 이벤트가 없습니다."),
    MARKER_NOT_FOUND(HttpStatus.NOT_FOUND, "E404008", "마커를 찾을 수 없습니다."),

    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E500001", "서버 내부 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
