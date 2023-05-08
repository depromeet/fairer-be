package com.depromeet.fairer.vo.houseWork;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HouseWorkCompFeedbackVO {

    private Long feedbackId;

    private String memberName;

    private String profilePath;

    private String comment;

    private Integer emoji;
}
