package nz.co.neo4j.sample.migration.bookshop.data

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["userId"])
class User {
	Long userId
	String userName
	String password
}
