package nz.co.neo4j.sample.migration.bookshop;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import nz.co.neo4j.sample.migration.bookshop.data.entity.UserEntity;
import nz.co.neo4j.sample.migration.bookshop.data.repository.BaseRepositoryFactoryBean;
import nz.co.neo4j.sample.migration.bookshop.data.repository.GenericBaseRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestContextConfiguration.class })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class GenericRepositorySlowTest {

	@PersistenceContext
	private EntityManager entityManager;

	private GenericBaseRepository<UserEntity, Long> userRepository;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(GenericRepositorySlowTest.class);

	private BaseRepositoryFactoryBean repositoryFactoryBean;

	@Before
	public void setUp() {
		JpaEntityInformation<UserEntity, Long> userEntityInfo = new JpaMetamodelEntityInformation<UserEntity, Long>(
				UserEntity.class, entityManager.getMetamodel());
		userRepository = new GenericBaseRepository<UserEntity, Long>(
				userEntityInfo, entityManager);

		repositoryFactoryBean = new BaseRepositoryFactoryBean();
	}

	@Test
	@Transactional
	public void testCreateUser() {
		UserEntity user = UserEntity.getBuilder("dav", "123", new Date())
				.build();
		userRepository.save(user);
		LOGGER.debug("user:{} ", user);
	}

}
