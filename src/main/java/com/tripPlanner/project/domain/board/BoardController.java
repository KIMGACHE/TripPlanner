package com.tripPlanner.project.domain.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
//@RequiredArgsConstructor
//@RequestMapping("/planner")
@Slf4j
public class BoardController {


    @GetMapping("/planner/board")
    public String board() {
      log.info("GET /planner/board");

        return "planner/board";
    }


}
