package com.depromeet.fairer.dto.feedback.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;


@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "피드백 이모지 별 개수 반환 객체", description = "피드백 이모지 별 개수 반환 객체")
public class FeedbackCountResponseDto {

    @ApiModelProperty(value = "텍스트 코멘트 개수")
    private Integer comment;

    @ApiModelProperty(value = "이모지 1 개수")
    private Integer emoji_1;

    @ApiModelProperty(value = "이모지 2 개수")
    private Integer emoji_2;

    @ApiModelProperty(value = "이모지 3 개수")
    private Integer emoji_3;

    @ApiModelProperty(value = "이모지 4 개수")
    private Integer emoji_4;

    @ApiModelProperty(value = "이모지 5 개수")
    private Integer emoji_5;

    @ApiModelProperty(value = "이모지 6 개수")
    private Integer emoji_6;

    public static FeedbackCountResponseDto from(Integer feedback, Integer emoji_1, Integer emoji_2, Integer emoji_3,
                                                Integer emoji_4, Integer emoji_5, Integer emoji_6){

        return new FeedbackCountResponseDtoBuilder()
                .comment(feedback - (emoji_1 + emoji_2 + emoji_3 + emoji_4 + emoji_5 + emoji_6))
                .emoji_1(emoji_1)
                .emoji_2(emoji_2)
                .emoji_3(emoji_3)
                .emoji_4(emoji_4)
                .emoji_5(emoji_5)
                .emoji_6(emoji_6)
                .build();
    }

}
