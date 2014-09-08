package nz.co.neo4j.sample.migration.bookshop


class NotFoundException extends Exception {

	public NotFoundException() {
		super()
	}


	public NotFoundException(String message, Throwable cause) {
		super(message, cause)
	}

	public NotFoundException(String message) {
		super(message)
	}
}
