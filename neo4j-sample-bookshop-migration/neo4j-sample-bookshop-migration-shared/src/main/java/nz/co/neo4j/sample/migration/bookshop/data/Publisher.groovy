package nz.co.neo4j.sample.migration.bookshop.data

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["publisherId"])
class Publisher {
	Long publisherId
	String name
	Date createTime
}
