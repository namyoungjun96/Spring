package springbook.learningtest.spring.jdbc.namedparam;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class NamedParameterJdbcTemplateTest {
	@Autowired DataSource dataSource;
	
	@Test
	public void useObject() {
		NamedParameterJdbcTemplate namedparamter = new NamedParameterJdbcTemplate(dataSource);
//		namedparamter.execute("insert into users(id, name, password, level, login, recommend, email) "
//				+ "values(?, ?, ?, ?, ?, ?, ?)", "jun", "준", "1234", "0", "100", "20", "jun.email");
	}
}
