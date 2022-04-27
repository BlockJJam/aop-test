package hello.aop.internalcall;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV1 {

    // 스프링 컨테이너에 프록시 빈이 들어온다
    private CallServiceV1 callServiceV1; // 생성자 쓰면 안된다 -> 순환참조

    // 밑에 log.info를 통해 애플리케이션 로드 시점에 해당 setter를 통해 프록시 객체를 주입하는 것을 확인 가능
    // callServiceV1 setter=class hello.aop.internalcall.CallServiceV1$$EnhancerBySpringCGLIB$$69d23b48 (로그)
    @Autowired
    public void setCallServiceV1(CallServiceV1 callServiceV1){
        log.info("callServiceV1 setter={}", callServiceV1.getClass());
        this.callServiceV1 = callServiceV1;
    }

    public void external(){
        log.info("call external");
        callServiceV1.internal(); // -> this.internal()을 의미
    }

    public void internal(){
        log.info("call internal");
    }
}
