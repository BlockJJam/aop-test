package hello.aop.exam.aop;

import hello.aop.exam.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class RetryAspect {
    // Around를 이용하는 이유, 원하는 타이밍에 joinPoint.proceed()로 메서드 호출해줘야 해서
//    @Around("@annotation(hello.aop.exam.annotation.Retry)")
    @Around("@annotation(retry)") // 매개변수로 받으면 이름을 같게 해서 깔끔하게 표현식을 고칠 수 있다
    public Object doRetry(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable{
        log.info("[retry] {} retry={}", joinPoint.getSignature(), retry);

        int maxRetry = retry.value();
        // exception을 담기 위한 용도
        Exception exceptionHolder = null;

        for(int retryCnt = 1; retryCnt <= maxRetry; retryCnt++){
            try{
                log.info("[retry] try count = {}/{}", retryCnt, maxRetry);
                return joinPoint.proceed();
            }catch(Exception e){
                exceptionHolder = e;
            }
        }
        throw exceptionHolder;
    }
}
