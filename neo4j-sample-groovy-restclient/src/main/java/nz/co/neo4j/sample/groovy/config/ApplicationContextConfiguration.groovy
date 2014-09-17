package nz.co.neo4j.sample.groovy.config;

import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan("nz.co.neo4j.sample.groovy")
public class ApplicationContextConfiguration {

	@Bean
	RESTClient neo4jRestClient(){
		return new RESTClient('http://localhost:7474/db/data/',ContentType.JSON)
	}
}
