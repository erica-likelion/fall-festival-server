package likelion.festival.controller;

import likelion.festival.service.NoticeService;
import likelion.festival.dto.NoticeDto;
import likelion.festival.exception.ApiSuccess;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notices")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping
    public ApiSuccess<List<NoticeDto>> getNotices() {
        return new ApiSuccess<>(noticeService.getAll(), "공연 목록 조회 완료");
    }

    @GetMapping("/{id}")
    public ApiSuccess<NoticeDto> getNotice(@PathVariable Long id) {
        NoticeDto noticeDto = noticeService.getById(id);

        return new ApiSuccess<>(noticeDto, "공지 상세 조회 성공");
    }
}
