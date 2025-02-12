package com.springboot.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;



    /*
     1. Q&A
      - 김용준 고객님 Q&A 총 질문 수: 1개 (Q1 - 미응답)
      25-02-12 01:38 PM 김용준 고객님 - Q1) 이 메서드에서는 주로 무슨 기능을 담당하나요?
      최용준 개발자님 답변 부탁드립니다.

      25-02-12 01:38 PM 김용준 고객님 - Q2) 아래와 같이 생성자를 구현할 때 super(exceptionCode.getMessage()); 를 사용하는 이유에 대해서 설명해주세요.
      public BusinessLogicException(ExceptionCode exceptionCode) {
      super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
     최용준 개발자님 답변 부탁드립니다.

     */

public class BusinessLogicException extends RuntimeException {

    @Getter
    private ExceptionCode exceptionCode;

    public BusinessLogicException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }





}
