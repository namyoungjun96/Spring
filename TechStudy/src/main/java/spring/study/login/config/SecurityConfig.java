package spring.study.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
//securedEnalbe(secured 어노테이션 활성화), prePostEnabled(preAuthorize 어노테이션 활성화, PostAuthorize 어노테이션 활성화) 
public class SecurityConfig {
	//	@Bean
	//    public WebSecurityCustomizer webSecurityCustomizer() {
	//            return (web) -> web.ignoring()
	//            // Spring Security should completely ignore URLs starting with /resources/
	//                            .antMatchers("/resources/**");
	//    }

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/user/**").authenticated()
		.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
		.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
		.anyRequest().permitAll()
		.and()
		.formLogin()
		.loginPage("/loginForm")
		.loginProcessingUrl("/login")				// 매핑된 주소를 입력하면, 시큐리티가 대신 로그인을 진행해준다.
		.defaultSuccessUrl("/");					// 기본적인 성공했을때 이동하는 페이지
		return http.build();
	}
	
	// 해당 메서드의 리턴되는 오브젝트를 IOC로 등록해준다.
	@Bean
	public BCryptPasswordEncoder encodedPwd() {
		return new BCryptPasswordEncoder();
	}
}
