package nz.co.neo4j.sample.jersey.simple;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import nz.co.neo4j.sample.jersey.simple.config.ApplicationContextConfiguration;

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
public class CypherIntegrationTest {
	private static final String HTTP_URI = "http://localhost:7474/db/data/";

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CypherIntegrationTest.class);

	private static final String TEST_CREATENODE = "/cypher/createNodes.json";
	private static final String TEST_QUERY_PERSON = "/cypher/queryPerson.json";
	private static final String TEST_DELETE = "/cypher/deleteNode.json";
	@Autowired
	private Client jerseyClient;

	@Test
	public void testTransactionalCreateNode() throws Exception {
		Path resPath = Paths.get(OpenTransactionalIntegrationTest.class
				.getResource(TEST_CREATENODE).toURI());
		String createNodesJson = new String(
				java.nio.file.Files.readAllBytes(resPath), "UTF8");

		WebResource webResource = jerseyClient.resource(HTTP_URI).path(
				"transaction/commit");
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, createNodesJson);
		MultivaluedMap<String, String> headers = response.getHeaders();
		Set<Map.Entry<String, List<String>>> headerSet = headers.entrySet();
		for (Map.Entry<String, List<String>> mapentry : headerSet) {
			LOGGER.info("Header Key : " + mapentry.getKey()
					+ "-Header Value : " + mapentry.getValue());
		}

		// String location = headers.get("Location Value").get(0);
		// LOGGER.info("location: {} ", location);
		String respStr = JerseyClientUtil.getResponsePayload(response);
		LOGGER.info("transactional create: {} ", respStr);

		Map<String, Object> respMap = TestUtil.jsonToMap(respStr);
		for (Map.Entry<String, Object> entry : respMap.entrySet()) {
			LOGGER.info("Content Key : " + entry.getKey() + "Content Value : "
					+ entry.getValue());
		}
		testQuery();
	}

	@Test
	public void testQuery() throws Exception {
		Path resPath = Paths.get(OpenTransactionalIntegrationTest.class
				.getResource(TEST_QUERY_PERSON).toURI());
		String queryPersonJson = new String(
				java.nio.file.Files.readAllBytes(resPath), "UTF8");

		WebResource webResource = jerseyClient.resource(HTTP_URI)
				.path("cypher");
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, queryPersonJson);
		String respStr = JerseyClientUtil.getResponsePayload(response);
		LOGGER.info("person query: {} ", respStr);

	}

	@Test
	public void testDelete() throws Exception {
		Path resPath = Paths.get(OpenTransactionalIntegrationTest.class
				.getResource(TEST_DELETE).toURI());
		String deleteJson = new String(
				java.nio.file.Files.readAllBytes(resPath), "UTF8");

		WebResource webResource = jerseyClient.resource(HTTP_URI).path(
				"transaction/commit");
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, deleteJson);
		String respStr = JerseyClientUtil.getResponsePayload(response);
		LOGGER.info("delete: {} ", respStr);
	}
}
