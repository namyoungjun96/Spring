package springbook.issuetracker.sqlservice.updatable;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import springbook.user.sqlservice.SqlNotFoundException;

public abstract class AbstractUpdatableSqlRegistryTest {
	UpdatableSqlRegistry sqlRegistry;
	
	@Before
	public void setUp() {
		sqlRegistry = createUpdatableSqlRegistry();
		sqlRegistry.registerSql("KEY1", "SQL1");
		sqlRegistry.registerSql("KEY2", "SQL2");
		sqlRegistry.registerSql("KEY3", "SQL3");
	}
	
	abstract protected UpdatableSqlRegistry createUpdatableSqlRegistry();

	@Test
	public void find() {
		checkFind("SQL1", "SQL2", "SQL3");
	}
	
	@Test
	public void unknownKey() {
		assertThrows(SqlNotFoundException.class, () -> {
			sqlRegistry.findSql("SQL9999!@#$");
		});
	}
	
	protected void checkFind(String expected1, String expected2, String expected3) {
		assertThat(sqlRegistry.findSql("KEY1"), equalTo(expected1));		
		assertThat(sqlRegistry.findSql("KEY2"), equalTo(expected2));		
		assertThat(sqlRegistry.findSql("KEY3"), equalTo(expected3));		
	}
	
	@Test
	public void updateSingle() {
		sqlRegistry.updateSql("KEY2", "Modified2");
		
		checkFind("SQL1", "Modified2", "SQL3");
	}
	
	@Test
	public void updateMulti() {
		Map<String, String> sqlmap = new HashMap<String, String>();
		sqlmap.put("KEY1", "Modified1");
		sqlmap.put("KEY3", "Modified3");
		
		sqlRegistry.updateSql(sqlmap);
		
		checkFind("Modified1", "SQL2", "Modified3");
	}
	
	@Test
	public void updateWithNotExistingkey() {
		assertThrows(SqlUpdateFailureException.class, () -> {
			sqlRegistry.updateSql("SQL9999!@#$", "Modified2");
		});
	}
}