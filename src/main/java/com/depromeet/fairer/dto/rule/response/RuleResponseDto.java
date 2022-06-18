package com.depromeet.fairer.dto.rule.response;

import com.depromeet.fairer.domain.rule.Rule;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ApiModel(value = "개별 규칙 반환 객체", description = "개별 규칙 반환 객체")
public class RuleResponseDto {

    @ApiModelProperty(value = "규칙 id")
    private Long ruleId;

    @ApiModelProperty(value = "규칙 내용")
    private String ruleName;

    public static RuleResponseDto createRule(Rule rule){
        return RuleResponseDto.builder()
                .ruleId(rule.getRuleId())
                .ruleName(rule.getRuleName())
                .build();
    }
}
