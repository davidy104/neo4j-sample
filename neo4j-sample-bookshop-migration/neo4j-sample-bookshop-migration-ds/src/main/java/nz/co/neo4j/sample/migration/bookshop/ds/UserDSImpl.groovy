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
		UserEntity addUserEntity
		try {
			Predicate predicate = userEntity.userName.eq(userName)
			addUserEntity = userRepository.findOne(predicate)
		} catch (e) {
			throw new DuplicatedException(e)
		}
		if(addUserEntity){
			throw new DuplicatedException('User[$userName] existed')
		}
		addUserEntity = new UserEntity(userName:userName,password:password,createDate:new Date())
		userRepository.save(addUserEntity);
		created = new User(userId:addUserEntity.userId,userName:addUserEntity.userName,createDate:addUserEntity.createDate)
		log.debug "createUser end: {} $created"
		return created
	}

	@Override
	public User loginUser(final String userName,final String password) {
		return null
	}

	@Override
	public User getUser(final String userName) throws NotFoundException {
		UserEntity foundEntity
		Predicate predicate = userEntity.userName.eq(userName)
		foundEntity = userRepository.findOne(predicate)
		if(!foundEntity){
			throw new NotFoundException('User not found by name[$userName]')
		}
		return new User(userId:foundEntity.userId,userName:foundEntity.userName,createDate:foundEntity.createDate)
	}

	@Override
	@Transactional
	User updateUser(Long userId, User updatedUser)
	throws NotFoundException {
		UserEntity foundEntity
		foundEntity = userRepository.findOne(userId)
		if(!foundEntity){
			throw new NotFoundException('User not found by userId[$userId]')
		}
		foundEntity.userName = updatedUser.userName
		if(updatedUser.password){
			foundEntity.password = updatedUser.password
		}
		if(updatedUser.createDate){
			foundEntity.createDate = updatedUser.createDate
		}
		userRepository.save(foundEntity)
		return new User(userId:foundEntity.userId,userName:foundEntity.userName,createDate:foundEntity.createDate)
	}

	@Override
	@Transactional
	void deleteUser(Long userId) throws NotFoundException {
		
	}
}
