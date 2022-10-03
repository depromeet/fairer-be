package com.depromeet.fairer.dto.houseworkComplete.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "집안일 완료 반환 객체")
public class HouseWorkCompleteResponseDto {

    @ApiModelProperty(value = "집안일 완료 ID")
    private Long houseWorkCompleteId;

    public static HouseWorkCompleteResponseDto create(Long houseWorkCompleteId){
        return HouseWorkCompleteResponseDto.builder()
                .houseWorkCompleteId(houseWorkCompleteId)
                .build();
    }

}

