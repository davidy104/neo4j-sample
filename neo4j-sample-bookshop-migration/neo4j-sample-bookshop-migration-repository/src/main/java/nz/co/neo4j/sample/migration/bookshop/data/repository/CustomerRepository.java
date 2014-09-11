package nz.co.neo4j.sample.migration.bookshop.data.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.packtpub.springdata.jpa.model.Contact;

import nz.co.neo4j.sample.migration.bookshop.data.entity.CustomerEntity;

public interface CustomerRepository extends
		BaseRepository<CustomerEntity, Long> {
	// Method-Name Query
	List<CustomerEntity> findByFirstNameStartingWithOrLastNameStartingWith(
			String firstName, String lastName);

	@Query("SELECT c FROM Contact c WHERE LOWER(c.firstName) LIKE LOWER(:searchTerm) OR LOWER(c.lastName) LIKE LOWER(:searchTerm)")
	List<CustomerEntity> findCustomers(@Param("searchTerm") String searchTerm,
			Pageable page);
}
