package nz.co.neo4j.sample.migration.bookshop;

import nz.co.neo4j.sample.migration.bookshop.config.InfrastructureContextConfiguration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "nz.co.neo4j.sample.migration.bookshop")
@Import(value = { InfrastructureContextConfiguration.class })
public class TestContextConfiguration {

}
