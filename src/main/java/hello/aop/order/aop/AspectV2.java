package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class AspectV2 {
    // pointcut signature
    // hello.aop.order 패키지와 하위 패키지sj
    @Pointcut("execution (* hello.aop.order..*(..))")
    private void allOrder(){}


    @Around("allOrder()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable{
        log.info("[log] {}", joinPoint.getSignature()); // join point 시그니쳐

        return joinPoint.proceed();
    }

    // 분리한 포인트컷을 가져다 쓰기 편하게 사용가능
//    @Around("allOrder()")
//    public Object doLog2(ProceedingJoinPoint joinPoint) throws Throwable{
//        log.info("[log] {}", joinPoint.getSignature()); // join point 시그니쳐
//
//        return joinPoint.proceed();
//    }
}
