package com.depromeet.fairer.dto.feedback.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@ApiModel(value = "피드백 업데이트 요청 객체", description = "피드백 업데이트 요청 객체")
public class FeedbackUpdateRequestDto {

    @ApiModelProperty(value = "텍스트 코멘트")
    private String comment;

}
