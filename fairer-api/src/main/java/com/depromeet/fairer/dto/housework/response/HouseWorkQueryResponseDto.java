package com.depromeet.fairer.dto.housework.response;

import com.depromeet.fairer.domain.housework.HouseWork;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HouseWorkQueryResponseDto {

    private HouseWork houseWork;

    private Long houseWorkCompleteId;
}
