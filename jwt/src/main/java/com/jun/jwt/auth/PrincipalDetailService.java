package com.jun.jwt.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jun.jwt.dao.UserDaoJPA;
import com.jun.jwt.domain.User;

// http://localhost:8080/login
@Service
public class PrincipalDetailService implements UserDetailsService {
	@Autowired private UserDaoJPA userDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("PrincipalDetailService의 loadUserByUsername()");
		User userEntity = userDao.findByUsername(username);
		return new PrincipalDetails(userEntity);
	}

}
