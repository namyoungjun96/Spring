package springbook.learningtest.spring.embeddeddb;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import java.util.List;
import java.util.Map;

public class EmbeddedDbTest {
	EmbeddedDatabase db;
	JdbcTemplate template;
	
	@BeforeEach
	public void setUp() {
		db = new EmbeddedDatabaseBuilder()
				.setType(HSQL)
				.addScript("classpath:/springbook/learningtest/spring/embeddeddb/schema.sql")
				.addScript("classpath:/springbook/learningtest/spring/embeddeddb/data.sql")
				.build();
		
		template = new JdbcTemplate(db);
	}
	
	@AfterEach
	public void tearDown() {
		db.shutdown();
	}
	
	@Test
	public void initData() {
		assertThat(template.queryForObject("select count(*) from SQLMAP", Integer.class), equalTo(2));
		
		List<Map<String, Object>> list = template.queryForList("select * from SQLMAP order by KEY_");
		assertThat((String)list.get(0).get("KEY_"), equalTo("KEY1"));
		assertThat((String)list.get(0).get("SQL_"), equalTo("SQL1"));
		assertThat((String)list.get(1).get("KEY_"), equalTo("KEY2"));
		assertThat((String)list.get(1).get("SQL_"), equalTo("SQL2"));
	}
	
	@Test
	public void insert() {
		template.update("insert into SQLMAP(KEY_, SQL_) values(?, ?)", "KEY3", "SQL3");
		
		assertThat(template.queryForObject("select count(*) from SQLMAP", Integer.class), equalTo(3));
	}
}
