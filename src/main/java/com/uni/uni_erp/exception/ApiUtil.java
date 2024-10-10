package com.uni.uni_erp.exception;

import lombok.Data;

@Data
public class ApiUtil<T> {

    private Integer status; // 응답 상태 코드 저장 (200, 400, 500)

    private String message; // 응답 메세지 저장(성공, 실패 문자열)

    private T body; // 응답의 실제 데이터 저장 (제네릭 활용)

    // 성공적인 응답을 반환할 때 사용하는 생성자
    public ApiUtil(T body) {
        this.status = 200;
        this.message = "성공";
        this.body = body;
    }

    // 커스텀 상태코드와 메시지를 반환시킬 때 사용하는 생성자 (예 : 에러 응답)
    public ApiUtil(Integer status, String message) {
        this.status = status;
        this.message = message;
        this.body = null;
    }

}
