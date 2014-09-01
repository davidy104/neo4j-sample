package nz.co.neo4j.sample.bookshop;

import static nz.co.neo4j.sample.bookshop.config.ApplicationContextConfiguration.DB_PATH;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.Resource;

import nz.co.neo4j.sample.bookshop.config.ApplicationContextConfiguration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.impl.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationContextConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DumpQueriesTest {

	@Resource
	private GraphDatabaseService graphDatabaseService;

	@Resource
	private ExecutionEngine executionEngine;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DumpQueriesTest.class);

	@Before
	public void setUp() throws Exception {
		try (Transaction tx = graphDatabaseService.beginTx()) {
			final URL url = BookRepositoryTest.class
					.getResource("/books_create.txt");
			final Path resPath = Paths.get(url.toURI());

			final String script = new String(
					java.nio.file.Files.readAllBytes(resPath), "UTF8");
			executionEngine.execute(script);
			final String randomVotes = "MATCH (b:Book) WITH b "
					+ "CREATE (b) <-[:Votes{score: round(rand()*3) + 2 }]- (u1:User)"
					+ "CREATE (b) <-[:Votes{score: round(rand()*3) + 2 }]- (u2:User)"
					+ "CREATE (b) <-[:Votes{score: round(rand()*4) + 1 }]- (u3:User)"
					+ "CREATE (b) <-[:Votes{score: round(rand()*3) + 1 }]- (u4:User)"
					+ "CREATE (b) <-[:Votes{score: round(rand()*4) + 1 }]- (u5:User)";
			executionEngine.execute(randomVotes);

			final String bookWithoutVotes = "CREATE (:Book {title:\"The Art of Prolog\",tags:[\"prolog\"]})";
			executionEngine.execute(bookWithoutVotes);
			tx.success();
		}
	}

	@After
	public void tearDown() throws Exception {
		if (graphDatabaseService != null) {
			graphDatabaseService.shutdown();
			graphDatabaseService = null;
		}

		final File dbDirectory = new File(DB_PATH);
		final boolean exists = dbDirectory.exists();
		if (exists) {
			FileUtils.deleteRecursively(dbDirectory);
		}
	}

	@Test
	public void testTextSearch() {
		dump("MATCH (b:Book { title: 'In Search of Lost Time' }) RETURN b");
		dump("MATCH (b:Book) WHERE b.title = 'In Search of Lost Time' RETURN b");
	}

	@Test
	public void testRegularExpressions() {
		dump("MATCH (b:Book) WHERE b.title =~ '.*Lost.*' RETURN b");
		dump("MATCH (b:Book) WHERE b.title =~ '^Henry.*' RETURN b");
		dump("MATCH (b:Book) WHERE b.title =~ '.*[Tt]ale(s)?.*' RETURN b");
	}

	@Test
	public void testValueComparisons() {
		dump("MATCH (book:Book) -[r:PublishedBy]-> (:Publisher) WHERE r.year >= 2012 RETURN book.title, r.year");
	}

	@Test
	public void testInPredicate() {
		dump("MATCH (b:Book) -[r:PublishedBy]-> (:Publisher) WHERE r.year IN [2012, 2013] RETURN b.title, r.year");
		dump("MATCH (b:Book) WHERE ANY ( tag IN b.tags WHERE tag IN ['nosql','neo4j'] ) RETURN b.title,b.tags");
		dump("MATCH (b:Book) WHERE ALL ( tag IN b.tags WHERE tag IN ['nosql','neo4j'] ) RETURN b.title,b.tags");
		dump("MATCH (b:Book) WHERE ANY ( tag IN b.tags WHERE tag = 'nosql' ) "
				+ "AND NONE (tag in b.tags WHERE tag = 'neo4j') RETURN b.title,b.tags");
		dump("MATCH (b:Book) WHERE SINGLE (tag IN b.tags WHERE tag IN ['ruby', 'mongodb'] ) RETURN b.title,b.tags");
	}

	@Test
	public void testPaging() {
		// LIMIT and SKIP
		dump("MATCH (b:Book) WHERE b.title =~ '(?i).*drama.*' RETURN b LIMIT 20");
		dump("MATCH (b:Book) WHERE b.title =~ '(?i).*drama.*' RETURN b SKIP 20 LIMIT 20");
	}

	@Test
	public void testSorting() {
		dump("MATCH (b:Book) WHERE ANY ( tag IN b.tags WHERE tag IN ['drama'] ) RETURN b.title ORDER BY b.title LIMIT 5");
		dump("MATCH (b:Book) WHERE ANY ( tag IN b.tags WHERE tag IN ['drama'] ) RETURN b.title ORDER BY b.title DESC LIMIT 5");
	}

	@Test
	public void testCoalesceFunction() {
		dump("MATCH (b:Book) OPTIONAL MATCH (b) -[p:PublishedBy]-> (c) RETURN b.title, p.year ORDER BY p.year DESC LIMIT 5");
		dump("MATCH (b:Book) OPTIONAL MATCH (b) -[p:PublishedBy]-> (c) RETURN b.title, p.year ORDER BY COALESCE(p.year,-5000) DESC LIMIT 5");
	}

	private void dump(final String query) {
		LOGGER.info(query);
		LOGGER.info(executionEngine.execute(query).dumpToString());
	}
}
