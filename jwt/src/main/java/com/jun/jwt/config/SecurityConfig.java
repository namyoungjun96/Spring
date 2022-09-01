package com.jun.jwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.jun.jwt.config.jwt.JwtAuthenticationFilter;
import com.jun.jwt.config.jwt.JwtAuthorizationFilter;
import com.jun.jwt.dao.UserDaoJPA;
import com.jun.jwt.filter.MyFilter3;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired private UserDaoJPA userDao;
	@Autowired private CorsConfig corsConfig;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http.addFilterBefore(new MyFilter3(), BasicAuthenticationFilter.class);
		// 어떤 Filter에 걸어야 할지 알아야되서 어느정도 구조를 파악하고 있어야 함.. Before이기때문에 저 필터보다 먼저.
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)	// Token방식을 쓰기때문에 세션은 비활성화
		.and()
		.formLogin().disable()	// token을 위해
		.httpBasic().disable()	// token 써서 ㅎㅎ
		.apply(new MyCustomDsl())	// AuthenticationManager을 던져야함. deprecated됐다.. ㅎㅎ
		.and()
		.authorizeRequests()
		.antMatchers("/api/v1/user/**")
		.access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
		.antMatchers("/api/v1/manager/**")
		.access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER')")
		.antMatchers("/api/v1/admin/**")
		.access("hasRole('ROLE_USER')")
		.anyRequest().permitAll();
		return http.build();
	}
	
	public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
		@Override
		public void configure(HttpSecurity http) throws Exception {
			AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
			http
				.addFilter(corsConfig.corsFilter())	// CrossOrigin(인증X), 시큐리티 필터에 등록 인증(O)
				.addFilter(new JwtAuthenticationFilter(authenticationManager))
				.addFilter(new JwtAuthorizationFilter(authenticationManager, userDao));
		}
	}
}
