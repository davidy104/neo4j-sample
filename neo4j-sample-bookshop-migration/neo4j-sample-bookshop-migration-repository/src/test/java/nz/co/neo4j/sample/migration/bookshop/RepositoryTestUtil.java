package nz.co.neo4j.sample.migration.bookshop;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import nz.co.neo4j.sample.migration.bookshop.data.entity.UserEntity;

public class RepositoryTestUtil {

	public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");

	public static List<UserEntity> userlist() throws Exception {
		List<UserEntity> users = new ArrayList<>();
		UserEntity user = UserEntity.getBuilder("dav", DATE_FORMAT.parse("2012-11-10"))
				.build();
		users.add(user);
		user = UserEntity.getBuilder("mike", DATE_FORMAT.parse("2013-10-10"))
				.build();
		users.add(user);
		user = UserEntity.getBuilder("mike", DATE_FORMAT.parse("2014-05-10"))
				.build();
		users.add(user);
		return users;
	}
}
