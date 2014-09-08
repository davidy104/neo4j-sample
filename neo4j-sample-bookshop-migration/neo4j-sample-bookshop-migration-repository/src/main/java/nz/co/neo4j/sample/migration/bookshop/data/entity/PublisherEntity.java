package nz.co.neo4j.sample.migration.bookshop.data.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@SuppressWarnings("serial")
@Entity
@Table(name = "T_PUBLISHER")
public class PublisherEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "PUBLISHER_ID", insertable = false, updatable = false)
	private Long publisherId;

	@Column(name = "NAME")
	private String name;

	@Column(name = "CREATE_TIME")
	@Temporal(value = TemporalType.TIME)
	private Date createTime;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, mappedBy = "publisher")
	private List<PublicationEntity> publications = Collections.emptyList();

	public Long getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(Long publisherId) {
		this.publisherId = publisherId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public List<PublicationEntity> getPublications() {
		return publications;
	}

	public void addPublication(final PublicationEntity publicationEntity) {
		if (publications.isEmpty()) {
			publications = new ArrayList<>();
		}
		publications.add(publicationEntity);
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(this.name, ((PublisherEntity) obj).name)
				.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.name).toHashCode();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
