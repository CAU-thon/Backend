package com.nuneddine.server.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-001", "해당 ID를 가진 사용자를 찾을 수 없습니다."),
    USER_NOT_FOUND_GIVEN_KAKAO_ID(HttpStatus.NOT_FOUND, "USER-002", "해당 kakaoID를 가진 사용자를 찾을 수 없습니다."),
    AWS_S3_ACCESS_DENIED(HttpStatus.FORBIDDEN, "AWS-001", "AWS S3에 접근할 수 있는 권한이 없거나 인증에 실패했습니다."),
    AWS_S3_NOT_CONNECTED(HttpStatus.SERVICE_UNAVAILABLE, "AWS-002", "AWS S3 연결에 실패했습니다."),
    FAILED_TO_UPLOAD_FILE(HttpStatus.BAD_REQUEST, "AWS-003", "파일 읽기 오류 혹은 잘못된 입력입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
