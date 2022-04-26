package hello.aop.pointcut;

import hello.aop.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
//@SpringBootTest // 기본으로 CGLIB로 다 만들어버린다 세팅을 바꿔주자
@SpringBootTest(properties = "spring.aop.proxy-target-class=false")
@Import(ThisTargetTest.ThisTargetAspect.class)
public class ThisTargetTest {
    @Autowired
    MemberService memberService;

    @Test
    void success(){
        log.info("memberService Proxy = {}", memberService.getClass());
        memberService.hello("helloA");
    }

    @Slf4j
    @Aspect
    static class ThisTargetAspect{

        // 부모 타입 허용하는 this
        @Around("this(hello.aop.member.MemberService)")
        public Object doThisInterface(ProceedingJoinPoint joinPoint) throws Throwable{
            log.info("[this-interface] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        // 부모 타입 허용하는 target
        @Around("target(hello.aop.member.MemberService)")
        public Object doTargetInterface(ProceedingJoinPoint joinPoint) throws Throwable{
            log.info("[target-interface] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        // 부모 타입 허용하는 this
        @Around("this(hello.aop.member.MemberServiceImpl)")
        public Object doThis(ProceedingJoinPoint joinPoint) throws Throwable{
            log.info("[this-impl] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        // 부모 타입 허용하는 target
        @Around("target(hello.aop.member.MemberServiceImpl)")
        public Object doTarget(ProceedingJoinPoint joinPoint) throws Throwable{
            log.info("[target-impl] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }
}
