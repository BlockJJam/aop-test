# Aspect에 대한 소개

---

### 핵심 기능과 부가 기능을 분리

- 부가 기능을 떨어트려서 한 곳에서 관리하고 부가 기능을 어디에 적용할 지 선택하는 기능도 만들었다
- 부가 기능 + 적용 기준 선정 = 하나의 모듈로 생성 ← 에스팩트
- 우리가 사용했던 `@Aspect`가 정확히 이 기능이었다

### 에스팩트

- 관점이라는 의미로, 애플리케이션을 바라보는 관점을 하나하나의 기능에서 횡단 관심사로 바라는 것
- **관점 지향 프로그래밍**(`AOP`): 에스팩트를 사용한 프로그래밍 자체를 관점 지향 프로그래밍이라 한다
- OOP를 대체하기 위함이 아닌, **OOP의 부족한 부분을 보조하는 목적**으로 개발되었다
- `LogTraceAspect → { OrderController, OrderService, OrderRepository }` (부가 기능이 핵심 기능을 보좌하는 형태)
- `AOP`로 횡단 관심사로 분리된 부가 기능들을 처리해준다

### AspectJ란,

- `AOP` 개념을 대표적으로 구현한 것
- 스프링도 `AOP`를 지원하지만, `AspectJ`의 문법을 차용했고 `AspectJ`의 일부 기능만 제공
- AspectJ 프레임워크 스스로의 설명
    - 자바 언어에 완벽한 관점 지향 확장
    - 횡단 관심사의 깔끔한 모듈화
        1. 오류 검사 및 처리
        2. 동기화
        3. 성능 최적화(캐싱)
        4. 모니터링 및 로깅

# AOP 적용 방식

---

### AOP 핵심기능과 부가기능을 어떻게 분리되어서 관리할까

- AOP를 사용할 때 부가기능 로직은 어떤 방식으로 실제 로직에 추가될까? ← 3가지 방법 존재
    1. **컴파일 시점**
    2. **클래스 로딩**
    3. **런타임 시점(프록시)** ← 이것을 테스트했었음

### 컴파일 시점에 자바 코드를 뜯어서 붙여버리는 방식

- `.java` 소스코드를 컴파일 → `.class` 파일로 만드는 시점에 부가 기능 로직을 추가시키는 방식

    ```java
    주문 로직 --[AspectJ Compiler]--> 주문 로직(+로그 추적 로직)
    ```

    - `AspectJ`를 제공하는 특별한 컴파일러를 사용해야 함
    - 컴파일 할 때, 핵심기능이 있는 컴파일된 코드 주변에 부가 기능이 붙어 버리는 것으로 생각하면 됨
    - **`AspectJ` 컴파일러**가 **`Aspect`를 확인해서 해당 클래스가 적용 대상인지 확인 → 적용 대상인 경우 부가기능 적용**
    - **위빙**(weaving) = 원본 로직에 부가 기능 부착하는 것
- **단점**
    - 잘 사용하지 않는 이유: 특별한 컴파일러가 있어야 하고, 설정도 복잡

 
### 클래스 로딩 시점에 JVM 내부 클래스 로더에 보관할 때 붙여버리는 방식

- 자바를 실행하면 자바 언어는 `.class` 파일을 JVM 내부 클래스 로더에 보관 → 중간에 `.class` 파일을 조작
    - java intrumentation 검색해서 사용방법을 확인해보면 된다
    - 위 내용이 **모니터링 툴들**이 이 방식을 사용
    - 로드 타임 위빙
- **단점**
    - 로드 타임 위빙 실행을 위해 `java -javaagent` 옵션을 통해 클래스 로더 조작기를 핸들링 ← 번거로우면서 운영도 어려움
    - 얘도 잘 사용안함


### 우리가 했던, 런타임 시점에 컴파일, 로드 다 끝나고 부가기능을 적용하는 방식

- 스프링 컨테이너의 도움을 받고, **프록시**와 **DI**, **빈 포스트 프로세서** 등의 개념을 총 동원 → 프록시를 통해 스프링 빈에 부가기능 적용
- 프록시와 같이 번잡한 과정을 거쳐야 하고, AOP 기능에도 일부 제약이 있음
    - **AOP 기능 제약**: 프록시를 통해 구현체를 불러내려면, 상속 때문에 final도 안되고, 생성자 이런 것들로 AOP 적용이 안됨
