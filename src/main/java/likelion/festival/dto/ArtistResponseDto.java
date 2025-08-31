package likelion.festival.dto;

import likelion.festival.domain.Artist;

import java.util.List;

/**
 * 가수 응답 DTO
 *
 * @param id    가수 ID
 * @param name  이름
 * @param genre 장르
 * @param image 프로필 이미지 URL
 * @param songs 대표곡 목록
 */
public record ArtistResponseDto(
        Long id,
        String name,
        String genre,
        String image,
        List<SongResponseDto> songs
) {
    public static ArtistResponseDto from(Artist artist) {
        List<SongResponseDto> songs = artist.getSongs()
                .stream()
                .map(SongResponseDto::from)
                .toList();

        return new ArtistResponseDto(
                artist.getId(),
                artist.getName(),
                artist.getGenre(),
                artist.getImage(),
                songs
        );
    }
}
