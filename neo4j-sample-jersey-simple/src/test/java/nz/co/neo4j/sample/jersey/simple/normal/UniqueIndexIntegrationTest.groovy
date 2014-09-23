package nz.co.neo4j.sample.jersey.simple.normal;

import static nz.co.neo4j.sample.jersey.simple.JerseyClientUtil.getResponsePayload
import static org.junit.Assert.*
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

import java.nio.file.Path
import java.nio.file.Paths

import javax.annotation.Resource
import javax.ws.rs.core.MediaType

import nz.co.neo4j.sample.jersey.simple.config.ApplicationContextConfiguration

import org.junit.After
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
class UniqueIndexIntegrationTest {
	static final String HTTP_URI = "http://localhost:7474/db/data/"
	@Resource
	Client jerseyClient
	def jsonSlurper = new JsonSlurper()
	def testNodes = []

	static final String TEST_INITIAL = "/deleteAll-cypher.json"

	@After
	public void tearDown(){
		Path resPath = Paths.get(UniqueIndexIntegrationTest.class.getResource(TEST_INITIAL)
				.toURI())
		String deleteAll = new String(
				java.nio.file.Files.readAllBytes(resPath), "UTF8")
		WebResource webResource = jerseyClient.resource(HTTP_URI)
				.path("cypher")
		ClientResponse response =  webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, deleteAll)
		String respStr = getResponsePayload(response)
		log.debug 'deleteAll response: {} $respStr'
	}

	@Test
	public void testCreateAndGet() {
		String userName = 'dav'
		String password ='123'
		String json = "{\"key\" : \"userName\",\"value\" : \""+userName+"\",\"properties\" : {\"userName\" : \""+userName+"\",\"password\" : "+password+"}}";
		WebResource webResource = jerseyClient.resource(HTTP_URI).path(
				"index/node/user").queryParam("uniqueness", "create_or_fail")
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, json)
		String respStr = getResponsePayload(response)
		log.debug 'create unique index response: {} $respStr'
		String location =response.getHeaders().get('Location').get(0)
		log.debug 'location: {} $location'
		String id = location.substring(location.lastIndexOf('/')+1,location.length())
		log.debug 'id: {} $id'

		String label = "[\"User\"]"
		webResource = jerseyClient.resource(HTTP_URI).path("/node/").path(id).path("/labels")
		response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class,label)
		respStr = getResponsePayload(response)
		log.info "testAddingLabelToNode response: {} $respStr"

		webResource = jerseyClient.resource(HTTP_URI).path("/node/").path(id)
		response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class)
		respStr = getResponsePayload(response)
		log.info "testGetNode response: {} $respStr"

		webResource = jerseyClient.resource(HTTP_URI).path("/node/").path(id).path("/labels")
		response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class)
		respStr = getResponsePayload(response)
		log.info "testListLabelsForNode response: {} $respStr"
	}
}