- 특별한 컴파일러를 준비해야 하거나, 복잡한 옵션 + 클래스 로더 조작기 설정등은 할 필요가 없음
    - 스프링만 있으면 얼마든지 AOP를 적용가능

### 위 3가지 방식의 부가 기능 적용의 차이

- **컴파일 시점**: 실제 대상 코드에 에스팩트 부가 기능 호출 코드가 포함 ← Aspect 직접 사용 필요
- **클래스 로딩 시점**: 마찬가지로 실제 대상 코드에 에스팩트 부가 기능 호출 코드가 포함 ← AspectJ 직접 사용 필요
- **런타임 시점**: 실제 대상 코드는 그대로 유지, 프록시를 통한 부가 기능 적용. 프록시를 통해야만 부가기능 적용 가능 ← **스프링 AOP 방식**

### AOP 적용 위치

- AOP를 적용 가능한 지점(`join point`): 메서드 실행 위치 뿐만 아니라, 생성자, 필드 값 접근, static 메서드 접근
    - 컴파일 시점과 클래스 로딩 시점의 경우는, 모든 지점에 사용 가능  ← 바이트 코드를 조작하기 때문
- (잘 이해할 것) 스프링 AOP의 경우에는 메서드 실행 지점에서만 AOP를 적용 가능
    - 왜? 프록시가 **원본코드를 호출하려면 메서드를 호출해야 하기 때문**
    - 프록시는 메서드 오버라이딩 개념으로 동작 ← 생성자 or static 메서드 or 필드 값 접근에는 프록시 개념에 적용이 안됨
    - 스프링 AOP의 `join point`는 **메서드 실행으로 제한**된다
- 스프링 AOP는 스프링 컨테이너가 관리할 수 있는 “**스프링 빈에서만 AOP를 적용가능**" ← 한계

> 스프링 AOP는 프록시 방식의 AOP를 차용할 뿐, **AspectJ를 직접 사용하는 것이 아님**
>

> **중요**
AspectJ를 사용하면 앞서 설명한 “더 복잡하고, 더 다양한 기능"을 사용 가능하다. 그럼, **스프링 AOP보다는 AspectJ를 직접 사용해서 AOP를 적용**하는게 낫지 않을까??
- AspectJ를 사용하기 위해 공부를 엄청해야 하고, 자바 관련 설정(특별한 컴파일러, 자바 옵션..)등도 복잡. 반면, **스프링 AOP**는 **스프링만 있으면 AOP를 적용 가능**하고 스프링 AOP 기능만으로도 **실무에서 필요한 요구사항을 대부분 만족할만한 구현 가능**
>

# AOP 용어 정리

---

### AOP를 적용하면서 사용했던 용어들

- 조인 포인트(Join Point)
    - 어드바이스가 적용될 수 있는 위치
    - 추상적인 개념으로, AOP를 적용할 수 있는 모든 지점
    - 스프링 AOP는 프록시 방식으로 인해 메서드 실행 지점으로 조인포인트를 제한

- **포인트컷**(Pointcut)
    - 어드바이스가 적용될 위치를 필터링하는 기능
    - 스프링 AOP는 메서드 실행 지점만 포인트컷으로 선별 가능
- **타켓**(Target)
    - 어드바이스를 받는 객체, 실제 구현체
- **어드바이스**(Advice)
    - 부가 기능
    - 특정 조인 포인트에서 Aspect에 의해 취해지는 조치
    - Around(주변), Before(전), After(후)와 같은 다양한 종류 존재
- **에스팩트**(Aspect)
    - Advice + Pointcut
    - @Aspect 활용했던 것을 생각하면 됨
    - 여러 어드바이스와 포인트컷이 함께 존재
- **어드바이저**(Advisor)
    - 하나의 Advice + Pointcut
    - 스프링 AOP 한정 용어
- **위빙**(Weaving)
    - 행위 ← 포인트컷으로 결정한 타겟의 조인 포인트에 어드바이스를 적용하는 것
    - AOP 적용을 위해 에스팩트를 객체에 연결한 상태
        - 컴파일, 클래스 로드, 런타임 시점
- **AOP 프록시**
    - AOP 기능을 구현하기 위해 만든 프록시 객체, 스프링 AOP가 사용하는 객체 = JDK 동적 프록시, CGLIB 객체