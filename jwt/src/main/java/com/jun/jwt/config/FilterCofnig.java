package com.jun.jwt.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jun.jwt.filter.MyFilter1;
import com.jun.jwt.filter.MyFilter2;

@Configuration
public class FilterCofnig {
	
	@Bean
	public FilterRegistrationBean<MyFilter1> filter1(){
		FilterRegistrationBean<MyFilter1> bean = new FilterRegistrationBean<>(new MyFilter1());
		bean.addUrlPatterns("/*");
		bean.setOrder(0);	// 낮은 번호가 필터중에서 가장 먼저 실행됨.
		return bean;
	}
	
	@Bean
	public FilterRegistrationBean<MyFilter2> filter2(){
		FilterRegistrationBean<MyFilter2> bean = new FilterRegistrationBean<>(new MyFilter2());
		bean.addUrlPatterns("/*");
		bean.setOrder(1);	// 낮은 번호가 필터중에서 가장 먼저 실행됨.
		return bean;
	}	// request요청이 올 때, 동작한다.
	// securityChainFilter가 다 동작하고나서 이 Filter가 동작한다.
}
