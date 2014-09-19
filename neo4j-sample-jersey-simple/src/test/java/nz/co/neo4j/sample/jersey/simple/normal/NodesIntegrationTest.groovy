package nz.co.neo4j.sample.jersey.simple.normal;

import static nz.co.neo4j.sample.jersey.simple.JerseyClientUtil.getResponsePayload
import static nz.co.neo4j.sample.jersey.simple.TestUtil.getJsonScripts
import static org.junit.Assert.*
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

import javax.annotation.Resource
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response.Status

import nz.co.neo4j.sample.jersey.simple.config.ApplicationContextConfiguration

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
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
class NodesIntegrationTest {
	static final String HTTP_URI = "http://localhost:7474/db/data/"

	static Map<String, String> jsonTestScripts
	@Resource
	Client jerseyClient

	def jsonSlurper = new JsonSlurper()

	def testNodes = []


	@Before
	void intial() {
		WebResource webResource = jerseyClient.resource(HTTP_URI).path(
				"node")
		String propertiesJson = "{\"name\" : \"Fyodor Dostoevsky\"}"
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, propertiesJson)

		if (response.getStatusInfo().statusCode != Status.CREATED.code) {
			throw new RuntimeException('Create node failed.')
		}
		String respStr = getResponsePayload(response)
		Map jsonResult = (Map) jsonSlurper.parseText(respStr)
		String createdNodeUri = jsonResult.get('self')
		log.info "createdNodeUri: {} $createdNodeUri"
		testNodes.add(createdNodeUri)
	}

	@After
	void tearDown(){
		testNodes.each {
			WebResource webResource = jerseyClient.resource(it)
			ClientResponse response = webResource
					.accept(MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON)
					.delete(ClientResponse.class)
			if (response.getStatusInfo().statusCode == Status.CONFLICT.code) {
				throw new RuntimeException('Relationship conflict.')
			} else if(response.getStatusInfo().statusCode != Status.NO_CONTENT.code){
				throw new RuntimeException('Unknown error.')
			}
		}
	}


	@Test
	void testGetNode() {
		String createdNodeUri = ((ArrayList)testNodes).get(0)
		WebResource webResource = jerseyClient.resource(createdNodeUri)
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class)
		String respStr = getResponsePayload(response)
		log.info "response: {} $respStr"
	}

	@Test
	void testGetNonExistentNode(){
		String expectedErrorMessage = "Cannot find node with id [99999999999999999] in database.";
		String expectedException ="NodeNotFoundException"
		WebResource webResource = jerseyClient.resource(HTTP_URI).path(
				"node").path("/99999999999999999")
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class)
		String respStr = getResponsePayload(response)
		log.info "response: {} $respStr"
		assertEquals(response.getStatusInfo().statusCode,
				Status.NOT_FOUND.code)

		Map jsonResult = (Map) jsonSlurper.parseText(respStr)
		String exception = jsonResult.get('exception')
		String errorMessage = jsonResult.get('message')
		assertEquals(expectedErrorMessage,errorMessage)
		assertEquals(expectedException,exception)
	}
	
	@Test
	void testAddingLabelToNode(){
		String label = "[\"Person\"]"
		String createdNodeUri = ((ArrayList)testNodes).get(0)
		WebResource webResource = jerseyClient.resource(createdNodeUri).path("/labels")
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class,label)
		String respStr = getResponsePayload(response)
		log.info "response: {} $respStr"
		assertEquals(response.getStatusInfo().statusCode,
			Status.NO_CONTENT.code)
		testListLabelsForNode()
		testGetAllNodesWithlabel()
	}
	
	@Test
	void testListLabelsForNode(){
		String createdNodeUri = ((ArrayList)testNodes).get(0)
		WebResource webResource = jerseyClient.resource(createdNodeUri).path("/labels")
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class)
		String respStr = getResponsePayload(response)
		log.info "response: {} $respStr"
	}
	
	@Test
	void testGetAllNodesWithlabel(){
		WebResource webResource = jerseyClient.resource(HTTP_URI).path("/label/Person/nodes")
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class)
		String respStr = getResponsePayload(response)
		log.info "response: {} $respStr"
	}
}
