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
	@Resource
	Client jerseyClient
	def jsonSlurper = new JsonSlurper()
	def testNodes = []

	@Before
	void intial() {
		//create node without label
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

		//create another node
		webResource = jerseyClient.resource(HTTP_URI).path(
				"node")
		propertiesJson = "{\"name\" : \"David Yuan\"}"
		response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, propertiesJson)

		if (response.getStatusInfo().statusCode != Status.CREATED.code) {
			throw new RuntimeException('Create node failed.')
		}
		respStr = getResponsePayload(response)
		jsonResult = (Map) jsonSlurper.parseText(respStr)
		createdNodeUri = jsonResult.get('self')
		log.info "createdNodeUri: {} $createdNodeUri"
		testNodes.add(createdNodeUri)
		//add label Person for previous Node created
		String label = "[\"Person\"]"
		webResource = jerseyClient.resource(createdNodeUri).path("/labels")
		response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class,label)
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
		log.info "testGetNode response: {} $respStr"
		Map mapResult = (Map) jsonSlurper.parseText(respStr)
		String self = mapResult.get('self')
		String id = self.substring(self.lastIndexOf('/')+1,self.length())
		log.info "id: {} $id"
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
		log.info "testAddingLabelToNode response: {} $respStr"
		assertEquals(response.getStatusInfo().statusCode,
				Status.NO_CONTENT.code)
	}

	@Test
	void testListLabelsForNode(){
		String createdNodeUri = ((ArrayList)testNodes).get(1)
		WebResource webResource = jerseyClient.resource(createdNodeUri).path("/labels")
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class)
		String respStr = getResponsePayload(response)
		log.info "testListLabelsForNode response: {} $respStr"
	}

	@Test
	void testGetAllNodesWithlabel(){
		WebResource webResource = jerseyClient.resource(HTTP_URI).path("/label/Person/nodes")
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class)
		String respStr = getResponsePayload(response)
		log.info "testGetAllNodesWithlabel response: {} $respStr"
	}

	@Test
	void testReplacingLabelsOnNode(){
		String jsonBody ="[ \"Actor\", \"Director\" ]";
		String createdNodeUri = ((ArrayList)testNodes).get(1)
		WebResource webResource = jerseyClient.resource(createdNodeUri).path("/labels")
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.put(ClientResponse.class,jsonBody)
		String respStr = getResponsePayload(response)
		log.info "testReplacingLabelsOnNode response: {} $respStr"
		testListLabelsForNode()
	}

	@Test
	void testRemoveLabelOnNode(){
		//DELETE http://localhost:7474/db/data/node/286/labels/Person
		String createdNodeUri = ((ArrayList)testNodes).get(1)
		WebResource webResource = jerseyClient.resource(createdNodeUri).path("/labels/Person")
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.delete(ClientResponse.class)
		String respStr = getResponsePayload(response)
		log.info "testRemoveLabelOnNode response: {} $respStr"
		assertEquals(response.getStatusInfo().statusCode,
				Status.NO_CONTENT.code)
	}
}
