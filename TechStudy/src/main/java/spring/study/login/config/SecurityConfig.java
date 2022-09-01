package spring.study.login.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import spring.study.login.config.oauth.PrincipalOauth2UserService;

// 1. 코드받기(인증), 2. 액세스토큰(권한),
// 3. 사용자 프로필 정보를 가져오고, 4-1. 그 정보를 토대로 회원가입을 자동으로 진행시키기도 함.
// 4-2 (이메일, 전화번호, 이름, 아이디) 쇼핑몰 -> (집주소) 백화점몰 -> (vip, 일반) 

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
//securedEnalbe(secured 어노테이션 활성화), prePostEnabled(preAuthorize 어노테이션 활성화, PostAuthorize 어노테이션 활성화) 
public class SecurityConfig {
	@Autowired private PrincipalOauth2UserService principalDetailsService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/user/**").authenticated()
			.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
			.anyRequest().permitAll()
		.and()
			.formLogin()
			.loginPage("/loginForm")
			.loginProcessingUrl("/login")		// 매핑된 주소를 입력하면, 시큐리티가 대신 로그인을 진행해준다.
			.defaultSuccessUrl("/")				// 기본적인 성공했을때 이동하는 페이지
		.and()
			.oauth2Login()
			.loginPage("/loginForm")			// 구글 로그인이 완료된 뒤의 후처리가 필요하다. Tip. 코드X, (액세스 토큰 + 사용자 프로필 정보O)
			.userInfoEndpoint()
			.userService(principalDetailsService);
		return http.build();
	}
	
//	@Bean
	//    public WebSecurityCustomizer webSecurityCustomizer() {
	//            return (web) -> web.ignoring()
	//            // Spring Security should completely ignore URLs starting with /resources/
	//                            .antMatchers("/resources/**");
	//    }
}
