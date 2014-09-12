package nz.co.neo4j.sample.migration.bookshop.ds

import nz.co.neo4j.sample.migration.bookshop.NotFoundException
import nz.co.neo4j.sample.migration.bookshop.data.User

interface UserDS {
	User createUser(String userName, String password)
	User loginUser(String userName, String password)
	User getUser(String userName) throws NotFoundException
	User updateUser(Long userId,User updatedUser) throws NotFoundException
	User deleteUser(Long userId) throws NotFoundException
}
