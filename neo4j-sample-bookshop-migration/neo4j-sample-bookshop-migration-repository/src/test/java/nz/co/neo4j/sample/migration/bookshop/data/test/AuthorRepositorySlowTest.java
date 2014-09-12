package nz.co.neo4j.sample.migration.bookshop.data.test;

import javax.annotation.Resource;

import nz.co.neo4j.sample.migration.bookshop.data.repository.AuthorRepository;
import nz.co.neo4j.sample.migration.bookshop.data.repository.UserRepository;
import nz.co.neo4j.sample.migration.bookshop.data.test.util.RepositoryTestContextConfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RepositoryTestContextConfiguration.class })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthorRepositorySlowTest {

	@Resource
	private AuthorRepository authorRepository;

	@Resource
	private UserRepository userRepository;

	@Test
	public void test() {

	}

}
