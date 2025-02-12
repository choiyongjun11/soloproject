package com.springboot.exception;

import lombok.Getter;

public enum ExceptionCode {
/*
    잘못된 요청에 대한 상태 코드 구현 클래스
     에러 코드, 상태 메시지를 클라이언트에게 전달해야 한다.
     요구사항)
     1. 요청 메시지를 찾지 못했습니다. (4xx 코드, 5xx 코드) // 다른 코드  -  2xx 코드 - ok(정상), 3xx 코드 - retry(재요청)
     2. 예외처리에서 활용할 수 있도록 작성해주십시오. (enum 사용)
     - throw new CustomException(ExceptionCode.NOT_FOUND)
     - enum을 사용하면 오타방지 "Bad Request" 같은 문자열을 직접 입력하지 않아도 된다.

     3. Q&A
      - 김용준 고객님 Q&A 총 질문 수: 3개 (Q1 - 응답 완료, Q2 - 응답 완료, Q3 - 응답 완료)


     25-02-11 김용준 고객님 - Q1) 그럼 enum 이라는 것은 무엇인가요??
     25-02-12 강사님께 물어본 후 최용준 개발자님 - 답변) 상수들을 관리하는 집합으로 이루어져 있으며 클래스와 같은 용도로 사용합니다. BAD_REQUEST(400, "Bad Request")와 같이 대문자로 선언되는 이유도 '상수'이기 때문입니다.
     자세한 사항
     25-02-12 ChatGpt - enum(열거형)이란? 질문함.
     - enum(열거형, Enumeration)은 여러 개의 상수(Constant) 값을 한 곳에서 관리하는 특수한 클래스입니다.
     즉, 고정된 값(상수)을 표현할 때 사용하는 자료형이며, BAD_REQUEST(400, "Bad Request")처럼 대문자로 선언도는 이유도 상수이기 때문입니다.
     - enum 의 특징
     1. 상수들의 집합으로 정해진 값들만 가질 수 있습니다.
     2. 객체처럼 사용가능하며 필드, 생성자, 메서드를 가질 수 있습니다.
     3. 안전한 코드 작성이 가능하며 실수로 잘못된 값을 넣는 실수를 방지할 수 있습니다.
     4. switch 문과 함꼐 사용가능하며 코드 가독성이 좋아집니다.

     - enum을 사용하는 이유
     - 기본적으로 상수를 사용할 때는 static final 변수를 사용하지만, enum을 사용하면 더 구조적이고 의미 있는 코드를 만들 수 있습니다.
     - 기존의 static final 같은 경우 단순한 숫자로만 관리되기 떄문에, 어떤 의미인지 명확하지 않아 구별하기 힘들었습니다.
     - enum 을 사용하면 BAD_REQUEST(400, "Bad Request") 이 처럼 코드에 대한 가독성이 좋아지고, 의미가 명확해집니다. 대문자로 선언하는 이유도 상수이기 떄문인 것입니다.
     - 따라서 잘못된 값을 넣을 위험이 줄어든다는 것입니다.

--------------------------------------------------------------
     HTTP 상태 코드 (4XX 코드 , 5XX 코드) 개념
     4xx 코드 - Client errors - 클라이언트의 요청이 유효하지 않아 서버가 해당 요청을 수행하지 않았다는 의미

     400 - Bad Request: 클라이언트의 요청이 유효하지 않아 더 이상 작업을 진행하지 않는 경우를 의미.
     401 - Unauthorized : 클라이언트의 권한이 없기 때문에 작업을 진행할 수 없는 경우를 의미.
     403 - Forbidden: 클라이언트가 권한이 없기 때문에 작업을 진행할 수 없는 경우를 의미.
     404 - Not Found: 클라이언트가 요청한 자원이 존재하지 않는다는 경우를 의미.
     405 - Method Not Allowed: 클라이언트의 요청이 허용되지 않는 메소드인 경우를 의미.
     409 - Conflict: 클라이언트의 요청이 서버의 상태와 충돌이 발생한 경우를 의미.
     429 - Too Many Requests: 클라이언트가 일정 시간 동안 너무 많은 요청을 보낸 경우를 의미.

     ex 1) 기밀성에 대한 공격
     - 해커는 사용자의 비밀번호를 알아내기 위해 POST/login API에 password를 무차별로 대입하면서 요청할 수 있다.
     - 서버 입장에선 자원의 기밀성(Confidentiality) 피해를 입을 수 있는 공격이면서, 이러한 무차별 요청으로 다른 요청을 처리할 수 없거나
     처리가 늦을 수 있는 가용성(Availability)에 피해를 입을 수 있다.
     - 서버는 이러한 공격에 대비해 인증 API의 경우 각 클라이언트는 N 시간 동안 N 회만 요청 가능하다는
     룰을 정하고 이것을 초과하면 429 상태코드를 응답해야 한다.

     ex 2) 가용성에 대한 공격
     - 해커는 시스템에 과부하를 주기 위해 특정 API에 지속적으로 요청을 보낼 수 있다. 해커의 비정상적인 요청으로 인해 실제로 서비스를
     받아야 할 정상적인 사용자가 서비스를 받지 못하는 가용성(Availability)에 피해를 입을 수 있다.
     - 서버는 이러한 공격에 대비해 클라이언트의 요청에 대해 n 시간 동안 n회 이상 요청 한다면 그 이후의 요청은 429 상태 코드로 응답해야 한다.


    5xx 코드 - Server errors - 서버 오류로 인해 요청을 수행할 수 없다는 의미.

    500 - Internal Server Error - 서버 내부 문제 발생
    서버 사용량의 폭주로 인해 서비스가 일시적으로 중단되거나, 스크립트의 오류, 웹 서버 문제 등 다양한 원인으로 에러가 발생한다.
    501 - Not Implemented - 요청에 대해 구현되지 않아 수행하지 아니한다.
    클라이언트가 서버의 능력을 넘은 요청을 했을 때, 서버가 기능을 지원하지 않음을 나타내는 것입니다. 추후에 기능이 개발되면 지원한다는 의미.

    25-02-11 김용준 고객님 - Q2) 405 상태코드와 501 상태코드에 대한 설명이 비슷한것 같아 보입니다!

    25-02-11 최용준 개발자님 - 답변)
    클라이언트 - 405 상태코드(Method Not Allowed) VS  서버 - 501 상태코드(Not Implemented) 공통점과 차이점
    - 두 상태코드는 클라이언트의 HTTP 메서드 요청으로부터 서버에서 지원하지 않아 요청을 반려한다는 점에서 유사성을 가지고 있습니다.
    - 차이점으로는 405 상태코드는 클라이언트가 지원되지 않는 기능을 요청했다는 점에서 구체적이다.
    즉, 서버는 클라이언트가 요청한 해당 기능에 대해서 뭔지는 알고있지만 기능을 제한/금지함으로써, 메서드가 실제로 클라이언트에 의해 호출되지 않아야 함을 강조합니다.
    - 501 상태코드는 그냥 해당 메소드 요청에 대해 모른다는 것을 암시합니다.
    즉, 서버에서 개발이 덜 된 것이다. 따라서 아직 구현되지는 않았지만 가까운 미래에 구현되어 지원 될 수도 있고,
    아니면 아예 확실하게 제한함으로 405로 처리할 수도 있다.

    502 - Bad Gateway - 게이트웨이가 잘못되어, 서버가 잘못된 응답을 수신함을 의미합니다.
    - 서로 다른 프로토콜을 연결해주는 장치가 잘못된 프로토콜을 연결하거나 어느 쪽에 문제가 있어 통신이 제대로 되지 않을 때 발생된다.
    - 접속이 폭주하는 원인으로 서버에서 통신장애가 발생하였을 경우, 인터넷상의 서버가 다른 서버로부터 유효하지 않은 응답을 받은 경우,
    - 사용자 브라우저에 이상이 있거나, 잘못된 네트워크 연결 혹은 설정 등을 했을 때 발생됩니다.

    알아두기)
    1. 검색 봇 및 기타 크롤러가 사이트를 방문하는 속도에 영향을 줄 수 있습니다.
    - 502 상태코드를 반환하는 동안 서버가 오랫동안 다운된 상태라면 사이트의 검색 순위에 영향을 줄 수 있다.
    - 따라서 사이트가 일시적으로 다운된 경우에는 502 대신 503 상태코드를 반환하는 것이 좋습니다.

    2. Gateway - 서로 다른 네트워크(영역)으로 접근 하기 위한 출입문과 같은 역할로 보면 된다.
    - 일상생활에서 보면 노트북, 스위치 허브와 같은 장비로 인터넷을 사용하기 위해서는 ip 주소가 필요하다.
    - ip 주소를 가지고 외부와 통신하기 위해서 gateway 를 통해서 통신한다.

    503 - Service Unavailable - 서비스 이용 불가 (일시적)
    - 지금 서버가 요청을 처리해 줄 수 없지만, 나중에 가능함을 의미하고자 할때 응답된다.
    - 갑작스러운 트래픽 급증으로 서버가 과부하되거나 특정 시간대에 서버 패치 및 업데이트 등 다양한 작업을 수행하기 위해
    서버 다운을 시켰거나, 서버가 재부팅되거나 방화벽 설정에 잘못된 구성이 있거나 등 다양한 원인으로 일어나는 것이다.
    서버가 언제 그 리소스를 사용할 수 있게 될지 알고 있다면, 서버는 Retry-after 헤더를 응답에 포함시켜 언제 그 리소스를
    사용할 수 있는지에 대한 안내를 클라이언트에게 해줄 수 있다. 502 코드와 달리 웹 크롤러의 검색 순위에 영향을 미치지 않는다.

    504 - Gateway Timeout - 게이트웨이 시간 초과로 서버에서 요청을 처리하지 아니하고 연결을 닫음.
    - 클라이언트가 서버에게 요청을 할 때 서버에서 갑작스러운 트래픽 급증으로 서버가 과부하 되어 일시적인 문제로 인해
    리소스를 일시적으로 처리할 수 없다. 일정시간 뒤에 요청하라고 안내하는 것과 같다.

    505 - Http Version Not Supported - 서버에서 지원되지 않는 http 버전이라 처리 불가
    - http 버전으로 HTTP/1.0, HTTP/1.1, HTTP/2, HTTP/3 이 있다.
    - 이중 서버에서 지원하지 않는 버전의 프로토콜로 된 요청을 받았을 떄 응답된다.

    506 - Variant Also Negotiates - 콘텐츠 협상과 관련있는 상태코드, 실험적인 프로토콜이며 공식적으로 표준으로 채택하지 않은 응답 코드
    507 - Insufficient Storage - 스토리지 공간 부족, 서버에 HTTP 요청을 수용할 충분한 공간이 없음을 나타내는 응답코드입니다.
    508 - Loop Detected - 무한 루프를 감지, 서버가 요청을 처리하는 동안 무한 루프를 감지한 경우 요청을 종료한다.
    510 - Not Extended - 추가 확장이 필요함, 실험적인 프로토콜이며 공식적으로 표준으로 채택하지 않은 응답 코드
    511 - Network Authentication Required - 네트워크 인증 요구
    - 클라이언트가 네트워크 액세스를 얻으려면 인증이 필요하다는 것을 나타낸다. 보통 네트워크에 엑세스 할 때 로그인이 필요한 경우를 들 수 있다.
    - 인터넷 포털에서 WIFI 네트워크에 연결한 후 일종의 로그인을 거쳐야 함을 컴퓨터에 알리는데 사용된다.
    이 응답 코드는 원 서버에서 생성되는 것이 아니라, 네트워크에 대한 액세스를 제어하는 프록시 서버에서 생성된다.

    599 - Network Connect Timeout Error - 네트워크 연결 시간 초과 오류
    - 일부 프록시에서 사용하는 비공식 http 상태코드
    - 로컬 네트워크를 찾을 수 없거나 로컬 네트워크에 대한 HTTP 연결 시간이 초과되어 코드에서 실행한 HTTP 요청이 실패했음을 나타낼 수 있다.

    출처: https://sanghaklee.tistory.com/61 이상학의 개발블로그,
     https://inpa.tistory.com/entry/HTTP-%F0%9F%8C%90-5XX-Server-Error-%EC%83%81%ED%83%9C-%EC%BD%94%EB%93%9C-%EC%A0%9C%EB%8C%80%EB%A1%9C-%EC%95%8C%EC%95%84%EB%B3%B4%EA%B8%B0 Inpa Dev 블로그
        ,최용준(본인)개발자 예정자의 네트워크 지식

    이 개념을 바탕으로 코드로 구현해보자.
--------------------------------------------------------------
 */

    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(402,"Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method not Allowed"),
    CONFLICT(409, "Conflict"),
    TOO_MANY_REQUESTS(429, "Too many Requests"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    NOT_IMPLEMENTED(501, "Not Implemented"),
    BAD_GATEWAY(502, "Bad Gateway"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),
    GATEWAY_TIMEOUT(504,"Gateway Timeout"),
    HTTP_VERSION_NOT_SUPPORTED(505, "Http Version Not Supported"),
    VARIANT_ALSO_NEGOTIATES(506, "Variant Also Negotiates"),
    INSUFFICIENT_STORAGE(507, "Insufficient Storage"),
    LOOP_DETECTED(508, "Loop Detected"),
    NOT_EXTENDED(510, "Not Extended"),
    NETWORK_AUTHENTICATION_REQUIRED(511, "Network Authentication Required"),
    NETWORK_CONNECT_TIMEOUT_ERROR(599, "Network Connect Timeout Error");

    //클라이언트가 웹 사이트(서버)에 요청을 했을 때 잘 전달이 됐는지 응답을 해줘야 합니다.
    //code, message 를 응답으로 반환하여 사용자에게 알려준다.
    @Getter
    private int code; //http 웹 사이트 상태를 숫자로 코드로 표시하여 알리기 위해서 사용합니다.

    @Getter
    private String message; //어떤 사유로 에러 코드를 전달 했는지에 대한 메시지를 사용자에게 알려주기 위해서 사용합니다.

    //25-02-11 김용준 고객님 - Q3) 아래 생성자를 보면 int code 로 매개변수를 가집니다. this.code = code 이렇게 설정하는 이유는?
    //25-02-12 강사님께 물어본 후, 최용준 개발자님 -답변) 생성자입니다. 열거형(enum)의 각 항목이 BAD_REQUEST(400, "Bad Request") 처럼 생성될 때 초기값을 설정하는 역할을 합니다.
    ExceptionCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}

/*
추후 찾은 내용: 2025-02-12 03:16 PM 최용준 개발자.
이런식으로 구현되어 있는게 존재한다..

 public enum HttpStatus{
 CONTINUE(100, HttpStatus.Series.INFORMATIONAL, "Continue"),
    SWITCHING_PROTOCOLS(101, HttpStatus.Series.INFORMATIONAL, "Switching Protocols"),
    PROCESSING(102, HttpStatus.Series.INFORMATIONAL, "Processing"),
    CHECKPOINT(103, HttpStatus.Series.INFORMATIONAL, "Checkpoint"),
    OK(200, HttpStatus.Series.SUCCESSFUL, "OK"),
    CREATED(201, HttpStatus.Series.SUCCESSFUL, "Created"),
 }


 */

