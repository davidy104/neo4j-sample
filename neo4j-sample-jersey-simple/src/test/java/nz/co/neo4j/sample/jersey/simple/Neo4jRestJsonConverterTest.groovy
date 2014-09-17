package nz.co.neo4j.sample.jersey.simple;

import groovy.util.logging.Slf4j

import java.nio.file.Path
import java.nio.file.Paths

import javax.annotation.Resource

import nz.co.neo4j.sample.jersey.simple.config.ApplicationContextConfiguration

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationContextConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
public class Neo4jRestJsonConverterTest {

	@Resource
	Neo4jRestJsonConverter converter

	final static String TEST_CYPHER_RESPONSE_FILE = "/cypher/result-sample.json"
	String responseSample

	@Before
	void setUp(){
		Path resPath = Paths.get(TestUtil.class.getResource(
				TEST_CYPHER_RESPONSE_FILE).toURI())
		responseSample = new String(java.nio.file.Files.readAllBytes(resPath),
				"UTF8")
	}

	@Test
	void testGetDataFromResponse() {
		Map<String, Map<String, Object>> resultMap = converter
				.getDataFromResponse(responseSample)
		log.info "responseSample: {} $responseSample"
		resultMap.each() { key, value ->
			log.info "selfid:{} ${key}"
			Map dataMap = (Map)value
			dataMap.each {dataKey, dataVal ->
				log.info "${dataKey} == ${dataVal}"
			}
		}
	}
}
