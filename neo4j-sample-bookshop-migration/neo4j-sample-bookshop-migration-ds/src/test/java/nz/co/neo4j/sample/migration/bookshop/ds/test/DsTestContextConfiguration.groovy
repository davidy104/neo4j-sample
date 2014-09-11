package nz.co.neo4j.sample.migration.bookshop.ds.test;

import javax.annotation.Resource

import nz.co.neo4j.sample.migration.bookshop.config.InfrastructureContextConfiguration
import nz.co.neo4j.sample.migration.bookshop.data.support.CustomerInitialTestDataSetup

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate

@Configuration
@Import(value = [InfrastructureContextConfiguration.class ])
@ComponentScan("nz.co.neo4j.sample.migration.bookshop.ds")
public class DsTestContextConfiguration {

	@Resource
	private PlatformTransactionManager transactionManager;

	@Bean(initMethod = "initialize")
	public CustomerInitialTestDataSetup setupCustomerTestData() {
		return new CustomerInitialTestDataSetup(new TransactionTemplate(
		transactionManager));
	}
}
