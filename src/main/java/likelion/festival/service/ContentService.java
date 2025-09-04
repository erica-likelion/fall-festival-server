package likelion.festival.service;

import likelion.festival.dto.ContentResponseDto;
import likelion.festival.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContentService {

    private final ContentRepository contentRepository;

    public List<ContentResponseDto> findCurrentContents(){
        LocalDateTime now = LocalDateTime.now();
        return contentRepository.findActiveContents(now).stream()
                .map(ContentResponseDto::from)
                .toList();
    }
}
