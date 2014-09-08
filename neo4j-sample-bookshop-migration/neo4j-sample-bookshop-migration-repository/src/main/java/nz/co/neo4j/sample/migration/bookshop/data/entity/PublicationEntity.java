package nz.co.neo4j.sample.migration.bookshop.data.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "T_PUBLICATION")
public class PublicationEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "PUBLICATION_ID", insertable = false, updatable = false)
	private Long publicationId;

	@Column(name = "YEAR")
	private Integer year;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PUBLISHER_ID", referencedColumnName = "PUBLISHER_ID")
	private PublisherEntity publisher;
	

}
