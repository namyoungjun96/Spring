package springbook.user.config;

import org.springframework.context.annotation.Import;

@Import(SqlServiceContext.class)
public @interface EnableSqlService {
	
}
