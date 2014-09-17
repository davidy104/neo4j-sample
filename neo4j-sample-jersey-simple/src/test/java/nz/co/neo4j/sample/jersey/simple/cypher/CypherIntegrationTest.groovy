package nz.co.neo4j.sample.jersey.simple.cypher;

import static nz.co.neo4j.sample.jersey.simple.JerseyClientUtil.getResponsePayload
import static nz.co.neo4j.sample.jersey.simple.TestUtil.getJsonScripts
import static nz.co.neo4j.sample.jersey.simple.TestUtil.jsonToMap
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue
import static org.junit.Assert.assertNull
import java.util.Map;

import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

import javax.annotation.Resource
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response.Status

import nz.co.neo4j.sample.jersey.simple.Neo4jRestJsonConverter
import nz.co.neo4j.sample.jersey.simple.config.ApplicationContextConfiguration

import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.ClientResponse
import com.sun.jersey.api.client.WebResource

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationContextConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
class CypherIntegrationTest {
	static final String HTTP_URI = "http://localhost:7474/db/data/"

	static Map<String, String> jsonTestScripts

	static final String TEST_INITIAL = "/cypher/initial.json"
	static final String TEST_QUERY_AUTHOR = "/cypher/queryAuthor.json"
	static final String TEST_CLEANUP = "/cypher/cleanup.json"

	static final String TEST_CREATE_NODE = "/cypher/createNode.json"
	static final String TEST_DELETE_NODE = "/cypher/deleteNode.json"
	static final String TEST_GET_NODE = "/cypher/getNode.json"

	@Resource
	Neo4jRestJsonConverter converter

	@Resource
	Client jerseyClient

	def jsonSlurper = new JsonSlurper()

	@BeforeClass
	static void setUp() {
		jsonTestScripts = getJsonScripts(TEST_INITIAL, TEST_QUERY_AUTHOR,
				TEST_CLEANUP, TEST_CREATE_NODE, TEST_DELETE_NODE, TEST_GET_NODE)
	}

	@Before
	void initial() {
		String intialJson = jsonTestScripts.get(TEST_INITIAL)
		WebResource webResource = jerseyClient.resource(HTTP_URI).path(
				"transaction/commit")
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, intialJson)
		String respStr = getResponsePayload(response)
		if (response.getStatusInfo().statusCode != Status.OK.code) {
			Map jsonResult = (Map) jsonSlurper.parseText(respStr)
			def error = jsonResult.get("errors")
			throw new RuntimeException("Initial failed. ${error}")
		}
	}

	@After
	public void teardown() {
		String deleteJson = jsonTestScripts.get(TEST_CLEANUP);
		WebResource webResource = jerseyClient.resource(HTTP_URI).path(
				"transaction/commit")
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, deleteJson)

		String respStr = getResponsePayload(response)
		if (response.getStatusInfo().statusCode != Status.OK.code) {
			Map jsonResult = (Map) jsonSlurper.parseText(respStr)
			def error = jsonResult.get("errors")
			throw new RuntimeException("teardown failed. ${error}")
		}
	}

	@Test
	public void testCreateAndDeleteSimpleNode(){
		Map<String,Map<String,Object>> resultMap=[:]
		ClientResponse response = execute(jsonTestScripts.get(TEST_CREATE_NODE))
		assertEquals(response.getStatusInfo().statusCode,
				Status.OK.code)
		String respStr = getResponsePayload(response)
		resultMap = converter.getDataFromResponse(respStr)
		assertFalse(resultMap.isEmpty())
		printResultMap(resultMap)

		response = execute(jsonTestScripts.get(TEST_GET_NODE));
		assertEquals(response.getStatusInfo().statusCode,
				Status.OK.code)
		respStr = getResponsePayload(response)
		resultMap = converter.getDataFromResponse(respStr)
		assertFalse(resultMap.isEmpty())
		printResultMap(resultMap)

		response = execute(jsonTestScripts.get(TEST_DELETE_NODE));
		assertEquals(response.getStatusInfo().statusCode,
				Status.OK.code)
		respStr = getResponsePayload(response)
		resultMap = converter.getDataFromResponse(respStr)
		assertTrue(resultMap.isEmpty())


		response = execute(jsonTestScripts.get(TEST_GET_NODE));
		assertEquals(response.getStatusInfo().statusCode,
				Status.OK.code)
		respStr = getResponsePayload(response)
		assertTrue(resultMap.isEmpty())
	}

	void printResultMap(Map<String,Map<String,Object>> resultMap){
		resultMap.each() { key, value ->
			log.info "selfid:{} ${key}"
			Map dataMap = (Map)value
			dataMap.each {dataKey, dataVal ->
				log.info "${dataKey} == ${dataVal}"
			}
		}
	}

	/**
	 * { "query":
	 * "MATCH (b:Book)<-[:AuthorOf]-(p) WHERE b.title = {title} RETURN b,p",
	 * "params":{ "title":"Things Fall Apart" } }
	 * 
	 * @throws Exception
	 */
	@Test
	void testQueryBookAndAuthor() {
		ClientResponse response = execute(jsonTestScripts
				.get(TEST_QUERY_AUTHOR));
		String respStr = getResponsePayload(response);
		log.info "author query: {} $respStr"
		assertEquals(response.getStatusInfo().getStatusCode(),
				Status.OK.getStatusCode());
	}

	ClientResponse execute(final String jsonBody){
		WebResource webResource = jerseyClient.resource(HTTP_URI)
				.path("cypher")
		return webResource.accept(MediaType.APPLICATION_JSON)
		.type(MediaType.APPLICATION_JSON)
		.post(ClientResponse.class, jsonBody)
	}
}
