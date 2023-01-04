package springbook.user.dao;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import springbook.user.config.AppContext;
import springbook.user.domain.Level;
import springbook.user.domain.User;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppContext.class)
@ActiveProfiles("test")
public class UserDaoTest {
	@Autowired UserDao dao;
	@Autowired DataSource dataSource;
	
	private User user;
	private User user2;
	private User user3;
	
	@BeforeEach
	public void setUp() {
		this.user = new User("dudwns12", "남영준", "1234", Level.BASIC, 1, 0, "fuswns96@naver.com");
		this.user2 = new User("fuswns96", "나명준", "1234", Level.SILVER, 55, 10, "ehdgornltls@naver.com");
		this.user3 = new User("dudwns", "남앵준", "1234", Level.GOLD, 100, 40, "ehdgornltls96@naver.com");
	}
	
	@Test
	public void update() {
		dao.deleteAll();
		
		dao.add(user);
		dao.add(user2);
		
		user.setName("나명준");
		user.setPassword("5678");
		user.setLevel(Level.GOLD);
		user.setLogin(1000);
		user.setRecommend(999);
		user.setEmail("dkahffkd@naver.com");
		dao.update(user);
		
		User userupdate = dao.get(user.getId());
		checkSameUser(user, userupdate);
		User user2same = dao.get(user2.getId());
		checkSameUser(user2, user2same);
	}
	
	@Test
	public void count() throws SQLException, ClassNotFoundException {
		dao.deleteAll();
		assertThat(dao.getCount(), equalTo(0));
		
		dao.add(user);
		assertThat(dao.getCount(), equalTo(1));
		
		dao.add(user2);
		assertThat(dao.getCount(), equalTo(2));
		
		dao.add(user3);
		assertThat(dao.getCount(), equalTo(3));
	}
	
	@Test
	public void getUserFailure() throws SQLException, ClassNotFoundException {
		dao.deleteAll();
		assertThat(dao.getCount(), equalTo(0));
		assertThrows(EmptyResultDataAccessException.class, () -> {
			dao.get("unknown_id");
		});
	}
	
	@Test
	public void getAll() throws ClassNotFoundException, SQLException {
		dao.deleteAll();
		
		List<User> users0 = dao.getAll();
		assertThat(users0.size(), equalTo(0));
		
		dao.add(user); // Id: ehdgornltls
		List<User> users1 = dao.getAll();
		assertThat(users1.size(), equalTo(1));
		checkSameUser(user, users1.get(0));
		
		dao.add(user2); // Id: fuswns96
		List<User> users2 = dao.getAll();
		assertThat(users2.size(), equalTo(2));
		checkSameUser(user, users2.get(0));
		checkSameUser(user2, users2.get(1));
		
		dao.add(user3); // Id: dudwns
		List<User> users3 = dao.getAll();
		assertThat(users3.size(), equalTo(3));
		checkSameUser(user3, users3.get(0));
		checkSameUser(user, users3.get(1));
		checkSameUser(user2, users3.get(2));
	}
	
	private void checkSameUser(User user, User user2) {
		assertThat(user.getId(), equalTo(user2.getId()));
		assertThat(user.getName(), equalTo(user2.getName()));
		assertThat(user.getPassword(), equalTo(user2.getPassword()));
		assertThat(user.getLevel(), equalTo(user2.getLevel()));
		assertThat(user.getLogin(), equalTo(user2.getLogin()));
		assertThat(user.getRecommend(), equalTo(user2.getRecommend()));
		assertThat(user.getEmail(), equalTo(user2.getEmail()));
	}
	
	@Test
	public void duplicateKey() {
		dao.deleteAll();
		
		dao.add(user);
		assertThrows(DuplicateKeyException.class, () -> {
			dao.add(user);
		});
		
	}
	
	@Test
	public void sqlExceptionTranslate() {
		dao.deleteAll();
		
		try {
			dao.add(user);
			dao.add(user);
		} catch(DuplicateKeyException ex) {
			SQLException sqlEx = (SQLException)ex.getRootCause();
			SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
			assertThat(set.translate(null, null, sqlEx), instanceOf(DuplicateKeyException.class));
		}
	}
	
	@Autowired DefaultListableBeanFactory bf;
	
	@Test
	public void beans() {
		for(String n : bf.getBeanDefinitionNames())
			System.out.println(n + "\t " + bf.getBean(n).getClass().getName());
	}
}
