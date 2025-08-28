package likelion.festival.service;


import likelion.festival.domain.Notice;
import likelion.festival.dto.NoticeDto;
import likelion.festival.exception.ApiException;
import likelion.festival.exception.ErrorCode;
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

        if (notices.isEmpty()) {
            throw new ApiException(ErrorCode.NOTICE_NOT_FOUND);
        }

        return notices.stream()
                .map(NoticeDto::from)
                .toList();
    }

}
