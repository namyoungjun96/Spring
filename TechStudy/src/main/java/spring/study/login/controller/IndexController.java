package spring.study.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import spring.study.login.config.auth.PrincipalDetails;
import spring.study.login.dao.UserDaoJPA;
import spring.study.login.domain.User;

@Controller
public class IndexController {
	@Autowired UserDaoJPA userDao;
	@Autowired BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/test/login")
	public @ResponseBody String testLogin(Authentication authentication,
			@AuthenticationPrincipal PrincipalDetails userDetails) {			// Principal 객체를 의존성 주입하는 어노테이션
		System.out.println("/test/login ================================");
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		System.out.println(authentication.getPrincipal().getClass());
		System.out.println("authentication : " + principal.getUser());
		System.out.println("userDetails : " + userDetails.getUser());
		
		return "세션 정보 확인하기";
	}
	// 구글일때는 안된다. DefaultOAuth2User로 캐스팅이 안된다고 나와있음.
	
	@GetMapping("/test/oauth/login")
	public @ResponseBody String testOauthLogin(Authentication authentication,
			@AuthenticationPrincipal OAuth2User oauth) {			// Principal 객체를 의존성 주입하는 어노테이션
		// security 쪽에서 인증하는 정보는 다 받아주는듯?
		System.out.println("/test/oauth/login ================================");
		OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
		System.out.println(authentication.getPrincipal().getClass());
		System.out.println("authentication : " + oauth2User.getAttributes());
		System.out.println("oauth2User : " + oauth.getAttributes());
		
		return "세션 정보 확인하기";
	}
	// 구글일때 됨 PrincipalOauth2UserService에서 laodUser.getAttributes한 정보임.
	
	// localhost:8080/
	// localhost:8080
	@GetMapping({"", "/"})
	public String index() {
		// mustache 기본 폴더 src/main/resources/
		// view resolver는 mustache에서 기본으로 templates(prefix), mustache(suffix)로 생략 가능
		return "index";
	}
	
	// OAuth 로그인을 해도 PrincipalDetails
	// 일반 로그인을 해도 PrincipalDetails 
	@GetMapping("/user")
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		System.out.println("principalDetails : " + principalDetails.getUser());
		return "user";
	}
	
	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}
	
	@GetMapping("/manager")
	public String manager() {
		return "manager";
	}
	
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	
	@PostMapping("/join")
	public String join(User user) {
		user.setRole("ROLE_USER");
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		userDao.save(user);
		return "redirect:/loginForm";
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}
	
	@PreAuthorize("hashRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")	// 여러개의 권한을 걸고싶으면 PreAuthorize
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "데이터정보";
	}
	// Pre는 메서드가 시작하기 전, Post는 메서드가 종료되고 나서
}
