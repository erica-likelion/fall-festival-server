package likelion.festival.dto;

import likelion.festival.domain.Performance;

import java.time.format.DateTimeFormatter;

/**
 * 공연 응답 DTO
 *
 * @param id        공연 ID
 * @param artistId  가수 ID
 * @param day       공연 일차
 * @param startTime 시작 시간
 * @param endTime   종료 시간
 */
public record PerformanceResponseDto(
        Long id,
        Long artistId,
        String day,
        String startTime,
        String endTime
) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static PerformanceResponseDto from(Performance performance) {
        return new PerformanceResponseDto(
                performance.getId(),
                performance.getArtist() == null ? null : performance.getArtist().getId(),
                performance.getDay().getValue(),
                performance.getStartTime().format(FORMATTER),
                performance.getEndTime().format(FORMATTER)
        );
    }
}
