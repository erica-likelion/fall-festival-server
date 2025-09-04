package likelion.festival.service;

import likelion.festival.dto.ContentResponse;
import likelion.festival.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContentService {

    private final ContentRepository contentRepository;

    public List<ContentResponse> findCurrentContent(){
        LocalDateTime now = LocalDateTime.now();
        return contentRepository.findActiveContents(now).stream()
                .map(ContentResponse::from)
                .collect(Collectors.toList());
    }
}
