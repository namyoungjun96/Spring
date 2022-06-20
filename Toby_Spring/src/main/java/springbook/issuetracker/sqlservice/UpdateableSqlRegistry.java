package springbook.issuetracker.sqlservice;

import java.util.Map;

import springbook.user.sqlservice.SqlRegistry;

public interface UpdateableSqlRegistry extends SqlRegistry {
	public void updateSql(String key, String sql) throws SqlUpdasteFailureException;
	
	public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException;
}
