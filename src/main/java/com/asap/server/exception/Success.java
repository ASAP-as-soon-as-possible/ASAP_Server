package com.asap.server.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Success {

    /**
     * 200 OK SUCCESS
     */
    CONFIRM_MEETING_SUCCESS(HttpStatus.OK, "회의 시간 확정 성공입니다."),
    FIXED_MEETING_SUCCESS(HttpStatus.OK, "큐카드 조회 성공입니다."),
    FIND_TIME_TABLE_SUCCESS(HttpStatus.OK, "종합 일정 시간표 조회 성공입니다."),
    FIND_MEETING_SCHEDULE_SUCCESS(HttpStatus.OK, "회의 선택지가 성공적으로 조회되었습니다."),
    MEETING_VALIDATION_SUCCESS(HttpStatus.OK, "유효한 회의입니다."),
    LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공입니다"),
    /**
     * 201 CREATED SUCCESS
     */
    CREATE_MEETING_SUCCESS(HttpStatus.CREATED, "회의가 성공적으로 생성되었습니다."),
    CREATE_MEETING_TIME_SUCCESS(HttpStatus.CREATED, "참여자 회의 가능 시간 입력을 성공하였습니다."),
    CREATE_HOST_TIME_SUCCESS(HttpStatus.CREATED, "방장의 회의 가능 시간이 성공적으로 입력되었습니다.");
    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
