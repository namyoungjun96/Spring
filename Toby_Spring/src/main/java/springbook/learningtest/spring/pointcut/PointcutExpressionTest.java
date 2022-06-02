package springbook.learningtest.spring.pointcut;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

public class PointcutExpressionTest {
	@Test
	public void methodSignaturePointcut() throws SecurityException, NoSuchMethodException {
		System.out.println(Target.class.getMethod("minus", int.class, int.class));
		
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
//		pointcut.setExpression("execution(public int springbook.learningtest.spring"
//				+ ".pointcut.Target.minus(int, int) throws java.lang.RuntimeException)");
//		pointcut.setExpression("execution(* minus(int, int))");
//		pointcut.setExpression("execution(* minus(..))");
		pointcut.setExpression("execution(* *(..))");
		// 가장 느슨하게 만들 때 리턴 타입, 파라미터, 메소드 이름에 상관없이 모든 메소드 조건을 다 허용하는 포인트컷 표현식
		
		// Target.minus()
		assertThat(pointcut.getClassFilter().matches(Target.class) &&
				pointcut.getMethodMatcher().matches(Target.class.getMethod("minus", int.class, int.class),
						null), equalTo(true));
		
		// Target.plus()
		assertThat(pointcut.getClassFilter().matches(Target.class) && 
				pointcut.getMethodMatcher().matches(Target.class.getMethod("plus", int.class, int.class),
						null), equalTo(false));
		
		// Bean.method()
		assertThat(pointcut.getClassFilter().matches(Bean.class) && 
				pointcut.getMethodMatcher().matches(Target.class.getMethod("method"), null), 
						equalTo(false));
	}
	
	@Test
	public void pointcut() throws Exception {
		targetClassPointcutMatches("execution(* *(..))", true, true, true, true, true, true);
	}
	
	public void targetClassPointcutMatches(String expression, boolean... expected) throws Exception {
		pointcutMatches(expression, expected[0], Target.class, "hello");
		pointcutMatches(expression, expected[1], Target.class, "hello", String.class);
		pointcutMatches(expression, expected[2], Target.class, "plus", int.class, int.class);
		pointcutMatches(expression, expected[3], Target.class, "minus", int.class, int.class);
		pointcutMatches(expression, expected[4], Target.class, "method");
		pointcutMatches(expression, expected[5], Bean.class, "method");
	}
	
	public void pointcutMatches(String expression, Boolean expected, Class<?> clazz,
			String methodName, Class<?>... args) throws Exception {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression(expression);
		
		assertThat(pointcut.getClassFilter().matches(clazz) && 
				pointcut.getMethodMatcher().matches(clazz.getMethod(methodName, args), null), 
				equalTo(expected));
	}
}
