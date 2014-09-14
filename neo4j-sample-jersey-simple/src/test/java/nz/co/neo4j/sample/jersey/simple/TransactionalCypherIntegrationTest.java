package nz.co.neo4j.sample.jersey.simple;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.ws.rs.core.MediaType;

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
public class TransactionalCypherIntegrationTest {
	private static final String HTTP_URI = "http://localhost:7474/db/data/";

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TransactionalCypherIntegrationTest.class);

	private static final String TEST_TRANS_CREATE_NODES = "/test.json";

	@Autowired
	private Client jerseyClient;

	@Test
	public void testTransactionalCreateNodes() throws Exception {
		final URL url = TransactionalCypherIntegrationTest.class
				.getResource(TEST_TRANS_CREATE_NODES);
		final Path resPath = Paths.get(url.toURI());
		final String jsonBody = new String(
				java.nio.file.Files.readAllBytes(resPath), "UTF8");
		LOGGER.info("jsonBody: {} ", jsonBody);
		// begin
		WebResource webResource = jerseyClient.resource(HTTP_URI).path(
				"transaction");
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, jsonBody);
		String respStr = JerseyClientUtil.getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

	}

}
