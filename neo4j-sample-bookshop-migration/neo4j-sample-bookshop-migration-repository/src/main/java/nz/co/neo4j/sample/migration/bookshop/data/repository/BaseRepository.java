package nz.co.neo4j.sample.migration.bookshop.data.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends
		JpaRepository<T, ID>, QueryDslPredicateExecutor<T> {

}
