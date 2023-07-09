package com.asap.server.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Error {

    /**
    400 BAD REQUEST
     **/
    INVALID_MULTIPART_EXTENSION_EXCEPTION(HttpStatus.BAD_REQUEST, "지원하지 않는 파일입니다"),
<<<<<<< Updated upstream
    INVALID_MEETING_URL_EXCEPTION(HttpStatus.BAD_REQUEST, "유효하지 않는 URL 입니다."),

=======
    /*
    * 401 UnauthorizedException
     */
    
>>>>>>> Stashed changes
    /**
     401 UNAUTHORIZED
     **/
<<<<<<< Updated upstream
    TOKEN_TIME_EXPIRED_EXCEPTION(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
=======
    NOT_FOUND_IMAGE_EXCEPTION(HttpStatus.NOT_FOUND, "이미지가 존재하지 않습니다."),
    NOT_FOUND_SAVE_IMAGE_EXCEPTION(HttpStatus.NOT_FOUND, "이미지가 저장되지 않았습니다."),
>>>>>>> Stashed changes

    /**
     * 500 INTERNAL SERVER ERROR
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
