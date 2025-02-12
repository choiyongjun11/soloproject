package com.springboot.validator;


import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/*
요구사항)
1. ConstraintValidator 구현체 가져오기
   - 구현체에 <NotSpace 애너테이션, String> 을 매개변수로 받습니다.

2. initialize, isValid 메서드 활용합니다.
    - 초기 메서드 양식
    @Override
    public void initialize(NotSpace constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return false;
    }

3. Q&A
  - 김용준 고객님 Q&A 총 질문 수: 2개 (Q1 - 미응답(ASAP 요청 발생), Q2 - 미응답(ASAP 요청 발생) )

    초기 메서드 양식과 아래에 구현한 메서드의 차이점에 대해 알려주세요.

    김용준 고객님 - Q1. <NotSpace, String> 매개변수 사용 이유에 대해 알려주세요.
    25-02-12 12:09 PM 김용준 고객님 - ASAP 요청) 최용준 개발자님 답변 부탁드립니다.

    김용준 고객님 - Q2.  return value == null || StringUtils.hasText(value);
    다음과 같은 return 문을 보면 왜 value와 StringUtils라는 인터페이스에 hasText 메서드를 반환하는가?
    25-02-12 12:09 PM 김용준 고객님 - ASAP 요청) 최용준 개발자님 답변 부탁드립니다.


*/

public class NotSpaceValidator implements ConstraintValidator<NotSpace, String> {
    @Override
    public void initialize(NotSpace constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || StringUtils.hasText(value);
    }





}
