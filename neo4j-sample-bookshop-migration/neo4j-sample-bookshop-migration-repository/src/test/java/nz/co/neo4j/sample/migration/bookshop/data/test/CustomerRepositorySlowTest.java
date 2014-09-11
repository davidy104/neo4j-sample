package nz.co.neo4j.sample.migration.bookshop.data.test;

import static nz.co.neo4j.sample.migration.bookshop.data.entity.QCustomerEntity.customerEntity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.annotation.Resource;

import nz.co.neo4j.sample.migration.bookshop.data.entity.CustomerEntity;
import nz.co.neo4j.sample.migration.bookshop.data.entity.UserEntity;
import nz.co.neo4j.sample.migration.bookshop.data.repository.CustomerRepository;
import nz.co.neo4j.sample.migration.bookshop.data.repository.UserRepository;
import nz.co.neo4j.sample.migration.bookshop.data.test.util.TestContextConfiguration;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.Projections;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestContextConfiguration.class })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class CustomerRepositorySlowTest {

	@Resource
	private CustomerRepository customerRepository;

	@Resource
	private UserRepository userRepository;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CustomerRepositorySlowTest.class);

	@After
	public void cleanUp() {
		this.customerRepository.deleteAll();
	}

	@Test
	public void testFindAll() {
		// test initial customer data
		List<CustomerEntity> customers = customerRepository.findAll();
		LOGGER.info("size:{} ", customers.size());
		assertEquals(customers.size(), 3);
	}

	@Test
	@Transactional(readOnly = true)
	public void testFindById() {
		CustomerEntity found = customerRepository.findOne(1L);
		assertNotNull(found);
		LOGGER.info("found: {} ", found);
	}

	@Test
	@Transactional(readOnly = true)
	public void testFindByName() {
		FactoryExpression<CustomerEntity> factoryExpression = Projections.bean(
				CustomerEntity.class, customerEntity.lastName,
				customerEntity.user, customerEntity.birthDate);
		Predicate predicate = customerEntity.firstName.eq("Brad").and(
				customerEntity.lastName.eq("Wu"));
		CustomerEntity found = customerRepository.findOne(factoryExpression,
				predicate);
		assertNotNull(found);
		LOGGER.info("found: {} ", found);
	}

	@Test
	@Transactional
	public void testCascadeDelete() throws Exception {
		CustomerEntity customer = customerRepository.findOne(1L);
		assertNotNull(customer);
		LOGGER.info("customer: {} ", customer);
		UserEntity user = customer.getUser();
		LOGGER.info("before customer remove, user: {} ", user);
		Long userId = user.getUserId();
		customerRepository.delete(customer);
		// we need to flush after remove, otherwise cascade delete user not
		// working
		customerRepository.flush();
		try {
			user = userRepository.getOne(userId);
			LOGGER.info("after customer remove, user: {} ", user);
			assertNull(user);
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	@Test
	@Transactional(readOnly = true)
	public void testMethodNameQuery() {
		List<CustomerEntity> customers = customerRepository
				.findByFirstNameStartingWithOrLastNameStartingWith("Bra", "Wu");
		LOGGER.info("firstone: {} ", customers.get(0));
	}

	@Test
//	@Transactional(readOnly = true)
	public void testQueryAnnotation() {
		List<CustomerEntity> customers = customerRepository
				.findCustomers("Brad");
		LOGGER.info("find one: {} ", customers.get(0));
//		LOGGER.info("user:{} ",customers.get(0).getUser());
	}

}
