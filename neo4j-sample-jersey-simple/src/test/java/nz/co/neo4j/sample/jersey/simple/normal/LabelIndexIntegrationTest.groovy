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

import org.junit.After;
import org.junit.Before
import org.junit.Test
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode
import org.springframework.test.context.ContextConfiguration

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.ClientResponse
import com.sun.jersey.api.client.WebResource
@ContextConfiguration(classes = ApplicationContextConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
class LabelIndexIntegrationTest {
	static final String HTTP_URI = "http://localhost:7474/db/data/"
	@Resource
	Client jerseyClient
	def jsonSlurper = new JsonSlurper()
	def testNodes = []

	@Before
	void intial(){
		//create node
		WebResource webResource = jerseyClient.resource(HTTP_URI).path(
				"node")
		String propertiesJson = "{\"name\" : \"David Yuan\"}"
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
	void testCreateIndexOnLabel() {

	}
}
