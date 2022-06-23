package com.depromeet.fairer.global.exception;

public class CannotJoinTeamException extends RuntimeException {
    public CannotJoinTeamException() {
        super("이미 팀에 소속되어 있는 회원입니다.");
    }
    public CannotJoinTeamException(String message) {
        super(message);
    }
}
