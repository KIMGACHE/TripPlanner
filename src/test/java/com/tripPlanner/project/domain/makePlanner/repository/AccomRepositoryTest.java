package com.tripPlanner.project.domain.makePlanner.repository;

import com.tripPlanner.project.domain.makePlanner.dto.AccomDto;
import com.tripPlanner.project.domain.makePlanner.entity.Accom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AccomRepositoryTest {

    @Autowired
    private AccomRepository accomRepository;

    @Test
    public void t1() {
        List<Accom> accoms = accomRepository.selectMiddle(126.35320496487589,34.72056346726476,127.35320496487589,35.72056346726476);
        List<AccomDto> list = new ArrayList<AccomDto>();
        accoms.forEach((accom)->{
//            System.out.println("..."+accom);
            list.add(AccomDto.entityToDto(accom));
        });
        System.out.println(list);
    }
}