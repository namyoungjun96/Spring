package spring.study.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import spring.study.login.dao.UserDaoJPA;
import spring.study.login.domain.User;

@Controller
public class IndexController {
	@Autowired UserDaoJPA userDao;
	@Autowired BCryptPasswordEncoder bCryptPasswordEncoder;
	
	// localhost:8080/
	// localhost:8080
	@GetMapping({"", "/"})
	public String index() {
		// mustache 기본 폴더 src/main/resources/
		// view resolver는 mustache에서 기본으로 templates(prefix), mustache(suffix)로 생략 가능
		return "index";
	}
	
	@GetMapping("/user")
	public  String user() {
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
		System.out.println(user.getUsername());
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
