package nz.co.neo4j.sample.migration.bookshop.data.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "T_AUTHOR")
@PrimaryKeyJoinColumn(name = "PERSON_ID")
public class AuthorEntity extends PersonEntity implements Serializable {

}
