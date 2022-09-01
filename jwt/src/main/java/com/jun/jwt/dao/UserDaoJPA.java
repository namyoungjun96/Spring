package com.jun.jwt.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jun.jwt.domain.User;

// CRUD 함수를 JPARepository가 들고 있음.
// Repository라는 어노테이션이 없어도 jap레파지토리 덖에 상관없음
public interface UserDaoJPA extends JpaRepository<User, Integer> {
	public User findByUsername(String username);			// Jpa Query methods
}
