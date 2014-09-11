package nz.co.neo4j.sample.migration.bookshop.data.test.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import nz.co.neo4j.sample.migration.bookshop.data.entity.UserEntity;
import nz.co.neo4j.sample.migration.bookshop.data.util.CustomerBuilder;
import nz.co.neo4j.sample.migration.bookshop.data.util.EntityBuilder.EntityBuilderManager;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public class CustomerInitialTestDataSetup {
	private final TransactionTemplate transactionTemplate;

	@PersistenceContext
	private EntityManager entityManager;

	Random rand = new Random();

	public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");

	public CustomerInitialTestDataSetup(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	public void initialize() {
		EntityBuilderManager.setEntityManager(this.entityManager);
		this.transactionTemplate.execute(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(TransactionStatus status) {
				{
					new CustomerBuilder() {
						{
							this.create(
									"Mike",
									"Jordan",
									"mike.jordan@gmail.com",
									getDateFromFormat(DATE_FORMAT, "1970-01-01"));
						}
					}.build().setUser(
							UserEntity
									.getBuilder(
											"jordan",
											getDateFromFormat(DATE_FORMAT,
													"2013-01-01")).build());

					new CustomerBuilder() {
						{
							this.create(
									"Ni",
									"Joe",
									"ni.joe@gmail.com",
									getDateFromFormat(DATE_FORMAT, "1990-01-01"));
						}
					}.build().setUser(
							UserEntity
									.getBuilder(
											"joen",
											getDateFromFormat(DATE_FORMAT,
													"2013-06-01")).build());

					new CustomerBuilder() {
						{
							this.create(
									"Wu",
									"Brad",
									"wu.brad@gmail.com",
									getDateFromFormat(DATE_FORMAT, "1980-01-01"));
						}
					}.build().setUser(
							UserEntity
									.getBuilder(
											"bradw",
											getDateFromFormat(DATE_FORMAT,
													"2014-03-01")).build());
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
