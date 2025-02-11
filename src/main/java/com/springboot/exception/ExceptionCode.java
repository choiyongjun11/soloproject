package com.springboot.exception;

import lombok.Getter;

public enum ExceptionCode {
/*
    잘못된 요청에 대한 상태 코드 구현 클래스
     <에러 코드, 상태 메시지를 클라이언트에게 전달해야 합니다.>
     EX) 요청 메시지를 찾지 못했습니다. (4xx 코드, 5xx 코드) / 2xx 코드 - ok, 3xx 코드 - retry

     4xx 코드 - Client errors - 클라이언트의 요청이
     400 - bad Request: 클라이언트의 요청이 유효하지 않아 더 이상 작업을 진행하지 않는 경우를 의미.
     401 - Unauthorized : 클라이언트의 권한이 없기 때문에 작업을 진행할 수 없는 경우를 의미.
     403 - Forbidden: 클라이언트가 권한이 없기 때문에 작업을 진행할 수 없는 경우를 의미.
     404 - Not Found: 클라이언트가 요청한 자원이 존재하지 않는다는 경우를 의미.
     405 - Method Not Allowed: 클라이언트의 요청이 허용되지 않는 메소드인 경우를 의미.
     409 - Conflict: 클라이언트의 요청이 서버의 상태와 충돌이 발생한 경우를 의미.

     429 - Too Many Requests: 클라이언트가 일정 시간 동안 너무 많은 요청을 보낸 경우를 의미.
     ex 1) 기밀성에 대한 공격
     해커는 사용자의 비밀번호를 알아내기 위해 POST/login API에 password를 무차별로 대입하면서 요청할 수 있다.
     서버 입장에선 자원의 기밀성(Confidentiality) 피해를 입을 수 있는 공격이면서, 이러한 무차별 요청으로 다른 요청을 처리할 수 없거나
     처리가 늦을 수 있는 가용성(Availability)에 피해를 입을 수 있다.
     서버는 이러한 공격에 대비해 인증 API의 경우 각 클라이언트는 N 시간 동안 N 회만 요청 가능하다는 룰을 정하고
     이것을 초과하면 429 상태코드를 응답해야 한다.

     ex 2) 가용성에 대한 공격
     해커는 시스템에 과부하를 주기 위해 특정 API에 지속적으로 요청을 보낼 수 있다. 해커의 비정상적인 요청으로 인해 실제로 서비스를
     받아야 할 정상적인 사용자가 서비스를 받지 못하는 가용성(Availability)에 피해를 입을 수 있다.
     서버는 이러한 공격에 대비해 클라이언트의 요청에 대해 n 시간 동안 n회 이상 요청 한다면 그 이후의 요청은 429 상태 코드로 응답해야 한다.


    5xx 코드 - Server errors - 서버 오류로 인해 요청을 수행할 수 없다는 의미.

 */

    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(402,"Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method not Allowed"),
    CONFLICT(409, "Conflict"),
    TOO_MANY_REQUESTS(429, "Too many Requests");



    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int code,String message) {
        this.status = code;
        this.message = message;
    }

}
