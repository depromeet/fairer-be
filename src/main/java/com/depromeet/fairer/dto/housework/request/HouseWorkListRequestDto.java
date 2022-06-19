package com.depromeet.fairer.dto.housework.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class HouseWorkListRequestDto {

    @ApiModelProperty(value = "생성할 집안일 목록")
    @NotNull
    private List<HouseWorkRequestDto> houseWorks;
}
