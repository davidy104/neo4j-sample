package nz.co.neo4j.sample.jersey.simple;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import nz.co.neo4j.sample.jersey.simple.config.ApplicationContextConfiguration;

import org.junit.After;
import org.junit.Before;
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
public class SingleHttpReqTransactionalIntegrationTest {
	private static final String HTTP_URI = "http://localhost:7474/db/data/";

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SingleHttpReqTransactionalIntegrationTest.class);

	private static final String TEST_TRANS_CREATE_NODES = "/transactional/transactionalNodeCreate.json";
	private static final String TEST_TRANS_DELETE_NODES = "/transactional/transactionalNodeDelete.json";
	private static final String TEST_QUERY_RELATION = "/transactional/queryRelation.json";
	private static final String TEST_QUERY_PERSON = "/transactional/queryPerson.json";
	private static final String TEST_DELETE_RELATION = "/transactional/deleteRelation.json";

	private String createNodesJson;
	private String deleteNodesJson;
	private String queryRelationshipJson;
	private String queryPersonJson;
	private String deleteRelationshipJson;

	@Autowired
	private Client jerseyClient;

	@Before
	public void setUp() throws Exception {
		Path resPath = Paths.get(OpenTransactionalIntegrationTest.class
				.getResource(TEST_TRANS_CREATE_NODES).toURI());
		createNodesJson = new String(java.nio.file.Files.readAllBytes(resPath),
				"UTF8");
		resPath = Paths.get(OpenTransactionalIntegrationTest.class
				.getResource(TEST_TRANS_DELETE_NODES).toURI());
		deleteNodesJson = new String(java.nio.file.Files.readAllBytes(resPath),
				"UTF8");
		resPath = Paths.get(OpenTransactionalIntegrationTest.class
				.getResource(TEST_QUERY_RELATION).toURI());
		queryRelationshipJson = new String(
				java.nio.file.Files.readAllBytes(resPath), "UTF8");
		resPath = Paths.get(OpenTransactionalIntegrationTest.class
				.getResource(TEST_DELETE_RELATION).toURI());
		deleteRelationshipJson = new String(
				java.nio.file.Files.readAllBytes(resPath), "UTF8");
		resPath = Paths.get(OpenTransactionalIntegrationTest.class
				.getResource(TEST_QUERY_PERSON).toURI());
		queryPersonJson = new String(java.nio.file.Files.readAllBytes(resPath),
				"UTF8");
	}

	@After
	public void cleanUp() throws Exception {
		WebResource webResource = jerseyClient.resource(HTTP_URI)
				.path("cypher");
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, deleteRelationshipJson);

		String respStr = JerseyClientUtil.getResponsePayload(response);
		LOGGER.info("delete relation:{} ", respStr);
		// delete nodes
		webResource = jerseyClient.resource(HTTP_URI)
				.path("transaction/commit");
		response = webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, deleteNodesJson);
		respStr = JerseyClientUtil.getResponsePayload(response);
		LOGGER.info("delete nodes: {} ", respStr);
	}

	@Test
	public void testSingleHttpRequestTransaction() throws Exception {
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

		webResource = jerseyClient.resource(HTTP_URI).path("cypher");
		response = webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, queryRelationshipJson);
		respStr = JerseyClientUtil.getResponsePayload(response);
		LOGGER.info("relationship query: {} ", respStr);

		webResource = jerseyClient.resource(HTTP_URI).path("cypher");
		response = webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, queryPersonJson);
		respStr = JerseyClientUtil.getResponsePayload(response);
		LOGGER.info("person query: {} ", respStr);
	}

}
