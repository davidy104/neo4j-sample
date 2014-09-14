package nz.co.neo4j.sample.migration.bookshop.ds.test.integration;

import static org.junit.Assert.assertNotNull
import groovy.util.logging.Slf4j

import javax.annotation.Resource

import nz.co.neo4j.sample.migration.bookshop.NotFoundException
import nz.co.neo4j.sample.migration.bookshop.data.User
import nz.co.neo4j.sample.migration.bookshop.ds.UserDS
import nz.co.neo4j.sample.migration.bookshop.ds.test.DsTestContextConfiguration;

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = [ DsTestContextConfiguration.class ])
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
public class UserDSIntegrationTest {

	@Resource
	UserDS userDs

	@Test
	void testCreate() {
		User added = userDs.createUser("Test01","123456");
		assertNotNull(added)
		log.info("added:{} $added")
	}

	@Test
	void testFindByName(){
		User found = userDs.getUserByName("jordan")
		assertNotNull(found)
		log.info("found:{} $found")
	}

	@Test(expected=NotFoundException.class)
	void testFindByNameNotFound(){
		User found = userDs.getUserByName("DAVVV")
	}

	@Test
	void testUpdateUser(){
		User update = new User(userName:"UpdateTest")
		update = userDs.updateUser(1L, update)
		log.info("update:{} $update")
	}
}
