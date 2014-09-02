package nz.co.neo4j.sample.rest.simple.config;

import javax.annotation.Resource;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.rest.graphdb.RestAPI;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan("nz.co.neo4j.sample.rest.simple")
@PropertySource("classpath:neo4j-rest.properties")
public class ApplicationContextConfiguration {
	private static final String HTTP_URI_CONFIG = "neo4j.server.httpuri";
	private static final String SERVER_ROOT_PATH_CONFIG = "neo4j.server.rootPath";
	// private static final String USER_NAME_CONFIG = "neo4j.server.username";
	// private static final String PASSWORD_CONFIG = "neo4j.server.password";

	@Resource
	private Environment environment;

	@Bean(destroyMethod = "shutdown")
	public GraphDatabaseService restGraphDatabase() {
		String ServerHttpUri = environment.getProperty(HTTP_URI_CONFIG,
				"http://localhost:7474");
		String serverRootPath = environment.getProperty(
				SERVER_ROOT_PATH_CONFIG, "/db/data/");
		GraphDatabaseService db = new RestGraphDatabase(ServerHttpUri
				+ serverRootPath);
		return db;
	}

	@Bean
	public RestAPI restAPI() {
		return ((RestGraphDatabase) restGraphDatabase()).getRestAPI();
	}

}
