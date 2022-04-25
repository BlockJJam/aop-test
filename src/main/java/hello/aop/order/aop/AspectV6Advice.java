package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Slf4j
@Aspect
public class AspectV6Advice {

    // hello.aop.order 패키지 하위 패키지이면서, 클래스 이름 패턴이 *Service인 것
//    @Around("hello.aop.order.aop.Pointcuts.allOrderAndService()")
//    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable{
//        try {
//            // @Before
//            log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
//            Object result = joinPoint.proceed();
//
//            // @AfterReturning
//            log.info("[트랜잭션 커밋] {}", joinPoint.getSignature());
//            return result;
//        }catch (Exception e){
//            // @AfterThrowing
//            log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
//            throw e;
//        }finally{
//            // @After
//            log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
//        }
//    }

    @Before("hello.aop.order.aop.Pointcuts.allOrderAndService()")
    public void doBefore(JoinPoint joinPoint){
        // @Before는 joinPoint.proceed() 실행 전에 자동으로 실행해주기 때문에 다음 로직에 대한 추가 작성이 필요없다
        log.info("[before] {}", joinPoint.getSignature());
    }

    @AfterReturning(value= "hello.aop.order.aop.Pointcuts.allOrderAndService()", returning="result")
    public void doReturn(JoinPoint joinPoint, Object result){
        // @Around에서는 return 값을 조작할 수 있지만, 여기서는 바꿀 순 없다
        log.info("[return]{} return={}", joinPoint.getSignature(), result);
    }

    // result의 Object -> String으로 변환해서 OrderRepository의 반환 값을 출력해보자
    // String의 상위타입인 Object에도 값은 들어간다
    // 근데 타입이 맞지 않으면? Integer처럼 -> 그럼 어드바이스 자체가 호출이 안됨
    @AfterReturning(value= "hello.aop.order.aop.Pointcuts.allOrder()", returning="result")
    public void doReturn2(JoinPoint joinPoint, String result){
        log.info("[return2]{} return=2{}", joinPoint.getSignature(), result);
    }

    @AfterThrowing(value = "hello.aop.order.aop.Pointcuts.allOrderAndService()", throwing = "ex")
    public void doThrowing(JoinPoint joinPoint,Exception ex){
        log.info("[ex] {} message={}", joinPoint.getSignature(), ex.getMessage());
    }

    @After(value="hello.aop.order.aop.Pointcuts.allOrderAndService()")
    public void doAfter(JoinPoint joinPoint){
        log.info("[after] {}",joinPoint.getSignature());
    }
}
