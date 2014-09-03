package nz.co.neo4j.sample.jersey.simple;

import javax.ws.rs.core.MediaType;

import nz.co.neo4j.sample.jersey.simple.config.ApplicationContextConfiguration;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationContextConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class Neo4jRestCRUDIntegrationTest {

	private static final String HTTP_URI = "http://localhost:7474/db/data/";
	private static final String CYPHER_PATH = "cypher";

	private static final Logger LOGGER = LoggerFactory
			.getLogger(Neo4jRestCRUDIntegrationTest.class);

	@Autowired
	private Client jerseyClient;

	@After
	public void cleanUp() throws Exception {
//		this.deleteTestNode();
	}

	@Test
	public void testCypherQueriesCreateNode() throws Exception {
		String jsonBody = "{\"query\" : \"CREATE (u:User { name : {name}, surname: {surname} }) RETURN u\",\"params\" : {\"name\" : \"John\", \"surname\" : \"Doe\"}}";

		WebResource webResource = jerseyClient.resource(HTTP_URI).path(
				CYPHER_PATH);
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, jsonBody);
		String respStr = JerseyClientUtil.getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);
	}

	@Test
	public void deleteTestNode() throws Exception {
		String jsonBody = "{\"query\" : \"MATCH (u:User { name : {name}, surname: {surname} }) DELETE u\",\"params\" : {\"name\" : \"John\", \"surname\" : \"Doe\"}}";
		WebResource webResource = jerseyClient.resource(HTTP_URI).path(
				CYPHER_PATH);
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, jsonBody);

		String respStr = JerseyClientUtil.getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);
	}
}
