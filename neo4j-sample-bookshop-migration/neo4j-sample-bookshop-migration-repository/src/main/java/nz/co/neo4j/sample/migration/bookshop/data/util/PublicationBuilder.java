package nz.co.neo4j.sample.migration.bookshop.data.util;

import nz.co.neo4j.sample.migration.bookshop.data.entity.PublicationEntity;
import nz.co.neo4j.sample.migration.bookshop.data.entity.PublisherEntity;

public class PublicationBuilder extends EntityBuilder<PublicationEntity> {

	@Override
	void initProduct() {
	}

	public PublicationBuilder create(Integer year, PublisherEntity publisher) {
		this.product = PublicationEntity.getBuilder(year, publisher).build();
		return this;
	}

	@Override
	PublicationEntity assembleProduct() {
		return this.product;
	}

}
