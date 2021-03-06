package nz.co.neo4j.sample.migration.bookshop.data.util;

import java.util.Date;

import nz.co.neo4j.sample.migration.bookshop.data.entity.CustomerEntity;
import nz.co.neo4j.sample.migration.bookshop.data.entity.UserEntity;

public class CustomerBuilder extends EntityBuilder<CustomerEntity> {

	@Override
	void initProduct() {
	}

	public CustomerBuilder create(String lastName, String firstName,
			String email, Date birthDate) {
		this.product = CustomerEntity.getBuilder(lastName, firstName, email,
				birthDate).build();
		return this;
	}

	public CustomerBuilder setUser(final UserEntity user) {
		this.product.setUser(user);
		return this;
	}

	@Override
	CustomerEntity assembleProduct() {
		return this.product;
	}

}
