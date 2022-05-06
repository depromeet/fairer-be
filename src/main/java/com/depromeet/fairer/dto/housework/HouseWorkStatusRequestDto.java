package com.depromeet.fairer.dto.housework;

import lombok.Data;
import lombok.Getter;
import javax.validation.constraints.NotNull;

@Data
@Getter
@NotNull
public class HouseWorkStatusRequestDto {
    private String toBeStatus;
}
