package nz.co.neo4j.sample.migration.bookshop.data

import groovy.transform.ToString

@ToString(includeNames = true, includeFields=true)
class Publication {
	int year
	@Delegate
	Publisher publisher = new Publisher()
}
