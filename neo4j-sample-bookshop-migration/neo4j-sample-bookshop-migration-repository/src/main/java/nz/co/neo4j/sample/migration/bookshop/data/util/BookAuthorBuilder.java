package nz.co.neo4j.sample.migration.bookshop.data.util;

import nz.co.neo4j.sample.migration.bookshop.data.entity.AuthorEntity;
import nz.co.neo4j.sample.migration.bookshop.data.entity.BookAuthorEntity;
import nz.co.neo4j.sample.migration.bookshop.data.entity.BookEntity;

public class BookAuthorBuilder extends EntityBuilder<BookAuthorEntity> {

	@Override
	void initProduct() {
	}

	public BookAuthorBuilder create(BookEntity book, AuthorEntity author) {
		this.product = BookAuthorEntity.getBuilder(book, author).build();
		return this;
	}

	@Override
	BookAuthorEntity assembleProduct() {
		return this.product;
	}

}
