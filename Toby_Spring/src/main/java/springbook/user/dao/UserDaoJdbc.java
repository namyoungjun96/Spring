package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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

public class UserDaoJdbc implements UserDao {
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
			return user;
		}
			};
	
//	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
//		this.jdbcContext = new JdbcContext();
//		this.jdbcContext.setDataSource(dataSource);
		
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		
//		this.dataSource = dataSource;
	}
	
//	public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException {
//		Connection c = null;
//		PreparedStatement ps = null;
//		
//		try {
//			c = dataSource.getConnection();
//			ps = stmt.makePreparedStatement(c);
//			
//			ps.executeUpdate();
//		} catch (SQLException e) {
//			throw e;
//		} finally {
//			if (ps != null) { try { ps.close(); } catch (SQLException e) {} }
//			if (c != null) { try { c.close(); } catch (SQLException e) {} }
//		}
//	}
	
 	public void add(final User user) throws DuplicateUserIdException {
// 		StatementStrategy st = new AddStatement(user);
// 		jdbcContextWithStatementStrategy(st);
 		
// 		class AddStatement implements StatementStrategy {
// 			@Override
// 			public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
// 				PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?, ?, ?)");
//
// 				ps.setString(1, user.getId());
// 				ps.setString(2, user.getName());
// 				ps.setString(3, user.getPassword());
// 				
// 				return ps;
// 			}
// 		}
// 		
// 		StatementStrategy st = new AddStatement();
// 		jdbcContext.workWithStatementStrategy(st);
 		
// 		this.jdbcContext.executeSql("insert into users(id, name, password) values(?, ?, ?)");
 		
 		try {
 			// JDBC를 이용해 USER 정보를 db에 추가하는 코드 또는
 			// 그런 기능을 가진 다른 SQLException을 던지는 메소드를 호출하는 코드
 			this.jdbcTemplate.update("insert into users(id, name, password, level, login, recommend)" + "values(?, ?, ?, ?, ?, ?)",
 					user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());
 		} catch (DuplicateUserIdException e) {
 			// ErrorCode가 MySQL의 "Duplicate Entry(1062)"이면 예외 전환
 				throw new DuplicateUserIdException(e);
 		}
	}
	
	public User get(String id){
//		Connection c = dataSource.getConnection();
//		
//		PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
//		ps.setString(1, id);
//		
//		ResultSet rs = ps.executeQuery();
//		
//		User user = null;
//		
////		User user = new User();
////		user.setId(rs.getString("id"));
////		user.setName(rs.getString("name"));
////		user.setPassword(rs.getString("password"));
//		
//		if (rs.next()) {
//			user = new User();
//			user.setId(rs.getString("id"));
//			user.setName(rs.getString("name"));
//			user.setPassword(rs.getString("password"));
//		}
//		
//		rs.close();
//		ps.close();
//		c.close();
//		
//		if (user == null) throw new EmptyResultDataAccessException(1);
//		
//		return user;
		
//		return this.jdbcTemplate.queryForObject("select * from users where id = ?", 
//				new Object[] {id},
//				new RowMapper<User>() {
//			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
//				User user = new User();
//				user.setId(rs.getString("id"));
//				user.setName(rs.getString("name"));
//				user.setPassword(rs.getString("password"));
//				return user;
//			}
//		});
		
		return this.jdbcTemplate.queryForObject("select * from users where id = ?", 
				new Object[] {id}, this.userMapper);
	}
	
	public void deleteAll() {
//		StatementStrategy st = new DeleteAllStatement();
//		jdbcContextWithStatementStrategy(st);
		
		// 익명함수를 변수에 생성해 할당시켜 쓰는 방법 . * 한번 쓸거라 아래방법을 선택
//		StatementStrategy st = new StatementStrategy() {
//			
//			@Override
//			public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
//				return c.prepareStatement("delete from users");
//			}
//		};
//		jdbcContextWithStatementStrategy(st);
		
		// 익명함수를 할당시키지 않고 바로 생성하는 방법 .
//		jdbcContext.workWithStatementStrategy(
//				new StatementStrategy() {
//					
//					@Override
//					public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
//						return c.prepareStatement("delete from users");
//					}
//				});
		
//		this.jdbcContext.executeSql("delete from users");
		
//		this.jdbcTemplate.update(
//				new PreparedStatementCreator() {
//					
//					@Override
//					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//						return con.prepareStatement("delete from users");
//					}
//				});
		
		this.jdbcTemplate.update("delete from users");
	}
	
	public int getCount() {
//		Connection c = null;
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		
//		try {
//			c = dataSource.getConnection();
//			ps = c.prepareStatement("select count(*) from users");
//			rs = ps.executeQuery();
//			
//			rs.next();
//			return rs.getInt(1);
//		} catch (SQLException e) {
//			throw e;
//		} finally {
//			if (rs != null) {
//				try {
//					rs.close();
//				} catch (SQLException e) {}
//			}
//			if (ps != null) {
//				try {
//					ps.close();
//				} catch (SQLException e) {}
//			}
//			if (c != null) {
//				try {
//					c.close();
//				} catch (SQLException e) {}
//			}
//		}
		
//		return this.jdbcTemplate.query(new PreparedStatementCreator() {
//			
//			@Override
//			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//				return con.prepareStatement("select count(*) from users");
//			}
//		}, new ResultSetExtractor<Integer>() {
//			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
//				rs.next();
//				return rs.getInt(1);
//			}
//		});
		
		return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
	}
	
	public List<User> getAll() {
//		return this.jdbcTemplate.query("select * from users order by id", 
//				new RowMapper<User>() {
//			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
//				User user = new User();
//				user.setId(rs.getString("id"));
//				user.setName(rs.getString("name"));
//				user.setPassword(rs.getString("password"));
//				return user;
//			}
//		});
		return this.jdbcTemplate.query("select * from users order by id", 
				this.userMapper);
	}

	@Override
	public void update(User user) {
		this.jdbcTemplate.update("update users set name = ?, password = ?, level = ?, login = ?, recommend = ? where id = ? ",
				user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getId());
	}
	
	//abstract protected PreparedStatement makeStatement(Connection c) throws SQLException;
}
