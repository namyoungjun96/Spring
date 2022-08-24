package spring.study.login.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import spring.study.login.domain.User;

// 이 메소드가 하는 일 : 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
// 로그인 진행이 완료가 되면 시큐리티 session을 만들어준다. (Security ContextHolder)라는 키값에 담아 세션정보를 저장시킨다.
// 오브젝트 => Authentication 타입 객체
// Authentication 안에 User 정보가 있어야 됨.
// User Object type => UserDetails type Object 여야 함.

// Security Session 에 세션 정보를 저장한다. 
// Security Session 안에는 Authentication Object 만 들어갈 수 있다.
// Authentication Object 안에는 UserDetails type Object 만 들어갈 수 있다.
// ** 프레임워크에서 정해놓은 방식임.

public class PrincipalDetails implements UserDetails {
	
	private User user;
	
	public PrincipalDetails(User user) {
		this.user = user;
	}

	// 해당 User의 권한을 return하는 곳!!
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Collection을 만드는 이유 = return type이 Collection이라서 (GrantedAuthority type의 Collection이 필요)
		// ArrayList를 통해서 필요한 return type을 정의한다. (User의 권한은 user.getRole()로 리턴할 수 있지만,
		// 리턴 타입이 콜렉션이기때문에 그에 맞게 매핑해주는 작업이다.
		Collection<GrantedAuthority> collect = new ArrayList<GrantedAuthority>();
		collect.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				// TODO Auto-generated method stub
				return user.getRole();
			}
		});
		return collect;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getUsername();
	}

	// 계정이 만료된지 확인하는 boolean ( true면 아니란 뜻 ? )
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	// 계정이 잠겼는지 확인하는
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	
	// 계정의 발급 만료기간 묻는듯
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	// 활성화된 계정이냐는 뜻
	@Override
	public boolean isEnabled() {
		// 예시 : 우리 사이트에서 1년동안 로그인하지 않으면 휴면유저로 등록이 된다면.
		// user를 통해서 logic을 만들어주면 됨.
		// example 현재시간 - 로그인시간 => 1년 초과하면 return false;
		// 이런식으로 다 logic을 관리할 수 있다.
		
		return true;
	}

}
