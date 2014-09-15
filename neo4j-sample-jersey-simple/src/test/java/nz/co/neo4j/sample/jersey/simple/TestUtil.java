package nz.co.neo4j.sample.jersey.simple;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class TestUtil {

	public static Map<String, Object> jsonToMap(final String json) {
		HashMap<String, Object> hashmap = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			hashmap = mapper.readValue(json,
					new TypeReference<Map<String, Object>>() {
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hashmap;
	}

}
