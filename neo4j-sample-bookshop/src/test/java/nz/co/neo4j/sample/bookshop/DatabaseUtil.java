package nz.co.neo4j.sample.bookshop;

import java.io.File;

public class DatabaseUtil {

	public static boolean clearAll(final String dbPath) {
		File f = new File(dbPath);
		return delete(f);
	}

	private static boolean delete(File f) {
		if (f.isDirectory()) {
			for (File c : f.listFiles()) {
				delete(c);
			}
		}
		return f.delete();
	}
}
