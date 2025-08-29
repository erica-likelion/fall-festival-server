package likelion.festival.service;

import likelion.festival.dto.EventResponse;
import likelion.festival.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;

    public List<EventResponse> findCurrentEvents(){
        LocalDateTime now = LocalDateTime.now();
        return eventRepository.findActiveEvents(now).stream()
                .map(EventResponse::from)
                .collect(Collectors.toList());
    }
}
