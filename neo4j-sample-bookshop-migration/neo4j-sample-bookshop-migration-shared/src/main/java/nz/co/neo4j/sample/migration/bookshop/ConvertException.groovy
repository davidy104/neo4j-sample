package nz.co.neo4j.sample.migration.bookshop

class ConvertException extends Exception {

	public ConvertException() {
	}

	public ConvertException(String message, Throwable cause) {
		super(message, cause)
	}

	public ConvertException(String message) {
		super(message)
	}
}
