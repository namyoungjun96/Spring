package springbook.user.service;

import static org.junit.jupiter.api.Assertions.fail;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import static springbook.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserService.MIN_RECCOMEND_FOR_GOLD;

// @TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL) autowired를 생성자 주입 방식으로 만드는 어노테이션.

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceTest {
	@Autowired
	PlatformTransactionManager transactionManager;
	@Autowired
	UserService userService;
	@Autowired
	UserDao userDao;
	@Autowired
	DataSource dataSource;
	
	List<User> users;
	
	@BeforeEach
	public void setUp() {
		// asList = 배열을 리스트로 만들어주는 편리한 메소드. 배열을 가변인자로 넣어주면 더욱 편리하다.
		users = Arrays.asList(
				new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
				new User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
				new User("erwins", "신승환", "p3", Level.SILVER, MIN_RECCOMEND_FOR_GOLD-1, 29),
				new User("madnite1", "이상호", "p4", Level.SILVER, MIN_RECCOMEND_FOR_GOLD, 30),
				new User("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE)
		);
	}
	
	@Test
	public void upgradeLevels() throws Exception {
		userDao.deleteAll();
		for(User user : users) userDao.add(user);
		
		userService.upgradeLevels();
		
		checkLevelUpgraded(users.get(0), false);
		checkLevelUpgraded(users.get(1), true);
		checkLevelUpgraded(users.get(2), false);
		checkLevelUpgraded(users.get(3), true);
		checkLevelUpgraded(users.get(4), false);
	}
	
	private void checkLevelUpgraded(User user, boolean upgraded) {
		User userUpdate = userDao.get(user.getId());
		if(upgraded) assertThat(userUpdate.getLevel(), equalTo(user.getLevel().nextLevel()));
		else assertThat(userUpdate.getLevel(), equalTo(user.getLevel()));
	}
	
	private void checkLevel(User user, Level expectedLevel) {
		User userUpdate = userDao.get(user.getId());
		assertThat(userUpdate.getLevel(), equalTo(expectedLevel));
	}
	
	@Test
	public void add() {
		userDao.deleteAll();
		
		User userWithLevel = users.get(4);	// GOLD 레벨
		User userWithoutLevel = users.get(0);
		userWithoutLevel.setLevel(null);
		
		userService.add(userWithLevel);
		userService.add(userWithoutLevel);
		
		User userWithLevelRead = userDao.get(userWithLevel.getId());
		User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());
		
		assertThat(userWithLevelRead.getLevel(), equalTo(userWithLevel.getLevel()));
		assertThat(userWithoutLevelRead.getLevel(), equalTo(userWithoutLevel.getLevel()));
	}
	
	@Test
	public void upgradeAllOrNothing() throws Exception {
		UserService testUserService = new TestUserService(users.get(3).getId());
		testUserService.setUserDao(userDao);
		testUserService.setTransactionManager(transactionManager);
//		testUserService.setDataSource(this.dataSource);			필요없음
		
		userDao.deleteAll();
		for(User user : users) userDao.add(user);
		
		try {
			testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		} catch(TestUserServiceException e) {} 
		finally { checkLevelUpgraded(users.get(1), false); }
	}
	
	static class TestUserService extends UserService {
		private String id;
		
		private TestUserService(String id) {
			this.id = id;
		}
		
		protected void upgradeLevel(User user) {
			if(user.getId().equals(this.id)) throw new TestUserServiceException();
			super.upgradeLevel(user);
		}
	}
	
	static class TestUserServiceException extends RuntimeException {
		
	}
	
//	@Test
//	public void bean() {
//		assertThat(this.userService, is(notNullValue()));
//		this.userService.testPrint();
//	}
}
