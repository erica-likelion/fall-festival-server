package likelion.festival.controller;

import likelion.festival.dto.FortuneRequestDto;
import likelion.festival.dto.FortuneResponseDto;
import likelion.festival.service.FortuneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/fortunes")
public class FortuneController {

    private final FortuneService fortuneService;

    public FortuneController(FortuneService fortuneService) {
        this.fortuneService = fortuneService;
    }

    @PostMapping("/")
    public ResponseEntity<FortuneResponseDto> create(@RequestBody FortuneRequestDto req){
        if(req == null || req.getName() == null || req.getName().isBlank()
                || req.getBirth() == null || req.getBirth().isBlank()){
            return ResponseEntity.badRequest().build();
        }
        //하루 기준으로 동일이름+동일생년월일 요청은 같은 PK -> 같은 운세로 반환
        FortuneResponseDto res = fortuneService.getOrCreate(req.getName(), req.getBirth());
        return ResponseEntity.ok(res);
    }
}
