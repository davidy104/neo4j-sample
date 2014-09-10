package nz.co.neo4j.sample.migration.bookshop;

import java.util.Date;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import nz.co.neo4j.sample.migration.bookshop.data.entity.UserEntity;
import nz.co.neo4j.sample.migration.bookshop.data.repository.UserRepository;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestContextConfiguration.class })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class UserRepositorySlowTest {

	@Resource
	private UserRepository userRepository;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserRepositorySlowTest.class);

	@BeforeClass
	@Transactional
	public void setUp() {

	}

	@After
	@Transactional
	public void cleanUp() {
		userRepository.deleteAll();
	}

	@Test
	@Transactional
	public void testCreate() {
		UserEntity user = UserEntity.getBuilder("testUser1", "123", new Date())
				.build();
		userRepository.save(user);
		LOGGER.info("user:{} ", user);
	}

}
