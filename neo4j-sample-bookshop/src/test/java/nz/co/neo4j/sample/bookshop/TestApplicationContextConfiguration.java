package nz.co.neo4j.sample.bookshop;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.impl.util.StringLogger;
import org.neo4j.test.TestGraphDatabaseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "nz.co.neo4j.sample.bookshop" })
public class TestApplicationContextConfiguration {

	public static final String DB_PATH = "database/bookshop";

	@Bean
	public GraphDatabaseService graphDatabaseService() {
		return new TestGraphDatabaseFactory().newImpermanentDatabase();
	}

	@Bean
	public ExecutionEngine executionEngine() {
		return new ExecutionEngine(graphDatabaseService(), StringLogger.SYSTEM);
	}

}
