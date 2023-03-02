package com.depromeet.fairer.domain.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FCMMessageTemplate {
    NOT_COMPLETE_HOUSEWORK("아직 끝내지 못한 집안일 %s개", "'%s'이 남아있어요!\uD83D\uDE22"),
    INDUCE_ADD_HOUSEWORK("작은 집안일부터 실천해보세요!", "최근에 추가한 집안일이 없어요. 집을 관리해보세요\uD83D\uDE0A"),
    DO_HOUSEWORK("'%s' 할 시간이에요", "%s에 하기로 했어요\uD83D\uDE4C"),
    OTHER_MEMBER_COMPLETE_HOUSEWORK("%s님이 오늘 집안일 모두 완료", "수고한 %s님께 마음의 박수를 드려요\uD83D\uDC4F");

    private final String title;
    private final String body;
}
