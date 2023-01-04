package springbook.issuetracker.sqlservice.updatable;

import springbook.issuetracker.sqlservice.updatable.ConcurrentHashMapSqlRegistry;
import springbook.issuetracker.sqlservice.updatable.UpdatableSqlRegistry;

public class ConcurrentHashMapSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {
	protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
		return new ConcurrentHashMapSqlRegistry();
	}
}
