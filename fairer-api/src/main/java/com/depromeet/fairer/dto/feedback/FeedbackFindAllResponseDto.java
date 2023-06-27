package com.depromeet.fairer.dto.feedback;

import com.depromeet.fairer.dto.feedback.response.FeedbackFindOneResponseDto;
import com.depromeet.fairer.vo.houseWork.HouseWorkCompFeedbackVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "완료한 집안일의 모든 피드백 조회 반환 객체 - api 반환 객체")
public class FeedbackFindAllResponseDto {

    @ApiModelProperty(value = "피드백 갯수")
    private int feedbackCount;

    @ApiModelProperty(value = "완료한 집안일 피드백 리스트")
    private List<FeedbackFindOneResponseDto> feedbackFindOneResponseDtoList;

    public static FeedbackFindAllResponseDto from(List<FeedbackFindOneResponseDto> dtoList) {
        return FeedbackFindAllResponseDto.builder()
                    .feedbackCount(dtoList.size())
                    .feedbackFindOneResponseDtoList(dtoList)
                    .build();
    }
}
