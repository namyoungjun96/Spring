package spring.study.login.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import spring.study.login.dao.UserDaoJPA;
import spring.study.login.domain.User;

// 시큐리티 설정에서 loginProcessingUrl("/login");
// /login 요청이 오면 자동으로 UserDetailService 타입으로 IOC되어 있는 loadUserByUsername 함수가 실행한다.
// 프레임워크에서 설정한것.. 그냥 규칙이라네
// 로그인 페이지에서 만든 로그인 폼의 input으로 받는 username인자는 loadUserByUsername의 매개변수로 사용함.
// 바꾸고싶은 경우는, securityContext파일부터 건드려야한다. (안 바꾸는게 좋은듯 굳이.)
// 로그인 하는 폼의 경우에도 아이디를 받을때 username을 id로 쓰는게 좋을듯.
@Service
public class PrincipalDetailsService implements UserDetailsService {
	@Autowired private UserDaoJPA userDao;

	// 시큐리티 session에는 Authentication이 들어가야하고 , Authentication는 UserDetails가 들어가야함.
	// 모양새가 Security Session(Authentication(UserDetails))
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findByUsername(username);
		if(user != null) {
			return new PrincipalDetails(user);
		}
		return null;
	}

}
