//package springbook.learningtest.jdk.jaxb;
//
//import static org.hamcrest.CoreMatchers.equalTo;
//import static org.hamcrest.MatcherAssert.assertThat;
//
//import java.io.IOException;
//import java.util.List;
//
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.JAXBException;
//import javax.xml.bind.Unmarshaller;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import springbook.user.sqlservice.jaxb.SqlType;
//import springbook.user.sqlservice.jaxb.Sqlmap;
//
//@ExtendWith(SpringExtension.class)
//public class JaxbTest {
//	@Test
//	public void readSqlmap() throws JAXBException, IOException {
//		String contextPath = Sqlmap.class.getPackage().getName();
//		System.out.println("이거 안나오나?" + contextPath);
//		JAXBContext context = JAXBContext.newInstance(contextPath);
//		Unmarshaller unmarshaller = context.createUnmarshaller();
//		Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(getClass().getResourceAsStream("sqlmap.xml"));
//		List<SqlType> sqlList = sqlmap.getSql();
//
//		assertThat(sqlList.size(), equalTo(3));
//		assertThat(sqlList.get(0).getKey(), equalTo("add"));
//		assertThat(sqlList.get(0).getValue(), equalTo("insert"));
//		assertThat(sqlList.get(1).getKey(), equalTo("get"));
//		assertThat(sqlList.get(1).getValue(), equalTo("select"));
//		assertThat(sqlList.get(2).getKey(), equalTo("delete"));
//		assertThat(sqlList.get(2).getValue(), equalTo("delete"));
//	}
//}
