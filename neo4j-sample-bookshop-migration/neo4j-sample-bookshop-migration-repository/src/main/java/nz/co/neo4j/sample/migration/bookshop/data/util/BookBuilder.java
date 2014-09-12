package nz.co.neo4j.sample.migration.bookshop.data.util;

import nz.co.neo4j.sample.migration.bookshop.data.entity.BookAuthorEntity;
import nz.co.neo4j.sample.migration.bookshop.data.entity.BookEntity;
import nz.co.neo4j.sample.migration.bookshop.data.entity.PublicationEntity;
import nz.co.neo4j.sample.migration.bookshop.data.entity.VoteEntity;

public class BookBuilder extends EntityBuilder<BookEntity> {

	@Override
	void initProduct() {
	}

	public BookBuilder create(String title, int pages, String tags,
			PublicationEntity publication) {
		this.product = BookEntity.getBuilder(title, pages, tags, publication)
				.build();
		return this;
	}

	public BookBuilder addPublication(final PublicationEntity publication) {
		this.product.setPublication(publication);
		return this;
	}

	public BookBuilder addBookAuthors(final BookAuthorEntity... bookAuthors) {
		for (BookAuthorEntity bookAuthor : bookAuthors) {
			this.product.addBookAuthor(bookAuthor);
		}
		return this;
	}

	public BookBuilder addVotes(final VoteEntity... votes) {
		for (VoteEntity vote : votes) {
			this.product.addVote(vote);
		}
		return this;
	}

	@Override
	BookEntity assembleProduct() {
		return this.product;
	}

}
