package nz.co.neo4j.sample.movie.config;

import javax.annotation.Resource;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;

@Configuration
@EnableNeo4jRepositories(basePackages = "nz.co.neo4j.sample.movie.data.repository")
@PropertySource("classpath:neo4j-rest.properties")
public class RemoteNeo4jServerConfig extends Neo4jConfiguration {

	@Resource
	private Environment environment;
	private static final String HTTP_URI_CONFIG = "neo4j.server.httpuri";
	private static final String SERVER_ROOT_PATH_CONFIG = "neo4j.server.rootPath";

	@Bean
	public GraphDatabaseService graphDatabaseService() {
		String ServerHttpUri = environment.getProperty(HTTP_URI_CONFIG,
				"http://localhost:7474");
		String serverRootPath = environment.getProperty(
				SERVER_ROOT_PATH_CONFIG, "/db/data/");
		return new SpringRestGraphDatabase(ServerHttpUri + serverRootPath);
	}
}
