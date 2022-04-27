package hello.aop.internalcall;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV2 {

//    private final ApplicationContext applicationContext; // 이 객체를 사용하는 것은 배보다 배꼽이 큰 격
    private final ObjectProvider<CallServiceV2> callServiceProvider;

    public CallServiceV2(ObjectProvider<CallServiceV2> callServiceProvider) {
        this.callServiceProvider = callServiceProvider;
    }

    public void external(){
        log.info("call external");
//        CallServiceV2 callServiceV2 = applicationContext.getBean(CallServiceV2.class);
        CallServiceV2 callServiceV2 = callServiceProvider.getObject(); // 이 시점에 컨테이너에서 해당 빈을 꺼내는 것(ApplicationContext보다 기능이 좀더 특화됨)
        callServiceV2.internal(); // -> this.internal()을 의미
    }

    public void internal(){
        log.info("call internal");
    }
}
