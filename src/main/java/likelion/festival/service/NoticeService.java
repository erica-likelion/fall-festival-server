package likelion.festival.service;


import likelion.festival.domain.Notice;
import likelion.festival.dto.NoticeDto;
import likelion.festival.exception.ApiException;
import likelion.festival.exception.ErrorCode;
import likelion.festival.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {
    private final NoticeRepository noticeRepository;

    // 공지사항 전체 불러오기
    @Cacheable("notices")
    public List<NoticeDto> getAll() {
        List<Notice> notices = noticeRepository.findAll();

        if (notices.isEmpty()) {
            throw new ApiException(ErrorCode.NOTICE_NOT_FOUND);
        }

        return notices.stream()
                .map(NoticeDto::from)
                .toList();
    }

    // 공지사항 한개 불러오기
    @Cacheable("noticeById")
    public NoticeDto getById(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NOTICE_NOT_FOUND, "id = " + id));

        return NoticeDto.from(notice);
    }

}
