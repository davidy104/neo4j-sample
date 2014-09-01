package nz.co.neo4j.sample.bookshop.config;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.kernel.impl.util.StringLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("nz.co.neo4j.sample.bookshop")
public class ApplicationContextConfiguration {

	public static final String DB_PATH = "database/bookshop";

	@Bean
	public GraphDatabaseService graphDatabaseService() {
		GraphDatabaseService db = new GraphDatabaseFactory()
				.newEmbeddedDatabaseBuilder(DB_PATH)
				.setConfig(GraphDatabaseSettings.nodestore_mapped_memory_size,
						"20M").newGraphDatabase();
		return db;
	}

	@Bean
	public ExecutionEngine executionEngine() {
		return new ExecutionEngine(graphDatabaseService(), StringLogger.SYSTEM);
	}

}
