package com.depromeet.fairer.vo.houseWork;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor @AllArgsConstructor
@ApiModel(value = "특정 멤버, 날짜에 할당된 집안일 모두 조회 반환 객체", description = "특정 멤버, 날짜에 할당된 집안일 모두 조회 반환 객체")
public class HouseWorkAndAssigneeResponseDto {

    @ApiModelProperty(value = "집안일, 담당자 객체 리스트")
    private List<HouseWorkAndAssigneeVo> houseWorkAndAssigneeVos;

    @ApiModelProperty(value = "완료한 집안일 갯수")
    private int successCount;

    @ApiModelProperty(value = "남은 집안일 갯수")
    private int leftCount;

    public HouseWorkAndAssigneeResponseDto(List<HouseWorkAndAssigneeVo> houseWorkAndAssigneeVos, long successCount, long leftCount) {
        this.houseWorkAndAssigneeVos = houseWorkAndAssigneeVos;
        this.successCount = (int) successCount;
        this.leftCount = (int) leftCount;
    }
}
