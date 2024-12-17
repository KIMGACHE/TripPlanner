package com.tripPlanner.project.domain.tourist;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequest {
    private String keyword;
    private String regionCode;
    private String hashtag;
    private String pageNo;
    private String arrange;
}
