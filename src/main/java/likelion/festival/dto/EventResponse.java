package likelion.festival.dto;

import likelion.festival.domain.Event;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EventResponse {

    private final Long id;
    private final String title;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String place;
    private final String period;
    private final Long noticeId;

    public static EventResponse from(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .place(event.getPlace())
                .period(event.getPeriod())
                .noticeId(event.getNotice() != null ? event.getNotice().getId() : null)
                .build();
    }
}
