package nz.co.neo4j.sample.bookshop.data.model;

public interface User {

	String getUsername();

	boolean passwordMatch(String password);

	void vote(Book book, int score);
}
