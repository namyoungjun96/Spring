package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;
import javax.sql.DataSource;

import org.mariadb.jdbc.MariaDbConnection;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.sqlservice.SqlService;

public class UserDaoJdbc implements UserDao {
	private SqlService sqlService;
	
	public void setSqlService(SqlService sqlService) {
		this.sqlService = sqlService;
	}
	
	private RowMapper<User> userMapper = 
			new RowMapper<User>() {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			// id, name, password, level, login, recommend
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			user.setLevel(Level.valueOf(rs.getInt("level")));
			user.setLogin(rs.getInt("login"));
			user.setRecommend(rs.getInt("recommend"));
			user.setEmail(rs.getString("email"));
			return user;
		}
			};
	
//	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
 	public void add(final User user) throws DuplicateUserIdException {
 		try {
 			// JDBC를 이용해 USER 정보를 db에 추가하는 코드 또는
 			// 그런 기능을 가진 다른 SQLException을 던지는 메소드를 호출하는 코드
// 			"insert into users(id, name, password, level, login, recommend, email)" + "values(?, ?, ?, ?, ?, ?, ?)",
 			this.jdbcTemplate.update(this.sqlService.getSql("userAdd"),
 					user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail());
 		} catch (DuplicateUserIdException e) {
 			// ErrorCode가 MySQL의 "Duplicate Entry(1062)"이면 예외 전환
 				throw new DuplicateUserIdException(e);
 		}
	}
	
	public User get(String id){
		return this.jdbcTemplate.queryForObject(this.sqlService.getSql("userGet"), 
				new Object[] {id}, this.userMapper);
	}
	
	public void deleteAll() {
		this.jdbcTemplate.update(this.sqlService.getSql("userDeleteAll"));
	}
	
	public int getCount() {
		return this.jdbcTemplate.queryForObject(this.sqlService.getSql("userGetCount"), Integer.class);
	}
	
	public List<User> getAll() {
		return this.jdbcTemplate.query(this.sqlService.getSql("userGetAll"), this.userMapper);
	}

	@Override
	public void update(User user) {
		this.jdbcTemplate.update(this.sqlService.getSql("userUpdate"),
				user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail(), user.getId());
	}
	
	//abstract protected PreparedStatement makeStatement(Connection c) throws SQLException;
}
