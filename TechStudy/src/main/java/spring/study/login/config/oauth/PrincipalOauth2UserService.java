package spring.study.login.config.oauth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import spring.study.login.config.auth.CustomBCryptPasswordENcoder;
import spring.study.login.config.auth.PrincipalDetails;
import spring.study.login.config.oauth.provider.GoogleUserInfo;
import spring.study.login.config.oauth.provider.NaverUserInfo;
import spring.study.login.config.oauth.provider.OAuth2UserInfo;
import spring.study.login.dao.UserDaoJPA;
import spring.study.login.domain.User;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
	
	@Autowired CustomBCryptPasswordENcoder bCryptPasswordEncoder;
	@Autowired UserDaoJPA userDao;

	// 구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		// TODO Auto-generated method stub
		System.out.println("userRequest : " + userRequest.getClientRegistration());
		// registrationId로 어떤 OAuth로 로그인 했는지 확인가능.
		System.out.println("userRequest : " + userRequest.getAccessToken().getTokenValue());


		OAuth2User oauth2User = super.loadUser(userRequest);
		// 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인을 완료 -> code를 return(OAuth-Client라이브러리) -> AccessToken요청
		// userRequest 정보 -> loadUser함수 -> 구글로부터 회원프로필 받아준다.
		System.out.println("userRequest : " + oauth2User.getAttributes());
		
		OAuth2UserInfo oAuth2UserInfo = null;
		if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			System.out.println("구글 로그인 요청");
			oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
		}
		else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
			System.out.println("네이버 로그인 요청");
			oAuth2UserInfo = new NaverUserInfo((Map<String, Object>)(oauth2User.getAttributes().get("response")));
		}
		else {
			System.out.println("don't");
		}
		
		// 회원가입을 강제로 진행해볼 예정
		String provider = oAuth2UserInfo.getProvider(); // google
		String providerId = oAuth2UserInfo.getProviderId();
		String username = provider + "_" + providerId; 
		String password =  bCryptPasswordEncoder.encode("겟인데어");
		String email = oAuth2UserInfo.getEmail();
		String role = "ROLE_USER";
		
		User userEntity = userDao.findByUsername(username);
		if(userEntity == null) {			// username으로 된 아이디가 없다면
			userEntity = new User(username, password, email, role, provider, providerId);
			userDao.save(userEntity);
		} 
		else {
			System.out.println("기존 회원입니다.");
		}

		return new PrincipalDetails(userEntity, oauth2User.getAttributes());
		// OAuth2 로그인은 userDomain, user에 대한 Attributes(Map<String, Object>)를 들고있어 이 두개를 리턴하는것이 좋다. 
	}
}
