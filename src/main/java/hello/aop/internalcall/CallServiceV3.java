package hello.aop.internalcall;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

/**
 * 구조를 분리한다
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CallServiceV3 {
    // 외부에서 작성된 internal()를 호출하는 방식으로
    private final InternalService internalService;

    public void external(){
        log.info("call external");
        internalService.internal();
    }
}
