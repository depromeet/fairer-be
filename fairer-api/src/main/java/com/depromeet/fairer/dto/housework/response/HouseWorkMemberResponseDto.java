package com.depromeet.fairer.dto.housework.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class HouseWorkMemberResponseDto {
    private Long teamId;
    private List<HouseWorkDateResponseDto> houseWorkDateResponseDtos;

    public static HouseWorkMemberResponseDto from(Long teamId, List<HouseWorkDateResponseDto> houseWorkDateResponseDtos){
        return new HouseWorkMemberResponseDtoBuilder()
                .teamId(teamId)
                .houseWorkDateResponseDtos(houseWorkDateResponseDtos)
                .build();
    }
}
