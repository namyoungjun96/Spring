package com.jun.jwt.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jun.jwt.auth.PrincipalDetails;
import com.jun.jwt.dao.UserDaoJPA;
import com.jun.jwt.domain.User;

// security가 filter를 가지고 있는데 그 필터중에 BasicAuthenticationFilter라는게 있다.
// 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게 되있다.
// 만약에 권한이나 인증이 필요한 주소가 아니라면 이 필터를 타지않는다.
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
	private UserDaoJPA userDao;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserDaoJPA userDao) {
		super(authenticationManager);
		this.userDao = userDao;
	}
	
	// 인증이나 권한이 필요한 주소요청이 있을 때 해당 필터를 타게 됨
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
//		super.doFilterInternal(request, response, chain);	응답 두번해서 request가 두번함. 그럼 죽는듯..
		System.out.println("인증이나 권한이 필요한 주소 요청이 됨.");
		
		String jwtHeader = request.getHeader("Authorization");
		System.out.println("jwtHeader : " + jwtHeader);
		
		// header가 있는지 확인
		if(jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
			chain.doFilter(request, response);
			return;
		}
		
		// JWT 토큰을 검증을 해서 정상적인 사용자인지 확인
		String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");
		String username = 
				JWT.require(Algorithm.HMAC512("cos")).build().verify(jwtToken).getClaim("username").asString();
		
		// 서명이 정상적으로 됨
		if(username != null) {
			User userEntity = userDao.findByUsername(username);
			PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
			
			// JWT 토큰 서명이 정상이면 Authentication 객체를 만들어준다.
			Authentication authentication = 
					new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
			
			// 강제로 시큐리티의 세션에 접근하여(SecurityContextHolder) authentication객체를 저장.
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			chain.doFilter(request, response);
		}
	}
}
