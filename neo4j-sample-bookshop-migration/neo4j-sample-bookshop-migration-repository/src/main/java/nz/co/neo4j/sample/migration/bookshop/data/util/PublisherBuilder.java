package nz.co.neo4j.sample.migration.bookshop.data.util;

import nz.co.neo4j.sample.migration.bookshop.data.entity.PublisherEntity;

public class PublisherBuilder extends EntityBuilder<PublisherEntity> {

	@Override
	void initProduct() {
	}

	public PublisherBuilder create(final String name) {
		this.product = PublisherEntity.getBuilder(name).build();
		return this;
	}

	@Override
	PublisherEntity assembleProduct() {
		return this.product;
	}

}
