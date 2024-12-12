package com.tripPlanner.project.domain.tourist;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tourist")
public class TouristController {

    @GetMapping("/travelcourse")
    public void tourist() {

    }

    @GetMapping("/travelcourse-info")
    public void touristInfo() {

    }
}
