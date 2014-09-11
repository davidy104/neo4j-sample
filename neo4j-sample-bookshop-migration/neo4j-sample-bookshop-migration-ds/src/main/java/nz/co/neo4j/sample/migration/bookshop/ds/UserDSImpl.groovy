package nz.co.neo4j.sample.migration.bookshop.ds

import static nz.co.neo4j.sample.migration.bookshop.data.entity.QUserEntity.userEntity
import groovy.util.logging.Slf4j

import javax.annotation.Resource

import nz.co.neo4j.sample.migration.bookshop.DuplicatedException
import nz.co.neo4j.sample.migration.bookshop.NotFoundException
import nz.co.neo4j.sample.migration.bookshop.data.User
import nz.co.neo4j.sample.migration.bookshop.data.entity.UserEntity
import nz.co.neo4j.sample.migration.bookshop.data.repository.UserRepository

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.mysema.query.types.Predicate

@Service
@Slf4j
@Transactional(readOnly = true)
class UserDSImpl implements UserDS{

	@Resource
	UserRepository userRepository

	@Override
	@Transactional
	User createUser(final String userName,final String password) {
		log.debug "createUser start: {} $userName, $password"
		User created
		UserEntity userEntity
		try {
			userEntity = userRepository.findOne(userEntity.userName.eq(userName))
		} catch (e) {
			throw new DuplicatedException(e)
		}
		if(userEntity){
			throw new DuplicatedException('User[$userName] existed')
		}
		userEntity = UserEntity.getBuilder(userName, password, new Date()).built();
		userRepository.save(userEntity);
		created = new User(userId:userEntity.userId,userName:userEntity.userName)
		log.debug "createUser end: {} $created"
		return created
	}

	@Override
	public User loginUser(final String userName,final String password) {

		return null
	}

	@Override
	public User getUser(final String userName) throws NotFoundException {
		return null
	}

	@Override
	@Transactional
	public User updateUser(Long userId, User updatedUser)
	throws NotFoundException {

		return null
	}

	@Override
	@Transactional
	public void deleteUser(Long userId) throws NotFoundException {
	}
}
