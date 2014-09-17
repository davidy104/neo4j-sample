package nz.co.neo4j.sample.groovy

import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

import org.springframework.stereotype.Service

@Service("neo4jRestJsonConverter")
@Slf4j
class Neo4jRestJsonConverter {

	Map<String,Map<String,Object>> getDataFromResponse(final String response){
		log.debug "getDataFromResponse start:{} $response"
		def result = [:]
		def resultDataMap = [:]
		JsonSlurper jsonSlurper = new JsonSlurper()
		Map jsonResult = (Map) jsonSlurper.parseText(response)
		ArrayList datajson = (ArrayList)jsonResult.get("data")

		ArrayList innerData = (ArrayList)datajson.get(0)
		log.debug "innerData:{} $innerData"
		innerData.each {
			Map datamap = (Map)it
			resultDataMap = (Map)datamap['data']
			result.put(datamap['self'] , resultDataMap)
		}
		return result
	}
}
