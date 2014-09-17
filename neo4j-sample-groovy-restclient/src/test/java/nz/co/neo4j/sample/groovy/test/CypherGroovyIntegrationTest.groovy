package nz.co.neo4j.sample.groovy.test;


import static nz.co.neo4j.sample.groovy.test.TestUtil.getJsonScripts
import static org.junit.Assert.*

import java.awt.PageAttributes.MediaType;

import groovy.util.logging.Slf4j
import groovyx.net.http.RESTClient

import javax.annotation.Resource

import nz.co.neo4j.sample.groovy.Neo4jRestJsonConverter
import nz.co.neo4j.sample.groovy.config.ApplicationContextConfiguration

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationContextConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
class CypherGroovyIntegrationTest {

	static final String TEST_INITIAL = "/cypher/initial.json"
	static final String TEST_QUERY_AUTHOR = "/cypher/queryAuthor.json"
	static final String TEST_CLEANUP = "/cypher/cleanup.json"
	static final String TEST_CREATE_NODE = "/cypher/createNode.json"
	static final String TEST_DELETE_NODE = "/cypher/deleteNode.json"
	static final String TEST_GET_NODE = "/cypher/getNode.json"

	@Resource
	Neo4jRestJsonConverter converter

	@Resource
	RESTClient neo4jRestClient

	static Map<String,String> jsonTestScripts

	@BeforeClass
	public static void setUp(){
		jsonTestScripts = getJsonScripts(TEST_INITIAL, TEST_QUERY_AUTHOR,
				TEST_CLEANUP, TEST_CREATE_NODE, TEST_DELETE_NODE, TEST_GET_NODE);
	}

	@Before
	public void initial(){
		String intialJson = jsonTestScripts.get(TEST_INITIAL)
		def response = neo4jRestClient.post(path: 'transaction/commit', body : intialJson)
//		log.info "status: {} ${response.status}"
//		String respStr = (String)response.data
		log.info "data: {} ${response}"
	}
	
	
	@Test
	public void test() {
		
	}
}
