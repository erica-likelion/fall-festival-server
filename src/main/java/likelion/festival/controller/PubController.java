package likelion.festival.controller;

import likelion.festival.dto.MenuResponseDto;
import likelion.festival.dto.PubResponseDtos.PubDetailDto;
import likelion.festival.dto.PubResponseDtos.PubSummaryDto;
import likelion.festival.exception.ApiSuccess;
import likelion.festival.service.PubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 주점 관련 REST API 컨트롤러
 */
@RestController
@RequestMapping("/api/pubs")
@RequiredArgsConstructor
public class PubController {

    private final PubService pubService;

    /**
     * 전체 주점 목록 조회
     */
    @GetMapping
    public ResponseEntity<ApiSuccess<List<PubSummaryDto>>> getAllPubs() {
        List<PubSummaryDto> pubs = pubService.getAllPubs();
        return ResponseEntity.ok(ApiSuccess.of(pubs, "주점 목록 조회 완료"));
    }

    /**
     * 특정 주점 정보 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiSuccess<PubDetailDto>> getPubById(@PathVariable Long id) {
        PubDetailDto pub = pubService.getPubById(id);
        return ResponseEntity.ok(ApiSuccess.of(pub, "주점 정보 조회 완료"));
    }

    /**
     * 특정 주점의 메뉴 목록 조회
     */
    @GetMapping("/{id}/menus")
    public ResponseEntity<ApiSuccess<List<MenuResponseDto>>> getMenusByPubId(@PathVariable Long id) {
        List<MenuResponseDto> menus = pubService.getMenusByPubId(id);
        return ResponseEntity.ok(ApiSuccess.of(menus, "주점 메뉴 목록 조회 완료"));
    }
}
