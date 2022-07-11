package springbook.user.config;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import springbook.user.dao.UserDao;

public class UserSqlMapConfig implements SqlMapConfig {
	@Override
	public Resource getSqlMapResource() {
		// TODO Auto-generated method stub
		return new ClassPathResource("sqlmap.xml", UserDao.class);
	}
}
