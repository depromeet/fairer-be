package com.depromeet.fairer.dto.feedback.response;

import com.depromeet.fairer.vo.houseWork.HouseWorkCompFeedbackVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
@ApiModel(value = "완료한 집안일의 모든 피드백 조회 반환 객체 - 피드백 단일 객체")
public class FeedbackFindOneResponseDto {

    @ApiModelProperty(value = "피드백 id")
    private Long feedbackId;

    @ApiModelProperty(value = "피드백 작성자 이름")
    private String memberName;

    @ApiModelProperty(value = "본인 작성 여부  - 본인이 작성 : true, 아님 : false")
    private boolean myFeedback;

    @ApiModelProperty(value = "피드백 작성자 프로필 이미지 경로")
    private String profilePath;

    @ApiModelProperty(value = "텍스트 피드백")
    private String comment;

    @ApiModelProperty(value = "이모지 id")
    private Integer emoji;

    public static FeedbackFindOneResponseDto fromVO(HouseWorkCompFeedbackVO VO) {
        return FeedbackFindOneResponseDto.builder()
                .feedbackId(VO.getFeedbackId())
                .memberName(VO.getMemberName())
                .myFeedback(VO.isMyFeedback())
                .profilePath(VO.getProfilePath())
                .comment(VO.getComment())
                .emoji(VO.getEmoji())
                .build();
    }

}
