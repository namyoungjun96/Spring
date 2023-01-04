package springbook.learningtest.jdk.proxy;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.reflect.Proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

public class DynamicProxyTest {
	@Test
	public void simpleProxy() {
		Hello hello = new HelloTarget();
		assertThat(hello.sayHello("Toby"), equalTo("Hello Toby"));
		assertThat(hello.sayHi("Toby"), equalTo("Hi Toby"));
		assertThat(hello.sayThankYou("Toby"), equalTo("ThankYou Toby"));
		
		Hello proxiedHello = (Hello)Proxy.newProxyInstance(
				getClass().getClassLoader(),
				new Class[] { Hello.class },
				new UppercaseHandler(new HelloTarget()));
		
		// Hello proxiedHello = new HelloUppercase(hello);
		assertThat(proxiedHello.sayHello("Toby"), equalTo("HELLO TOBY"));
		assertThat(proxiedHello.sayHi("Toby"), equalTo("HI TOBY"));
		assertThat(proxiedHello.sayThankYou("Toby"), equalTo("THANKYOU TOBY"));
	}
	
	@Test
	public void proxyFactoryBean() {
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(new HelloTarget());
//		pfBean.addAdvice(new UppercaseAdvice());
		
		NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
		pointcut.setMappedName("sayH*");
		NameMatchMethodPointcut lowerPointcut = new NameMatchMethodPointcut();
		lowerPointcut.setMappedName("sayT*");
		
		pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
		pfBean.addAdvisor(new DefaultPointcutAdvisor(lowerPointcut, new LowercaseAdvice()));
		
		Hello proxiedHello = (Hello) pfBean.getObject();
		
		assertThat(proxiedHello.sayHello("Toby"), equalTo("HELLO TOBY"));
		assertThat(proxiedHello.sayHi("Toby"), equalTo("HI TOBY"));
		assertThat(proxiedHello.sayThankYou("Toby"), equalTo("thankyou toby"));
//		assertThat(proxiedHello.sayThankYou("Toby"), equalTo("THANKYOU TOBY"));
		// pointcut을 적용하면 mapping한 이름만 작동하기 때문에 T로 시작하는 메소드는 작동하지 않는다.
	}
	
	@Test
	public void classNamePointcutAdvisor() {
		// 포인트컷 준비
		NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut() {
			public ClassFilter getClassFilter() {
				return new ClassFilter() {
					public boolean matches(Class<?> clazz) {
						return clazz.getSimpleName().startsWith("HelloT");
					}
				};
			}
		};
		classMethodPointcut.setMappedName("sayH*");
		
		// 테스트
		checkAdviced(new HelloTarget(), classMethodPointcut, true);
		
		class HelloWorld extends HelloTarget {};
		checkAdviced(new HelloWorld(), classMethodPointcut, false);
		
		class HelloToby extends HelloTarget {};
		checkAdviced(new HelloToby(), classMethodPointcut, true);
	}
	
	private void checkAdviced(Object target, Pointcut pointcut, boolean adviced) {
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(target);
		pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
		Hello proixedHello = (Hello) pfBean.getObject();
		
		if(adviced) {
			assertThat(proixedHello.sayHello("Toby"), equalTo("HELLO TOBY"));
			assertThat(proixedHello.sayHi("Toby"), equalTo("HI TOBY"));
			assertThat(proixedHello.sayThankYou("Toby"), equalTo("ThankYou Toby"));
		} 
		else {
			assertThat(proixedHello.sayHello("Toby"), equalTo("Hello Toby"));
			assertThat(proixedHello.sayHi("Toby"), equalTo("Hi Toby"));
			assertThat(proixedHello.sayThankYou("Toby"), equalTo("ThankYou Toby"));
		}
	}
	
	static class UppercaseAdvice implements MethodInterceptor {
		public Object invoke(MethodInvocation invocation) throws Throwable {
			String ret = (String)invocation.proceed();
			return ret.toUpperCase();
		}
	}
	
	static class LowercaseAdvice implements MethodInterceptor {
		public Object invoke(MethodInvocation invocation) throws Throwable {
			String ret = (String)invocation.proceed();
			return ret.toLowerCase();
		}
	}
}
