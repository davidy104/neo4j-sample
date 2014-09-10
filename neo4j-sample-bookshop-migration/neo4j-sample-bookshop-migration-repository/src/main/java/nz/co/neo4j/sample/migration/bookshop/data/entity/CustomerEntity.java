package nz.co.neo4j.sample.migration.bookshop.data.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "T_CUSTOMER")
@PrimaryKeyJoinColumn(name = "PERSON_ID")
public class CustomerEntity extends PersonEntity implements Serializable {
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
	private UserEntity user;

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public static Builder getBuilder(String lastName, String firstName,
			String email, Date birthDate) {
		return new Builder(lastName, firstName, email, birthDate);
	}

	public static Builder getBuilder(String lastName, String firstName,
			String email) {
		return new Builder(lastName, firstName, email);
	}

	public static class Builder {
		private CustomerEntity built;

		public Builder(String lastName, String firstName, String email) {
			built = new CustomerEntity();
			built.lastName = lastName;
			built.firstName = firstName;
			built.email = email;
		}

		public Builder(String lastName, String firstName, String email,
				Date birthDate) {
			built = new CustomerEntity();
			built.lastName = lastName;
			built.firstName = firstName;
			built.email = email;
			built.birthDate = birthDate;
		}

		public CustomerEntity build() {
			return built;
		}
	}

}
