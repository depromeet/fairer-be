package com.depromeet.fairer.dto.feedback.response;

import com.depromeet.fairer.domain.feedback.Feedback;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "피드백 업데이트 반환 객체", description = "피드백 업데이트 반환 객체")
public class FeedbackUpdateResponseDto {

    @ApiModelProperty(value = "피드백 id")
    private Long feedbackId;

    @ApiModelProperty(value = "텍스트 코멘트")
    private String comment;

    @ApiModelProperty(value = "이모지 id")
    private Integer emoji;

    public static FeedbackUpdateResponseDto from(Feedback feedback){
        return FeedbackUpdateResponseDto.builder()
                .feedbackId(feedback.getFeedbackId())
                .comment(feedback.getComment())
                .emoji(feedback.getEmoji())
                .build();
    }

}
