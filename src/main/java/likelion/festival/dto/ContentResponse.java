package likelion.festival.dto;

import likelion.festival.domain.Content;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ContentResponseDto {

    private final Long id;
    private final String title;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String period;
    private final String place;
    private final Long noticeId;

    public static ContentResponse from(Content content) {
        return ContentResponse.builder()
                .id(content.getId())
                .title(content.getTitle())
                .startTime(content.getStartTime())
                .endTime(content.getEndTime())
                .period(content.getPeriod())
                .place(content.getPlace())
                .noticeId(content.getNotice() != null ? content.getNotice().getId() : null)
                .build();
    }
}
