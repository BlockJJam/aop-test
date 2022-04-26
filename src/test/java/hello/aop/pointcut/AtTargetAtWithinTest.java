package hello.aop.pointcut;

import hello.aop.member.annotation.ClassAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Slf4j
@Import({AtTargetAtWithinTest.Config.class})
@SpringBootTest
public class AtTargetAtWithinTest {
    @Autowired
    Child child;

    @Test
    void success(){
        log.info("child Proxy = {}", child.getClass());
        child.childMethod();
        child.parentMethod();
    }

    static class Config{

        @Bean
        public Parent parent(){
            return new Parent();
        }

        @Bean
        public Child child(){
            return new Child();
        }

        @Bean
        public AtTargetAtWithinAspect atTargetAtWithinAspect(){
            return new AtTargetAtWithinAspect();
        }
    }

    static class Parent{
        public void parentMethod(){}
    }

    @ClassAop
    static class Child extends Parent{
        public void childMethod(){}
    }

    @Slf4j
    @Aspect
    static class AtTargetAtWithinAspect{

        // 동적으로 스프링 컨테이너를 돌려야 하기 때문에 @Aspect로 런타임에서 확인해봐야 한다
        // @target: 인스턴스 기준으로 모든 메섣의 조인 포인트를 선정, 부모 타입의 메서드도 적용
        @Around("execution(* hello.aop..*(..)) && @target(hello.aop.member.annotation.ClassAop)")
        public Object atTarget(ProceedingJoinPoint joinPoint) throws Throwable{
            log.info("[@target] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        // @within: 선택된 클래스 내부에 있는 메서드만 조인 포인트로 선정, 부모 타입의 메서드는 적용되지 않음
        @Around("execution(* hello.aop..*(..)) && @within(hello.aop.member.annotation.ClassAop)")
        public Object atWitihin(ProceedingJoinPoint joinPoint) throws Throwable{
            log.info("[@within] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }
}
