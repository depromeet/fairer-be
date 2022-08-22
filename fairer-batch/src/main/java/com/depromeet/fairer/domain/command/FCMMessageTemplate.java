package com.depromeet.fairer.domain.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FCMMessageTemplate {
    NOT_COMPLETE_HOUSEWORK("아직 끝내지 못한 집안일 %s개", "'%s'이 남아있어요!\uD83D\uDE22"),
    INDUCE_ADD_HOUSEWORK("오늘 할 집안일을 추가해보세요!", "오늘도 평화롭고 깨끗한 %s 만들어봐요!");

    private final String title;
    private final String body;
}
