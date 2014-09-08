package nz.co.neo4j.sample.migration.bookshop


public class DuplicatedException extends Exception {

	public DuplicatedException() {
		super()
	}


	public DuplicatedException(String message, Throwable cause) {
		super(message, cause)
	}

	public DuplicatedException(String message) {
		super(message)
	}
}
