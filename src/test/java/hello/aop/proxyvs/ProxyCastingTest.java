package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;

@Slf4j
public class ProxyCastingTest {
    @Test
    void jdkProxy(){
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(false); // JDK 동적 프록시

        // 프록시를 인터페이스로 캐스팅 해보자
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();
        log.info("proxy.hello= {}", memberServiceProxy.hello("helloA"));

        // MemberService ->  MemberServiceImpl 타입 캐스팅 될까
        // result: 오류 <- 당연하다, MemberService는 MemberServiceImpl자체를 모른다(.ClassCastException)
//        MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy;
        Assertions.assertThatThrownBy(()-> {
            MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy;})
                .isInstanceOf(ClassCastException.class);

    }

    @Test
    void cglibProxy(){
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true); // JDK 동적 프록시

        // 프록시를 인터페이스로 캐스팅 해보자
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();
        log.info("proxy class={}", memberServiceProxy.getClass());

        // CGLIB 프록시를 구현 클래스로 캐스팅 시도
        MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy;
    }
}
