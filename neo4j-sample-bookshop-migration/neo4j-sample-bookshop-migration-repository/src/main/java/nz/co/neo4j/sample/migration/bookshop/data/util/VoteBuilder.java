package nz.co.neo4j.sample.migration.bookshop.data.util;

import java.util.Date;

import nz.co.neo4j.sample.migration.bookshop.data.entity.BookEntity;
import nz.co.neo4j.sample.migration.bookshop.data.entity.UserEntity;
import nz.co.neo4j.sample.migration.bookshop.data.entity.VoteEntity;

public class VoteBuilder extends EntityBuilder<VoteEntity> {

	@Override
	void initProduct() {
	}

	public VoteBuilder create(final BookEntity book, final UserEntity user,
			final Integer score, final Date createTime) {
		this.product = VoteEntity.getBuilder(book, user, score, createTime)
				.build();
		return this;
	}

	@Override
	VoteEntity assembleProduct() {
		return this.product;
	}

}
