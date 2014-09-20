

package nz.co.neo4j.sample.jersey.simple.normal;

import static nz.co.neo4j.sample.jersey.simple.JerseyClientUtil.getResponsePayload
import static nz.co.neo4j.sample.jersey.simple.TestUtil.getJsonScripts
import static org.junit.Assert.*
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

import javax.annotation.Resource
import javax.ws.rs.core.MediaType

import nz.co.neo4j.sample.jersey.simple.Neo4jRestJsonConverter;
import nz.co.neo4j.sample.jersey.simple.config.ApplicationContextConfiguration

import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.ClientResponse
import com.sun.jersey.api.client.WebResource
import com.sun.jersey.api.client.ClientResponse.Status

/**
 * http://localhost:7474/db/data/node/123/relationships/{dir}/{-list|&|types}
 * Where dir is one of all, in, out and types is an ampersand-separated list of types. See the examples below for more information.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationContextConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
class RelationshipsIntegrationTest {
	static final String HTTP_URI = "http://localhost:7474/db/data/"
	@Resource
	Client jerseyClient
	def jsonSlurper = new JsonSlurper()

	static final String TEST_INITIAL = "/cypher/initial.json"
	static final String TEST_CLEANUP = "/cypher/cleanup.json"

	static Map<String, String> jsonTestScripts
	@Resource
	Neo4jRestJsonConverter converter

	String testWithRelationshipBookNodeUri
	String testWithRelationshipPersonNodeUri
	String testWithOutRelationshipBookNodeUri
	String testWithOutRelationshipPersonNodeUri

	@BeforeClass
	static void setUp() {
		jsonTestScripts = getJsonScripts(TEST_INITIAL,
				TEST_CLEANUP)
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
		initialRelationshipsTestData()
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
	void testGetAllRelationships() {
		WebResource webResource = jerseyClient.resource(testWithRelationshipBookNodeUri)
				.path("/relationships/all")
		ClientResponse response =  webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class)
		String respStr = getResponsePayload(response)
		log.info "testGetAllRelationships response :{} "+respStr
	}

	void initialRelationshipsTestData(){
		String body = "{\"query\":\"MATCH (b:Book)<-[:AuthorOf]-(p) WHERE b.title = 'Things Fall Apart' RETURN b,p\"}"
		WebResource webResource = jerseyClient.resource(HTTP_URI)
				.path("cypher")
		ClientResponse response =  webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, body)
		String respStr = getResponsePayload(response)
		Map resultMap = converter.getDataFromResponse(respStr)
		List<?> list = new ArrayList(resultMap.keySet())
		testWithRelationshipBookNodeUri = list.get(0)
		testWithRelationshipPersonNodeUri = list.get(1)

		body = "{\"query\":\"MATCH (b:Book) WHERE b.title = 'Don Quixote' RETURN b\"}"
		webResource = jerseyClient.resource(HTTP_URI)
				.path("cypher")
		response =  webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, body)
		respStr = getResponsePayload(response)
		resultMap = converter.getDataFromResponse(respStr)
		testWithOutRelationshipBookNodeUri = resultMap.keySet().iterator().next()

		body = "{\"query\":\"MATCH (p:Person) WHERE p.name = 'Miguel de Cervantes' RETURN p\"}"
		webResource = jerseyClient.resource(HTTP_URI)
				.path("cypher")
		response =  webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, body)
		respStr = getResponsePayload(response)
		resultMap = converter.getDataFromResponse(respStr)
		testWithOutRelationshipPersonNodeUri = resultMap.keySet().iterator().next()
	}
}
