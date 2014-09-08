package nz.co.neo4j.sample.migration.bookshop.data

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["personId"])
class Person {
	Long personId
	String lastName
	String firstName
	Date birthDate
	User user
}
