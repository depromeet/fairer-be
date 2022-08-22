package com.depromeet.fairer.dto.rule.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ApiModel(value = "팀 규칙 반환 객체", description = "팀 규칙 반환 객체")
public class RulesResponseDto {

    @ApiModelProperty(value = "팀 id")
    private Long teamId;

    @ApiModelProperty(value = "(규칙id + 규칙 내용) list")
    private List<RuleResponseDto> ruleResponseDtos;

    public static RulesResponseDto createRules(Long teamId, List<RuleResponseDto> ruleResponseDtos){
        return RulesResponseDto.builder()
                .teamId(teamId)
                .ruleResponseDtos(ruleResponseDtos)
                .build();
    }
}
