package likelion.festival.controller;

import likelion.festival.dto.EventResponse;
import likelion.festival.service.EventService;
import likelion.festival.exception.ApiException;
import likelion.festival.exception.ApiSuccess;
import likelion.festival.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<ApiSuccess<List<EventResponse>>> getCurrentEvents(){
        List<EventResponse> events = eventService.findCurrentEvents();
        if(events.isEmpty()){
            throw new ApiException(ErrorCode.EVENT_NOT_FOUND);
        }
        return ResponseEntity.ok(ApiSuccess.of(events, "현재 진행중인 이벤트 조회 성공"));
    }

}
