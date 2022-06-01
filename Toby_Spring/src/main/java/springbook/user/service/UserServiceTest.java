package springbook.user.service;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import springbook.learningtest.jdk.proxy.Hello;
import springbook.learningtest.jdk.proxy.HelloTarget;
import springbook.learningtest.jdk.proxy.UppercaseHandler;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import static springbook.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserServiceImpl.MIN_RECCOMEND_FOR_GOLD;

// @TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL) autowired를 생성자 주입 방식으로 만드는 어노테이션.

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceTest {
	@Autowired PlatformTransactionManager transactionManager;
	@Autowired UserService userService;
	@Autowired UserService testUserService;
	@Autowired UserDao userDao;
	@Autowired DataSource dataSource;
//	@Autowired UserServiceImpl userServiceImpl;
	@Autowired MailSender mailSender;
	@Autowired ApplicationContext context;

	List<User> users;

	@BeforeEach
	public void setUp() {
		// asList = 배열을 리스트로 만들어주는 편리한 메소드. 배열을 가변인자로 넣어주면 더욱 편리하다.
		users = Arrays.asList(
				new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0, "fuswns96@naver.com"),
				new User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0, "이메일"),
				new User("erwins", "신승환", "p3", Level.SILVER, MIN_RECCOMEND_FOR_GOLD-1, 29, "아무거나"),
				new User("madnite1", "이상호", "p4", Level.SILVER, MIN_RECCOMEND_FOR_GOLD, 30, "넣읍시다"),
				new User("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE, "안되면 나중에 바꾸지 뭐")
				);
	}

	@Test
	@DirtiesContext
	public void upgradeLevels() throws Exception {
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		userDao.deleteAll();
		for(User user : users) userDao.add(user);
		
		MockUserDao mockUserDao = new MockUserDao(this.users);
		userServiceImpl.setUserDao(mockUserDao);
 
		MockMailSender mockMailSender = new MockMailSender();
		userServiceImpl.setMailSender(mockMailSender);

		userServiceImpl.upgradeLevels();
		
		List<User> updated = mockUserDao.getUpdated();
		assertThat(updated.size(), equalTo(2));
		checkUserAndLevel(updated.get(0), "joytouch", Level.SILVER);
		checkUserAndLevel(updated.get(1), "madnite1", Level.GOLD);

//		checkLevelUpgraded(users.get(0), false);
//		checkLevelUpgraded(users.get(1), true);
//		checkLevelUpgraded(users.get(2), false);
//		checkLevelUpgraded(users.get(3), true);
//		checkLevelUpgraded(users.get(4), false);

		List<String> request = mockMailSender.getRequests();
		assertThat(request.size(), equalTo(2));
		assertThat(request.get(0), equalTo(users.get(1).getEmail()));
		assertThat(request.get(1), equalTo(users.get(3).getEmail()));
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
//		UserServiceImpl testUserService = new TestUserServiceImpl();
//		testUserService.setUserDao(userDao);
		//		testUserService.setDataSource(this.dataSource);			필요없음
//		testUserService.setMailSender(mailSender);
//		
//		ProxyFactoryBean txProxyFactoryBean = context.getBean("&userService", ProxyFactoryBean.class);
//		txProxyFactoryBean.setTarget(testUserService);
//		UserService txUserService = (UserService) txProxyFactoryBean.getObject();
		
//		TxProxyFactoryBean txProxyFactoryBean = context.getBean("&userService", TxProxyFactoryBean.class);
//		txProxyFactoryBean.setTarget(testUserService);
//		UserService txUserService = (UserService) txProxyFactoryBean.getObject();

//		UserServiceTx txUserService = new UserServiceTx();
//		txUserService.setTransactionManager(transactionManager);
//		txUserService.setUserService(testUserService);
		
//		TransactionHandler txHandler = new TransactionHandler();
//		txHandler.setTarget(testUserService);
//		txHandler.setTransactionManager(transactionManager);
//		txHandler.setPattern("upgradeLevels");
		
//		UserService txUserService = (UserService)Proxy.newProxyInstance(
//				getClass().getClassLoader(),
//				new Class[] { UserService.class },
//				txHandler);

		userDao.deleteAll();
		for(User user : users) userDao.add(user);

		try {
			this.testUserService.upgradeLevels();
//			fail("TestUserServiceException expected");
		} catch(TestUserServiceException e) {} 
		finally { checkLevelUpgraded(users.get(1), false); }
	}
	
	@Test
	public void mockUpgradeLevels() throws Exception {
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		UserDao mockUserDao = mock(UserDao.class);
		when(mockUserDao.getAll()).thenReturn(this.users);
		userServiceImpl.setUserDao(mockUserDao);
		
		MailSender mockMailSender = mock(MailSender.class);
		userServiceImpl.setMailSender(mockMailSender);
		
		userServiceImpl.upgradeLevels();
		
		verify(mockUserDao, times(2)).update(any(User.class));
		verify(mockUserDao, times(2)).update(any(User.class));
		verify(mockUserDao).update(users.get(1));
		assertThat(users.get(1).getLevel(), equalTo(Level.SILVER));
		verify(mockUserDao).update(users.get(3));
		assertThat(users.get(3).getLevel(), equalTo(Level.GOLD));
		
		ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
		verify(mockMailSender, times(2)).send(mailMessageArg.capture());
		List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
		assertThat(mailMessages.get(0).getTo()[0], equalTo(users.get(1).getEmail()));
		assertThat(mailMessages.get(1).getTo()[0], equalTo(users.get(3).getEmail()));
	}

	static class TestUserServiceImpl extends UserServiceImpl {
		private String id = "madnite1";

//		private TestUserService(String id) {
//			this.id = id;
//		}

		protected void upgradeLevel(User user) {
			if(user.getId().equals(this.id)) throw new TestUserServiceException();
			super.upgradeLevel(user);
		}
	}

	static class TestUserServiceException extends RuntimeException {

	}

	static class MockMailSender implements MailSender {
		private List<String> requests = new ArrayList<String>();

		public List<String> getRequests(){
			return requests;
		}

		public void send(SimpleMailMessage mailMessage) throws MailException {
			requests.add(mailMessage.getTo()[0]);
		}

		public void send(SimpleMailMessage[] mailMessage) throws MailException {}
	}

	static class MockUserDao implements UserDao {
		private List<User> users;
		private List<User> updated = new ArrayList();
		
		private MockUserDao(List<User> users) {
			this.users = users;
		}
		
		public List<User> getUpdated(){
			return this.updated;
		}
		
		public List<User> getAll(){
			return this.users;
		}
		
		public void update(User user) {
			updated.add(user);
		}

		
		// 테스트에 사용되지 않는 메소드들
		@Override
		public void add(User user) { throw new UnsupportedOperationException(); }
		@Override
		public User get(String id) { throw new UnsupportedOperationException(); }
		@Override
		public void deleteAll() { throw new UnsupportedOperationException(); }
		@Override
		public int getCount() { throw new UnsupportedOperationException(); }
	}
	
	private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
		assertThat(updated.getId(), equalTo(expectedId));
		assertThat(updated.getLevel(), equalTo(expectedLevel));
	}
	
	//	@Test
	//	public void bean() {
	//		assertThat(this.userService, is(notNullValue()));
	//		this.userService.testPrint();
	//	}
}
