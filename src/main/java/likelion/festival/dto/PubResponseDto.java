package likelion.festival.dto;

import likelion.festival.domain.Pub;

/**
 * 주점 응답 DTO
 *
 * @param id           주점 ID
 * @param location     주점 위치
 * @param type         주점 종류
 * @param affiliation  소속 단체
 * @param name         주점명
 * @param takeout      포장 가능 여부
 * @param profileImage 프로필 이미지 URL
 * @param posterImage  포스터 이미지 URL
 */
public record PubResponseDto(
        Long id,
        String location,
        String type,
        String affiliation,
        String name,
        Boolean takeout,
        String profileImage,
        String posterImage
) {
    public static PubResponseDto from(Pub pub) {
        return new PubResponseDto(
                pub.getId(),
                pub.getLocation(),
                pub.getType(),
                pub.getAffiliation(),
                pub.getName(),
                pub.getTakeout(),
                pub.getProfileImage(),
                pub.getPosterImage()
        );
    }
}
