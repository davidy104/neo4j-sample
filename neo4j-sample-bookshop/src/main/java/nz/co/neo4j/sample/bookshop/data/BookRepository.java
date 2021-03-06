package nz.co.neo4j.sample.bookshop.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import nz.co.neo4j.sample.bookshop.data.model.Book;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.Node;
import org.neo4j.helpers.collection.IteratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class BookRepository {

	private ExecutionEngine executionEngine;

	@Autowired
	public BookRepository(
			@Qualifier("executionEngine") ExecutionEngine executionEngine) {
		this.executionEngine = executionEngine;
	}

	public Book create() {
		final ExecutionResult result = executionEngine
				.execute("CREATE (b: Book) RETURN b");
		final Iterator<Node> iterator = result.columnAs("b");
		for (Node b : IteratorUtil.asIterable(iterator)) {
			final BookNode book = new BookNode(b);
			return book;
		}
		return null;
	}

	public List<Book> findByTags(String... tags) {
		final Map<String, Object> params = new HashMap<>();
		params.put("tags", tags);

		final ExecutionResult result = executionEngine
				.execute(
						"MATCH (b:Book) WHERE ALL( tag IN b.tags WHERE tag IN {tags} ) RETURN b",
						params);
		final List<Book> ret = new LinkedList<>();

		final Iterator<Node> iterator = result.columnAs("b");
		for (Node b : IteratorUtil.asIterable(iterator)) {
			final BookNode book = new BookNode(b);
			ret.add(book);
		}
		return ret;
	}

	public List<Book> findByTag(String tag) {
		final Map<String, Object> params = new HashMap<>();
		params.put("query", tag);

		final ExecutionResult result = executionEngine
				.execute(
						"MATCH (b:Book) WHERE ANY( tag IN b.tags WHERE tag = {query} ) RETURN b",
						params);
		final List<Book> ret = new LinkedList<>();

		final Iterator<Node> iterator = result.columnAs("b");
		for (Node b : IteratorUtil.asIterable(iterator)) {
			final BookNode book = new BookNode(b);
			ret.add(book);
		}
		return ret;
	}

	public List<Book> findByTitleAfterYear(String title, int year) {
		final Map<String, Object> params = new HashMap<>();
		params.put("query", "(?i).*" + Pattern.quote(title) + ".*");
		params.put("year", year);

		final ExecutionResult result = executionEngine
				.execute(
						"MATCH (b:Book) -[r:PublishedBy]-> (:Publisher) WHERE r.year >= {year} AND b.title =~ {query} RETURN b",
						params);
		final List<Book> ret = new LinkedList<>();

		final Iterator<Node> iterator = result.columnAs("b");
		for (Node b : IteratorUtil.asIterable(iterator)) {
			final BookNode book = new BookNode(b);
			ret.add(book);
		}
		return ret;
	}

	public List<Book> findExactTitle(String title) {
		final Map<String, Object> params = new HashMap<>();
		params.put("title", title);

		final ExecutionResult result = executionEngine.execute(
				"MATCH (b:Book) WHERE b.title = {title} RETURN b", params);
		final List<Book> ret = new LinkedList<>();

		final Iterator<Node> iterator = result.columnAs("b");
		for (Node b : IteratorUtil.asIterable(iterator)) {
			final BookNode book = new BookNode(b);
			ret.add(book);
		}
		return ret;
	}

	public List<Book> find(String text, int limit, int skip) {
		final String query = "(?i).*" + Pattern.quote(text) + ".*";

		final Map<String, Object> params = new HashMap<>();
		params.put("query", query);
		params.put("limit", limit);
		params.put("skip", skip);

		final ExecutionResult result = executionEngine
				.execute(
						"MATCH (b:Book) WHERE b.title =~ {query} RETURN b SKIP {skip} LIMIT {limit}",
						params);
		final List<Book> ret = new LinkedList<>();

		final Iterator<Node> iterator = result.columnAs("b");
		for (Node b : IteratorUtil.asIterable(iterator)) {
			final BookNode book = new BookNode(b);
			ret.add(book);
		}
		return ret;
	}

	public Double getScore(Book book) {
		final Map<String, Object> params = new HashMap<>();

		final BookNode bn = (BookNode) book;
		params.put("id", bn.getId());

		final ExecutionResult result = executionEngine
				.execute(
						"START b=node({id}) MATCH (b) <-[r:Votes]- (u:User) RETURN avg(r.score) as a",
						params);

		final Iterator<Double> iterator = result.columnAs("a");
		for (Double avg : IteratorUtil.asIterable(iterator)) {
			return avg;
		}
		return null;
	}
}
