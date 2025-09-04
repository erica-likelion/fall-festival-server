package likelion.festival.dto;

import likelion.festival.domain.Notice;
import java.util.List;

public record NoticeDto(
        Long id,
        String title,
        String content,
        List<String> images,
        String tag
) {
    // 엔티티 Notice를 dto로 변환해주는 헬퍼
    // 컨트롤러/서비스에서 new 호출 대신 NoticeDto.from(n)으로 사용
    public static NoticeDto from(Notice n) {
        return new NoticeDto(
                n.getId(),
                n.getTitle(),
                n.getContent(),
                n.getImages(),
                n.getTag()
        );
    }
}
