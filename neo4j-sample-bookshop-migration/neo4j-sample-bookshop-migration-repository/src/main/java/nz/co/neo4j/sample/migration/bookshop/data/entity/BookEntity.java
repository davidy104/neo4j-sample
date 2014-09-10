package nz.co.neo4j.sample.migration.bookshop.data.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@SuppressWarnings("serial")
@Entity
@Table(name = "T_BOOK")
public class BookEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BOOK_ID", insertable = false, updatable = false)
	private Long bookId;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "PAGES")
	private Integer pages;

	@Column(name = "TAGS")
	private String tags;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, mappedBy = "bookAuthorPK.book")
	private List<BookAuthorEntity> bookAuthors = Collections.emptyList();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "votePK.book")
	private List<VoteEntity> votes = Collections.emptyList();

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PUBLICATION_ID", referencedColumnName = "PUBLICATION_ID")
	private PublicationEntity publication;

	public void addBookAuthor(final BookAuthorEntity bookAuthor) {
		if (this.bookAuthors.isEmpty()) {
			this.bookAuthors = new ArrayList<>();
		}
		this.bookAuthors.add(bookAuthor);
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public List<BookAuthorEntity> getBookAuthors() {
		return bookAuthors;
	}

	public void setBookAuthors(List<BookAuthorEntity> bookAuthors) {
		this.bookAuthors = bookAuthors;
	}

	public List<VoteEntity> getVotes() {
		return votes;
	}

	public void setVotes(List<VoteEntity> votes) {
		this.votes = votes;
	}

	public PublicationEntity getPublication() {
		return publication;
	}

	public void setPublication(PublicationEntity publication) {
		this.publication = publication;
	}

	public static Builder getBuilder(String title, Integer pages, String tags) {
		return new Builder(title, pages, tags);
	}

	public static Builder getBuilder(String title, Integer pages) {
		return new Builder(title, pages);
	}

	public static class Builder {

		private BookEntity built;

		public Builder(String title, Integer pages, String tags) {
			built = new BookEntity();
			built.title = title;
			built.pages = pages;
			built.tags = tags;
		}

		public Builder(String title, Integer pages) {
			built = new BookEntity();
			built.title = title;
			built.pages = pages;
		}

		public Builder(String title, Integer pages, String tags,
				PublicationEntity publication) {
			built = new BookEntity();
			built.title = title;
			built.pages = pages;
			built.tags = tags;
			built.publication = publication;
		}

		public BookEntity build() {
			return built;
		}
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(this.title, ((BookEntity) obj).title)
				.append(this.pages, ((BookEntity) obj).pages)
				.append(this.tags, ((BookEntity) obj).tags).isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.title).append(this.pages).append(this.tags)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("bookId", bookId).append("title", title)
				.append("pages", pages).append("tags", tags).toString();
	}

}
