package likelion.festival.controller;

import likelion.festival.dto.ContentResponse;
import likelion.festival.service.ContentService;
import likelion.festival.exception.ApiException;
import likelion.festival.exception.ApiSuccess;
import likelion.festival.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/contents")
public class ContentController {

    private final ContentService contentService;

    @GetMapping
    public ResponseEntity<ApiSuccess<List<ContentResponse>>> getCurrentContents(){
        List<ContentResponse> contents = contentService.findCurrentContent();
        if(contents.isEmpty()){
            throw new ApiException(ErrorCode.CONTENT_NOT_FOUND);
        }
        return ResponseEntity.ok(ApiSuccess.of(contents, "현재 진행중인 컨텐츠 조회 성공"));
    }

}
