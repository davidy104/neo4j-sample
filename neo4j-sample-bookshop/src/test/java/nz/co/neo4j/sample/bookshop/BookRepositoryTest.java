package nz.co.neo4j.sample.bookshop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.Resource;

import nz.co.neo4j.sample.bookshop.data.BookRepository;
import nz.co.neo4j.sample.bookshop.data.model.Book;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Ignore("no need to run all the time")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplicationContextConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class BookRepositoryTest {

	@Resource
	private GraphDatabaseService graphDatabaseService;

	@Resource
	private BookRepository bookRepository;

	@Resource
	private ExecutionEngine executionEngine;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BookRepositoryTest.class);

	@Before
	public void setUp() throws Exception {
		// clearAll(DB_PATH);
		try (Transaction tx = graphDatabaseService.beginTx()) {
			final URL url = BookRepositoryTest.class
					.getResource("/books_create.txt");
			final Path resPath = Paths.get(url.toURI());

			final String script = new String(
					java.nio.file.Files.readAllBytes(resPath), "UTF8");
			executionEngine.execute(script);
			executionEngine.execute("MATCH (b:Book) WITH b "
					+ "CREATE (b) <-[:Votes{score: 4 }]- (:User)");
			tx.success();
		}
	}

	@After
	public void tearDown() throws Exception {
		if (graphDatabaseService != null) {
			graphDatabaseService.shutdown();
			graphDatabaseService = null;
		}
	}

	@Test
	public void testFindShouldReturnCorrectNumberWhenInvoked() {
		try (final Transaction t = graphDatabaseService.beginTx()) {
			final List<Book> books = bookRepository.find("tales", 10, 0);
			assertNotNull(books);
			assertEquals(3, books.size());
			t.success();
		}
	}

	@Test
	public void testFindByYearShouldReturnCorrectNumberWhenInvoked() {
		try (final Transaction t = graphDatabaseService.beginTx()) {
			final List<Book> books = bookRepository.findByTitleAfterYear(
					"Memcached", 2013);
			assertNotNull(books);
			assertEquals(1, books.size());
			t.success();
		}
	}

	@Test
	public void testFindByTagShouldReturnCorrectNumberWhenInvoked() {
		try (final Transaction t = graphDatabaseService.beginTx()) {
			final List<Book> books = bookRepository.findByTag("drama");
			assertNotNull(books);
			assertEquals(44, books.size());
			t.success();
		}
	}

	@Test
	public void testFindByTagsShouldReturnCorrectNumberWhenInvoked() {
		try (final Transaction t = graphDatabaseService.beginTx()) {
			final List<Book> books = bookRepository
					.findByTags("neo4j", "nosql");
			assertNotNull(books);
			assertEquals(2, books.size());
			t.success();
		}
	}

	@Test
	public void testFindExactTitle() {
		try (final Transaction t = graphDatabaseService.beginTx()) {
			final List<Book> books = bookRepository
					.findExactTitle("War and Peace");
			assertNotNull(books);
			assertEquals(1, books.size());
			t.success();
		}
	}

	@Test
	public void testScoreValues() {
		try (final Transaction t = graphDatabaseService.beginTx()) {
			final List<Book> books = bookRepository.find("Learning", 1, 0);
			assertNotNull(books);
			for (Book b : books) {
				Double score = bookRepository.getScore(b);
				// assertEquals(4.0, score);
			}
			t.success();
		}
	}

}
