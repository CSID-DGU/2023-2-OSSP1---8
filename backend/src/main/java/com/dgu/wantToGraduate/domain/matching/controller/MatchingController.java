package com.dgu.wantToGraduate.domain.matching.controller;

import com.dgu.wantToGraduate.domain.matching.dto.MatchingDto;
import com.dgu.wantToGraduate.domain.matching.service.MatchingService_Q_version;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.dgu.wantToGraduate.domain.matching.service.MatchingService_Q_version.totalGroupCnt;
import static com.dgu.wantToGraduate.domain.matching.service.MatchingService_Q_version.totalScore;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/matching")
public class MatchingController {

    private final MatchingService_Q_version m0atchingServiceQversion;
    @PostMapping("/run")
    public ResponseEntity<?> match(@RequestBody MatchingDto.RequestDto requestDto){
        MatchingDto.ResponseDto matching = m0atchingServiceQversion.matching(requestDto);
        if(matching != null) m0atchingServiceQversion.testEvaluatePriority(matching);
        log.info("[최종요소]\nToTscore: {} \n ToTcnt : {}", totalScore, totalGroupCnt);
        if(totalGroupCnt>=3000) {
            log.info("[최종] matching: {}", (double)totalScore/9000*100);
        }


        return ResponseEntity.ok(matching);
    }
}
