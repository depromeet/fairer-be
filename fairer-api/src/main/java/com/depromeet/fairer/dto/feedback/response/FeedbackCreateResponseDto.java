package com.depromeet.fairer.dto.feedback.response;

import com.depromeet.fairer.domain.feedback.Feedback;
import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.dto.feedback.request.FeedbackCreateRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@ApiModel(value = "피드백 생성 응답 객체")
public class FeedbackCreateResponseDto {

    @ApiModelProperty(value = "피드백 id")
    @NotNull
    private Long feedbackId;

    public static FeedbackCreateResponseDto create(Long feedbackId) {
        return FeedbackCreateResponseDto.builder()
                .feedbackId(feedbackId)
                .build();
    }
}
