package com.depromeet.fairer.dto.housework.request;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class HouseWorkStatusRequestDto {
    @NotNull
    private int toBeStatus;
}
