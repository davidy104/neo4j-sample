package nz.co.neo4j.sample.migration.bookshop.ds

import nz.co.neo4j.sample.migration.bookshop.data.User

interface UserDS {
	User createUser(String username, String password)

}
