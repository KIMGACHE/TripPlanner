package com.tripPlanner.project.domain.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planner")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // BoardService에서 공개된 플래너와 썸네일 이미지를 함께 가져와서 DTO 리스트로 반환
    @GetMapping("/board")
    public ResponseEntity<List<BoardDto>> getPlannerList() {
        return ResponseEntity.ok(boardService.getPlannersForBoard());
    }

    @GetMapping("/plans/total")
    public ResponseEntity<Integer> getTotalPlans(){
        int totalPlans = boardService.getTotalPlans();
        return ResponseEntity.ok(totalPlans);
    }

}
