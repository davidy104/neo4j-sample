package nz.co.neo4j.sample.rest.simple;

import static org.neo4j.helpers.collection.MapUtil.map;

import javax.annotation.Resource;

import nz.co.neo4j.sample.rest.simple.config.ApplicationContextConfiguration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.Node;
import org.neo4j.rest.graphdb.RestAPI;
import org.neo4j.rest.graphdb.query.RestCypherQueryEngine;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationContextConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SimpleCypherQueryCRUDIntegrationTest {

	@Resource
	private RestAPI restAPI;

	private RestCypherQueryEngine engine;

	@Before
	public void setUp() throws Exception {
		engine = new RestCypherQueryEngine(restAPI);
	}

	@Test
	public void testCreateNode() {
		engine.query("create (n {name:{name}}) return n", map("name", "Foo"))
				.to(Node.class).singleOrNull();
	}

}
