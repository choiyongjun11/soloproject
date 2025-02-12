package com.springboot.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


 /*
    공백 처리 기능 구현 - 다른 패키지 내에서도 쉽게 사용할 수 있게 @ 애너테이션 기반으로 구성했습니다.

    요구사항)
    1. Q&A
    - 김용준 고객님 총 질문 수: 3개 (Q1 - 미응답(ASAP 요청 발생), Q2 - 응답 완료, Q3 - 응답 완료 - 추후 보충 필요)

   25-02-12 09:45 AM 김용준 고객님 - Q1) NotSpace 인터페이스에서 아래 Class를 지정하는 이유는?
   Class<?>[] group() default {};
   Class<? extends Payload>[] payload() default{};

   25-02-12 10:45 AM 김용준 고객님 - ASAP 요청) 최용준 개발자님 답변 부탁드립니다..

   25-02-12 09:45 AM 김용준 고객님 - Q2) NotSpace 인터페이스에서 아래 애너테이션을 사용하는 이유는?
   @Target(ElementType.FIELD)
   @Retention(RetentionPolicy.RUNTIME)
   @Constraint(validatedBy = {NotSpaceValidator.class})

   25-02-12 10:43 AM 최용준 개발자님 - 답변
   @Retention(RetentionPolicy.RUNTIME) 이는 어노테이션 유지 기간을 지정하며 런타임(실행 중)까지 유지됨을 의미한다.
   RUNTIME으로 설정하면 프로그램 실행 중에도 어노테이션 정보를 읽고 활용할 수 있다.

   25-02-12 11:41 AM 김용준 고객님 - Q3) 그럼 어떻게 실행 중에 확인 할 수 있을까요??
   25-02-12 12:15 PM 최용준 개발자님 - 답변 (Chat gpt 활용)
   Reflection(리플렉션) API를 사용하여 어노테이션 정보를 가져올 수 있습니다.


 */
@Target(ElementType.FIELD) //클래스의 필드에만 붙일 수 있는 어노테이션이다.
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {NotSpaceValidator.class})
public @interface NotSpace {
    // 입력값이 공백이면 안되게 해주며 잘못된 공백이 들어오지 않게 하기 위해 사용자에게 메시지로 알려줍니다.
    // 어노테이션(annotation)에서 기본 메시지를 정의하는 부분입니다.
    String message() default "공백이 아니어야 합니다."; //유효성 검사(validation) 실패 이유를 설명합니다.
    Class<?>[] group() default {};
    Class<? extends Payload>[] payload() default{};

}
