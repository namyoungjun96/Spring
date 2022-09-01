package com.jun.jwt.config.jwt;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jun.jwt.auth.CustomBCryptPasswordENcoder;
import com.jun.jwt.auth.PrincipalDetails;
import com.jun.jwt.domain.User;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음.
// /login 요청해서 username, password 전송하면 (post)
// UsernamePasswordAuthenticationFilter 동작을 함.
// formlogin을 disable하면 작동하지않는다.
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	@Autowired CustomBCryptPasswordENcoder bCryptPasswordEncoder;
	
	private AuthenticationManager authenticationManager;
	
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	// /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		// TODO Auto-generated method stub
		System.out.println("JwtAuthenticationFilter : 로그인 시도중");
		
		try {
//			System.out.println(request.getInputStream().toString());
//			BufferedReader br = request.getReader();
//			
//			String input = null;
//			while((input = br.readLine()) != null) {
//				System.out.println(input);
//			}							// 혹시 값을 바디에 안넣었는지 잘 보자.. 와 ㅈ같다 ㅎㅎ,,
			ObjectMapper om = new ObjectMapper();
			User user = om.readValue(request.getInputStream(), User.class);
			System.out.println("로그인 완료: " + user.getUsername());
			
			UsernamePasswordAuthenticationToken authenticationToken = 
					new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
			
			// PrincipalDetailsService의 loadUserByUsername() 함수가 실행된 후 정상이면 authentication이 리턴됨.
			// DB에 있는 username과 password가 일치한다. == 인증이 됐다.
			Authentication authentication = 
					authenticationManager.authenticate(authenticationToken);
			
			// authentication 객체가 session영역에 저장됨. => 로그인이 됐다는 뜻.
			PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
			System.out.println(principalDetails.getUser().getUsername());
			// PrincipalDetails의 값이 있다는 건 로그인이 정상적으로 됐다는 뜻
			
			// authentication 객체가 session 영역에 저장해야 하고 그 방법이 return.
			// 리턴의 이유는 권한 관리를 security가 대신 해주기 때문에 편하려고 하는거임.
			// 굳이 JWT토큰을 사용하면서 세션을 만들 이유가 없음. 근데 단지 권한 처리때문에 session을 넣어 줍니다.
			return authentication;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 1. username, password를 받아서
		// 2. 정상인지 록읜 시도를 해보는 것 authenticationManager로 로그인 시도를 하면!! PrincipalDetailsService가 호출이 된다.
		// 3. PrincipalDetails를 세션에 담고 - 담는 이유: 안 담으면 권한 관리가 안됨!!
		// 4. JWT토큰을 만들어서 응답해주면 됨.
		
		return null;
	}
	
	// 실행되는 순서: attemptAuthentication실행 후 인증이 정상적으로 되었으면 successfulAuthentication가 실행된다.
	// JWT 토큰을 만들어서 request요청한 사용자에게 JWT토큰을 response해주면 됨.
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		// TODO Auto-generated method stub
		System.out.println("successfulAuthentication 실행됨 : 인증 완료라는 뜻 ^^ ");
		PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
		
		// RSA방식이 아니고 Hash암호방식임
		String jwtToken = JWT.create()
				.withSubject("cos토큰")
				.withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 10)))
				.withClaim("id", principalDetails.getUser().getId())
				.withClaim("username", principalDetails.getUser().getUsername())
				.sign(Algorithm.HMAC512("cos"));
		
		response.addHeader("Authorization", "Bearer "+jwtToken);
	}
}
