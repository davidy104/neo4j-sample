package nz.co.neo4j.sample.migration.bookshop.data.util;

import nz.co.neo4j.sample.migration.bookshop.data.entity.BookEntity;

public class BookBuilder extends EntityBuilder<BookEntity> {

	@Override
	void initProduct() {
	}

	public BookBuilder create(String title, int pages, String tags) {
		this.product = BookEntity.getBuilder(title, pages, tags).build();
		return this;
	}

	@Override
	BookEntity assembleProduct() {
		return this.product;
	}

}
