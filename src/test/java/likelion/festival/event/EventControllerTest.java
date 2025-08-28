package likelion.festival.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import likelion.festival.event.controller.EventController;
import likelion.festival.event.dto.EventResponse;
import likelion.festival.event.service.EventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventService eventService;

    @Test
    @DisplayName("현재 진행중인 이벤트 조회 성공")
    void getCurrentEvents_Success() throws Exception {
        // given
        EventResponse event1 = EventResponse.builder()
                .id(1L)
                .title("타임어택! 칵테일 빨리 마시기")
                .startTime(LocalDateTime.now().minusHours(1))
                .endTime(LocalDateTime.now().plusHours(1))
                .place("주막거리")
                .link("http://instagram.com/event1")
                .build();

        List<EventResponse> events = List.of(event1);
        given(eventService.findCurrentEvents()).willReturn(events);

        // when & then
        mockMvc.perform(get("/api/events")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("현재 진행중인 이벤트 조회 성공"))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].title").value("타임어택! 칵테일 빨리 마시기"))
                .andDo(print());
    }

    @Test
    @DisplayName("현재 진행중인 이벤트가 없을 경우 404 에러 발생")
    void getCurrentEvents_Fail_EventNotFound() throws Exception {
        // given
        given(eventService.findCurrentEvents()).willReturn(Collections.emptyList());

        // when & then
        mockMvc.perform(get("/api/events")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.code").value("E404007"))
                .andExpect(jsonPath("$.message").value("현재 진행중인 이벤트가 없습니다."))
                .andDo(print());
    }
}
