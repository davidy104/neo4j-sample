package nz.co.neo4j.sample.migration.bookshop.data.util;

import java.util.Date;

import nz.co.neo4j.sample.migration.bookshop.data.entity.AuthorEntity;
import nz.co.neo4j.sample.migration.bookshop.data.entity.UserEntity;

public class AuthorBuilder extends EntityBuilder<AuthorEntity> {

	@Override
	void initProduct() {
	}

	public AuthorBuilder create(String lastName, String firstName,
			String email, Date birthDate) {
		if (birthDate != null) {
			this.product = AuthorEntity.getBuilder(lastName, firstName, email,
					birthDate).build();
		} else {
			this.product = AuthorEntity.getBuilder(lastName, firstName, email)
					.build();
		}
		return this;
	}

	public AuthorBuilder setUser(final UserEntity user) {
		this.product.setUser(user);
		return this;
	}

	@Override
	AuthorEntity assembleProduct() {
		return this.product;
	}

}
