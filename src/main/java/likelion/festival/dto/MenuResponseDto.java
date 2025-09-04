package likelion.festival.dto;

import likelion.festival.domain.Menu;

/**
 * 메뉴 응답 DTO
 *
 * @param id          메뉴 ID
 * @param name        메뉴명
 * @param category    메뉴 카테고리 (main, side, others)
 * @param description 메뉴 설명
 * @param price       메뉴 가격
 */
public record MenuResponseDto(
        Long id,
        String name,
        String category,
        String description,
        Integer price
) {
    public static MenuResponseDto from(Menu menu) {
        return new MenuResponseDto(
                menu.getId(),
                menu.getName(),
                menu.getCategory().name(),
                menu.getDescription(),
                menu.getPrice()
        );
    }
}
