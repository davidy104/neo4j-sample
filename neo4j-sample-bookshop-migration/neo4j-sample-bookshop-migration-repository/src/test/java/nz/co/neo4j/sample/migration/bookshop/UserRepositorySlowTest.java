package nz.co.neo4j.sample.migration.bookshop;

import static nz.co.neo4j.sample.migration.bookshop.data.entity.QUserEntity.userEntity;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import nz.co.neo4j.sample.migration.bookshop.data.entity.UserEntity;
import nz.co.neo4j.sample.migration.bookshop.data.repository.UserRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.types.Projections;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestContextConfiguration.class })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class UserRepositorySlowTest {

	@Resource
	private UserRepository userRepository;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserRepositorySlowTest.class);

	@Before
	public void setUp() throws Exception {
		userRepository.save(RepositoryTestUtil.userlist());
	}

	@After
	public void cleanUp() {
		userRepository.deleteAll();
	}

	@Test
	public void testFindAll() {
		List<UserEntity> users = userRepository.findAll();
		LOGGER.info("users size:{} ", users.size());
	}

	@Test
	public void testFindByName() {
		UserEntity found = userRepository
				.findOne(userEntity.userName.eq("dav"));
		assertNotNull(found);
		LOGGER.info("found:{} ", found);
	}

	@Test
	public void testFindOneWithSelectedFields() {
		UserEntity found = userRepository.findOne(Projections.bean(
				UserEntity.class, userEntity.userId, userEntity.userName),
				userEntity.userName.eq("dav"));
		assertNotNull(found);
		assertNull(found.getCreateDate());
		LOGGER.info("found:{} ", found);
	}

	@Test
	public void testPaging() {
		Pageable pageSpecification = new PageRequest(0, 5, new Sort(
				Sort.Direction.ASC, "createDate"));
		Page<UserEntity> page = userRepository.findAll(Projections.bean(
				UserEntity.class, userEntity.userId, userEntity.createDate,
				userEntity.userName), userEntity.userName.eq("dav"),
				pageSpecification);
		int pageSize = page.getSize();
		int pageNumber = page.getNumber();
		List<UserEntity> users = page.getContent();
		LOGGER.info("pageSize:{} ", pageSize);
		LOGGER.info("pageNumber:{} ", pageNumber);
		LOGGER.info("firstUser:{} ", users.get(0));
	}

	@Test
	public void testCreate() {
		UserEntity user = UserEntity.getBuilder("testUser1", "123", new Date())
				.build();
		userRepository.save(user);
		assertNotNull(user);
		LOGGER.info("user:{} ", user);
	}

	@Test
	@Transactional
	public void testDeleteById() throws Exception {
		UserEntity deleted = userRepository.deleteById(1L);
		assertNotNull(deleted);
		LOGGER.info("deleted:{} ", deleted);
		try {
			deleted = userRepository.getOne(1L);
			LOGGER.info("found:{} ", deleted);
		} catch (Exception e) {
			assertNotNull(e);
			e.printStackTrace();
		}
	}

	@Test(expected = NotFoundException.class)
	public void testDeleteNotfound() throws Exception {
		userRepository.deleteById(100L);
	}

}
