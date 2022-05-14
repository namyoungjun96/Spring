package springbook.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.domain.Level;
import springbook.user.domain.User;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserDaoTest {
	@Autowired
	private ApplicationContext context;
	@Autowired
	private UserDao dao;
	@Autowired
	private DataSource dataSource;
	
	private User user;
	private User user2;
	private User user3;
	
	@Before
	public void setUp() {
//		this.dao = context.getBean("userDao", UserDao.class);
		
		this.user = new User("dudwns12", "남영준", "1234", Level.BASIC, 1, 0, "fuswns96@naver.com");
		this.user2 = new User("fuswns96", "나명준", "1234", Level.SILVER, 55, 10, "ehdgornltls@naver.com");
		this.user3 = new User("dudwns", "남앵준", "1234", Level.GOLD, 100, 40, "ehdgornltls96@naver.com");
		
//		System.out.println(this.context);
//		System.out.println(this);
//		
//		DataSource dataSource = new SingleConnectionDataSource("jdbc:mysql://localhost/tobi", "root", "1234", true);
//		dao.setDataSource(dataSource);
	}
	
//	@Test
//	public void addAndGet() throws SQLException, ClassNotFoundException {
//		dao.deleteAll();
//		assertThat(dao.getCount(), is(0));
//		
//		User user = new User();
//		user.setId("ehdgo");
//		user.setName("남영준");
//		user.setPassword("1234");
//
//		dao.add(user);
//		assertThat(dao.getCount(), is(1));
//		
//		User user2 = dao.get(user.getId());
//		
//		assertThat(user2.getName(), is(user.getName()));
//		assertThat(user2.getPassword(), is(user.getPassword()));
//		
//		User userget = dao.get(user.getId());
//		checkSameUser(userget, user);
//		
//		User userget2 = dao.get(user2.getId());
//		checkSameUser(userget2, user2);
//	}
	
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
		assertThat(dao.getCount(), is(0));
		
		dao.add(user);
		assertThat(dao.getCount(), is(1));
		
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		
		dao.add(user3);
		assertThat(dao.getCount(), is(3));
	}
	
	@Test(expected=EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException, ClassNotFoundException {
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.get("unkown_id");
	}
	
	@Test
	public void getAll() throws ClassNotFoundException, SQLException {
		dao.deleteAll();
		
		List<User> users0 = dao.getAll();
		assertThat(users0.size(), is(0));
		
		dao.add(user); // Id: ehdgornltls
		List<User> users1 = dao.getAll();
		assertThat(users1.size(), is(1));
		checkSameUser(user, users1.get(0));
		
		dao.add(user2); // Id: fuswns96
		List<User> users2 = dao.getAll();
		assertThat(users2.size(), is(2));
		checkSameUser(user, users2.get(0));
		checkSameUser(user2, users2.get(1));
		
		dao.add(user3); // Id: dudwns
		List<User> users3 = dao.getAll();
		assertThat(users3.size(), is(3));
		checkSameUser(user3, users3.get(0));
		checkSameUser(user, users3.get(1));
		checkSameUser(user2, users3.get(2));
	}
	
	private void checkSameUser(User user, User user2) {
		assertThat(user.getId(), is(user2.getId()));
		assertThat(user.getName(), is(user2.getName()));
		assertThat(user.getPassword(), is(user2.getPassword()));
		assertThat(user.getLevel(), is(user2.getLevel()));
		assertThat(user.getLogin(), is(user2.getLogin()));
		assertThat(user.getRecommend(), is(user2.getRecommend()));
		assertThat(user.getEmail(), is(user2.getEmail()));
	}
	
	@Test(expected = DuplicateKeyException.class)
	public void duplicateKey() {
		dao.deleteAll();
		
		dao.add(user);
		dao.add(user);
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

}
