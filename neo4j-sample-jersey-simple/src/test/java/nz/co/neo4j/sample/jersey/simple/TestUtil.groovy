package nz.co.neo4j.sample.jersey.simple;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class TestUtil {

	static Map<String, String> getJsonScripts(
			final String... jsonFileNames) throws Exception {
		def resultMap = [:]
		jsonFileNames.each {
			Path resPath = Paths.get(TestUtil.class.getResource(it)
					.toURI())
			String createNodesJson = new String(
					java.nio.file.Files.readAllBytes(resPath), "UTF8")
			resultMap.put(it, createNodesJson)
		}
		return resultMap
	}

	static Map<String, Object> jsonToMap(final String json) {
		def hashmap = [:]
		ObjectMapper mapper = new ObjectMapper()
		try {
			hashmap = mapper.readValue(json,
					new TypeReference<Map<String, Object>>() {
					})
		} catch (Exception e) {
			e.printStackTrace()
		}
		return hashmap
	}
}
