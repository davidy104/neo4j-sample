package nz.co.neo4j.sample.migration.bookshop.data.util;

import java.util.Date;

import nz.co.neo4j.sample.migration.bookshop.data.entity.UserEntity;

public class UserBuilder extends EntityBuilder<UserEntity> {

	@Override
	void initProduct() {
	}

	public UserBuilder create(String userName, Date createDate) {
		this.product = UserEntity.getBuilder(userName, createDate).build();
		return this;
	}

	@Override
	UserEntity assembleProduct() {
		return this.product;
	}

}
