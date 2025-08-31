package likelion.festival.dto;

import likelion.festival.domain.Song;

/**
 * 대표곡 응답 DTO
 *
 * @param id    대표곡 ID
 * @param title 제목
 * @param link  유튜브 뮤직비디오 URL
 * @param image 대표곡 이미지 URL
 */
public record SongResponseDto(
        Long id,
        String title,
        String link,
        String image
) {
    public static SongResponseDto from(Song song) {
        return new SongResponseDto(
                song.getId(),
                song.getTitle(),
                song.getLink(),
                song.getImage()
        );
    }
}
