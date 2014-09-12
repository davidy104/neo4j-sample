package nz.co.neo4j.sample.migration.bookshop.data.support;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import nz.co.neo4j.sample.migration.bookshop.data.entity.AuthorEntity;
import nz.co.neo4j.sample.migration.bookshop.data.entity.CustomerEntity;
import nz.co.neo4j.sample.migration.bookshop.data.entity.UserEntity;
import nz.co.neo4j.sample.migration.bookshop.data.util.AuthorBuilder;
import nz.co.neo4j.sample.migration.bookshop.data.util.CustomerBuilder;
import nz.co.neo4j.sample.migration.bookshop.data.util.EntityBuilder.EntityBuilderManager;
import nz.co.neo4j.sample.migration.bookshop.data.util.UserBuilder;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public class InitialDataSetup {
	private final TransactionTemplate transactionTemplate;

	@PersistenceContext
	private EntityManager entityManager;

	Random rand = new Random();

	public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");

	private UserEntity user1;
	private UserEntity user2;
	private UserEntity user3;
	private UserEntity user4;

	private CustomerEntity cust1;
	private CustomerEntity cust2;

	private AuthorEntity author1;
	private AuthorEntity author2;

	public InitialDataSetup(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	public void initialize() {
		EntityBuilderManager.setEntityManager(this.entityManager);
		this.transactionTemplate.execute(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(TransactionStatus status) {
				// initial Users
				{
					user1 = new UserBuilder() {
						{
							this.create(
									"jordan",
									getDateFromFormat(DATE_FORMAT, "2013-01-01"));
						}
					}.build();

					user2 = new UserBuilder() {
						{
							this.create(
									"joen",
									getDateFromFormat(DATE_FORMAT, "2013-06-01"));
						}
					}.build();

					user3 = new UserBuilder() {
						{
							this.create(
									"chinuaAchebe",
									getDateFromFormat(DATE_FORMAT, "2014-04-01"));
						}
					}.build();

					user4 = new UserBuilder() {
						{
							this.create(
									"HansChristianAndersen",
									getDateFromFormat(DATE_FORMAT, "2014-01-01"));
						}
					}.build();

				}
				// initial Customers
				{
					cust1 = new CustomerBuilder() {
						{
							this.create(
									"Mike",
									"Jordan",
									"mike.jordan@gmail.com",
									getDateFromFormat(DATE_FORMAT, "1970-01-01"));
						}
					}.build();
					cust1.setUser(user1);

					cust2 = new CustomerBuilder() {
						{
							this.create(
									"Ni",
									"Joe",
									"ni.joe@gmail.com",
									getDateFromFormat(DATE_FORMAT, "1990-01-01"));
						}
					}.build();
					cust2.setUser(user2);
				}
				// bind user with customer
				{
					user1.setPerson(cust1);
					user2.setPerson(cust2);
				}

				// initial Authors
				{
					author1 = new AuthorBuilder() {
						{
							this.create("Achebe", "Chinua",
									"Chinua.Achebe@gmail.com", null).build();
						}
					}.build();
					author1.setUser(user3);

					author2 = new AuthorBuilder() {
						{
							this.create("Andersen", "Hans Christian",
									"HansChristian.Andersen@gmail.com", null)
									.build();
						}
					}.build();
					author2.setUser(user4);
				}

				// bind user with authors
				{
					user3.setPerson(author1);
					user4.setPerson(author2);
				}

				return null;
			}
		});
		EntityBuilderManager.clearEntityManager();
	}

	private static Date getDateFromFormat(DateFormat dateFormatter,
			String dateStr) {
		Date date = null;
		try {
			date = dateFormatter.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
