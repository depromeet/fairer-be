package com.depromeet.fairer.dto.housework.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "집안일 상태 요청 객체", description = "집안일 상태 요청 객체")
public class HouseWorkStatusRequestDto {
    @NotNull
    @ApiModelProperty(value = "집안일 완료 요쳥 플래그")
    private int toBeStatus;
}
