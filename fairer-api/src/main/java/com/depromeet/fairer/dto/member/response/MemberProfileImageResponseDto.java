package com.depromeet.fairer.dto.member.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MemberProfileImageResponseDto {
    List<String> bigImageList;
}
