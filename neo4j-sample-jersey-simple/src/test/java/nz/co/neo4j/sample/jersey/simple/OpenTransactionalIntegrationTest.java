package nz.co.neo4j.sample.jersey.simple;

import java.net.URL;
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
public class OpenTransactionalIntegrationTest {
	private static final String HTTP_URI = "http://localhost:7474/db/data/";

	private static final Logger LOGGER = LoggerFactory
			.getLogger(OpenTransactionalIntegrationTest.class);

	private static final String TEST_TRANS_CREATE_NODES = "/opentx/transactionalNodeCreate.json";
	private static final String TEST_QUERY_RELATION = "/transactional/query-relation.json";
	private static final String TEST_DELETE_RELATION = "/transactional/delete-relation.json";

	@Autowired
	private Client jerseyClient;

	@Test
	public void test() throws Exception {
		URL url = OpenTransactionalIntegrationTest.class
				.getResource(TEST_TRANS_CREATE_NODES);
		Path resPath = Paths.get(url.toURI());
		String jsonBody = new String(java.nio.file.Files.readAllBytes(resPath),
				"UTF8");
		LOGGER.info("jsonBody: {} ", jsonBody);

		WebResource webResource = jerseyClient.resource(HTTP_URI).path(
				"transaction");
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, jsonBody);
		MultivaluedMap<String, String> headers = response.getHeaders();
		Set<Map.Entry<String, List<String>>> headerSet = headers.entrySet();
		for (Map.Entry<String, List<String>> mapentry : headerSet) {
			LOGGER.info("Key : " + mapentry.getKey() + " Value : "
					+ mapentry.getValue());
		}

		// String location = headers.get("Location Value").get(0);
		// LOGGER.info("location: {} ", location);
		String respStr = JerseyClientUtil.getResponsePayload(response);
		LOGGER.info("respStr: {} ", respStr);

		Map<String, Object> respMap = TestUtil.jsonToMap(respStr);
		for (Map.Entry<String, Object> entry : respMap.entrySet()) {
			LOGGER.info("Key : " + entry.getKey() + " Value : "
					+ entry.getValue());
		}

		String commitUri = (String) respMap.get("commit");
		LOGGER.info("commitUri: {} ", commitUri);
		this.commit(commitUri, null);
	}

	private void commit(final String commitUri, String content)
			throws Exception {
		WebResource webResource = jerseyClient.resource(commitUri);
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, content);
		String respStr = JerseyClientUtil.getResponsePayload(response);
		LOGGER.info("respStr: {} ", respStr);
	}

}
