package com.depromeet.fairer.dto.housework.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(value = "집안일 리스트 생성 요청 객체", description = "집안일 리스트 생성 요청 객체")
public class HouseWorksCreateRequestDto {

    @ApiModelProperty(value = "생성할 집안일 목록")
    @NotNull
    private List<HouseWorkUpdateRequestDto> houseWorks;
}
