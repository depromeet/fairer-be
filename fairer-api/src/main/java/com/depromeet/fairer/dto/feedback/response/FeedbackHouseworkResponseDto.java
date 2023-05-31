package com.depromeet.fairer.dto.feedback.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "집안일 리스트 조회 api - 피드백 반환 객체", description = "집안일 리스트 조회 api - 피드백 반환 객체")
public class FeedbackHouseworkResponseDto {

//    @ApiModelProperty(value = "이모지 종류")
//    private Integer feedbackEmoji;

    @ApiModelProperty(value = "피드백 개수")
    private Integer feedbackNum;

    @ApiModelProperty(value = "피드백 id")
    private Long feedbackId;

    @ApiModelProperty(value = "본인 작성 여부  - 본인이 작성 : true, 아님 : false")
    private boolean myFeedback;

    public static FeedbackHouseworkResponseDto from(int feedbackNum, long feedbackId, boolean myFeedback){

        return FeedbackHouseworkResponseDto.builder()
              //  .feedbackEmoji(feedbackEmoji)
                .feedbackNum(feedbackNum)
                .feedbackId(feedbackId)
                .myFeedback(myFeedback)
                .build();
    }


}
