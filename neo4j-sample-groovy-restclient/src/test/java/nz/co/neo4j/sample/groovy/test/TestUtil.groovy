package nz.co.neo4j.sample.groovy.test;

import java.nio.file.Path
import java.nio.file.Paths

class TestUtil {

	static Map<String, String> getJsonScripts(
			final String... jsonFileNames) {
		def resultMap = [:]
		jsonFileNames.each{
			Path resPath = Paths.get(TestUtil.class.getResource(it)
					.toURI())
			String createNodesJson = new String(
					java.nio.file.Files.readAllBytes(resPath), "UTF8")
			resultMap.put(it, createNodesJson)
		}
		return resultMap
	}
}
