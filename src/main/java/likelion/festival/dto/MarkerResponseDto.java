package likelion.festival.dto;

import likelion.festival.domain.Marker;

import java.util.List;

/**
 * 마커 응답 DTO
 *
 * @param id         마커 ID
 * @param category   카테고리
 * @param image      이미지 URL
 * @param name       이름
 * @param latitude   위도
 * @param longitude  경도
 * @param time       시간
 * @param closedDays 닫는 일차 목록
 * @param linkType   연결 타입
 * @param linkId     연결 대상 ID
 */
public record MarkerResponseDto(
        Long id,
        String category,
        String image,
        String name,
        Double latitude,
        Double longitude,
        String time,
        List<String> closedDays,
        String linkType,
        Long linkId
) {

    public static MarkerResponseDto from(Marker marker) {
        LinkInfo linkInfo = checkLinkInfo(marker);

        return new MarkerResponseDto(
                marker.getId(),
                marker.getCategory().getValue(),
                marker.getImage(),
                marker.getName(),
                marker.getActualLatitude(),
                marker.getActualLongitude(),
                marker.getTime(),
                marker.getClosedDays() != null ? marker.getClosedDays() : List.of(),
                linkInfo.type(),
                linkInfo.id()
        );
    }

    /**
     * 마커 연결 정보를 확인하는 메서드
     */
    private static LinkInfo checkLinkInfo(Marker marker) {
        if (marker.hasEventLink() && marker.getEvent().getNotice() != null) {
            return new LinkInfo("NOTICE_DETAIL", marker.getEvent().getNotice().getId());
        }

        if (marker.hasPubLink()) {
            return new LinkInfo("PUB_DETAIL", marker.getPub().getId());
        }

        if (marker.hasNoticeLink()) {
            return new LinkInfo("NOTICE_DETAIL", marker.getNotice().getId());
        }

        return new LinkInfo("STATIC", null);
    }

    /**
     * 연결 정보 내부 Record
     */
    private record LinkInfo(String type, Long id) {
    }
}
