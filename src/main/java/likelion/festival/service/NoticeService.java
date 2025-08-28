package likelion.festival.service;


import likelion.festival.domain.Notice;
import likelion.festival.dto.NoticeDto;
import likelion.festival.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public List<NoticeDto> getAll() {
        List<Notice> notices = noticeRepository.findAll();
        return notices.stream()
                .map(NoticeDto::from)
                .toList();
    }

}
