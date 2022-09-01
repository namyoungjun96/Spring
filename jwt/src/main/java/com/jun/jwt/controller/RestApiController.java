package com.jun.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jun.jwt.auth.CustomBCryptPasswordENcoder;
import com.jun.jwt.auth.PrincipalDetails;
import com.jun.jwt.dao.UserDaoJPA;
import com.jun.jwt.domain.User;

@RestController
public class RestApiController {
	@Autowired CustomBCryptPasswordENcoder bCryptPasswordEncoder;
	@Autowired UserDaoJPA userDao;
	
	@GetMapping("home")
	public String home() {
		System.out.println("이건 홈");
		return "<h1>home</h1>";
	}
	
	@PostMapping
	public String token() {
		return "<h1>home</h1>";
	}
	
	@PostMapping("join")
	public String join(@RequestBody User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles("ROLE_USER");
		userDao.save(user);
		
		return "회원가입완료";
	}
	
	@GetMapping("/api/v1/user")
	public String user(Authentication authentication) {
		PrincipalDetails prinDetails = (PrincipalDetails) authentication.getPrincipal();
		System.out.println(prinDetails.getUser().getRoles());
		return "user";
	}
	
	@GetMapping("/api/v1/manager")
	public String manager() {
		return "manager";
	}
	
	@GetMapping("/api/v1/admin")
	public String admin() {
		return "admin";
	}
}
