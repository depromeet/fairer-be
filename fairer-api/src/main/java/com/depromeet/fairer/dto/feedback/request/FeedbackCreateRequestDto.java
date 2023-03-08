package com.depromeet.fairer.dto.feedback.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Data
@ApiModel(value = "피드백 생성 요청 객체", description = "피드백 생성 요청 객체")
public class FeedbackCreateRequestDto {

    @ApiModelProperty(value = "완료된 집안일 id")
    @NotNull
    private Long houseCompleteId;

    @ApiModelProperty(value = "텍스트 코멘트")
    private String comment;

    @ApiModelProperty(value = "이모지 id")
    private Integer emoji;
}
