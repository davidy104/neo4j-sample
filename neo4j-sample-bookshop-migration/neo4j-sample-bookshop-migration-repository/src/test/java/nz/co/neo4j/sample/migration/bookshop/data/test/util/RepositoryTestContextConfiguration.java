package nz.co.neo4j.sample.migration.bookshop.data.test.util;

import javax.annotation.Resource;

import nz.co.neo4j.sample.migration.bookshop.config.InfrastructureContextConfiguration;
import nz.co.neo4j.sample.migration.bookshop.data.support.InitialDataSetup;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@Import(value = { InfrastructureContextConfiguration.class })
public class RepositoryTestContextConfiguration {

	@Resource
	private PlatformTransactionManager transactionManager;

	@Bean(initMethod = "initialize")
	public InitialDataSetup initialDataSetup() {
		return new InitialDataSetup(new TransactionTemplate(transactionManager));
	}
}
