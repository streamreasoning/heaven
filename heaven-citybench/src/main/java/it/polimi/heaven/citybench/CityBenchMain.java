package it.polimi.heaven.citybench;

import it.polimi.heaven.baselines.jena.GraphBaseline;
import it.polimi.heaven.baselines.jena.query.BaselineQuery;
import it.polimi.heaven.citybench.encoders.CB2GraphStimulusEncoder;
import it.polimi.heaven.core.teststand.rspengine.Receiver;
import it.polimi.heaven.core.teststand.streamer.Encoder;
import it.polimi.heaven.core.teststand.streamer.FlowRateProfiler;
import it.polimi.heaven.core.teststand.streamer.ParsingTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.extern.log4j.Log4j;

import org.insight_centre.aceis.eventmodel.EventDeclaration;
import org.insight_centre.aceis.io.EventRepository;
import org.insight_centre.aceis.io.rdf.RDFFileManager;

@Log4j
public class CityBenchMain {

	private static String dataset = "SensorRepository.n3", ontology = "ontology", streams = "streams";
	private static EventRepository er;

	public static void main(String[] args) throws Exception {
		HashMap<String, String> parameters = new HashMap<String, String>();
		for (String s : args) {
			parameters.put(s.split("=")[0], s.split("=")[1]);
		}
		String query = "select ?obId1 ?obId2 ?v1 ?v2 "
				+ "	where {"
				+ "?p1   a <http://www.insight-centre.org/citytraffic#CongestionLevel>. "
				+ "?p2   a <http://www.insight-centre.org/citytraffic#CongestionLevel>. "
				+ "{ "
				+ "?obId1 <http://purl.oclc.org/NET/ssnx/ssn#observedProperty> ?p1. "
				+ "?obId1 <http://purl.oclc.org/NET/sao/hasValue> ?v1. "
				+ "?obId1 <http://purl.oclc.org/NET/ssnx/ssn#observedBy> <http://www.insight-centre.org/dataset/SampleEventService#AarhusTrafficData182955>."
				+ "} "
				+ "{"
				+ "?obId2 <http://purl.oclc.org/NET/ssnx/ssn#observedProperty> ?p2."
				+ "?obId2 <http://purl.oclc.org/NET/sao/hasValue> ?v2. "
				+ "?obId2 <http://purl.oclc.org/NET/ssnx/ssn#observedBy> <http://www.insight-centre.org/dataset/SampleEventService#AarhusTrafficData158505>."
				+ "}} ";

		RDFFileManager.initializeDataset(dataset);

		BaselineQuery q = new BaselineQuery();
		q.setEsperStreams(new String[] { "AarhusTrafficData182955", "AarhusTrafficData158505" });
		q.setEsper_queries(" select  * from AarhusTrafficData182955.win:time(3000 msec), AarhusTrafficData158505.win:time(3000 msec) output snapshot every 1000 msec");
		q.setSparql_query(query);
		q.setTbox(RDFFileManager.dataset.getDefaultModel());

		er = RDFFileManager.buildRepoFromFile(0);
		Map<String, EventDeclaration> event_declarations = startCSPARQLStreamsFromQuery(Arrays.asList(q.getEsperStreams()));
		ParsingTemplate obs_factory = new CBParser(event_declarations);
		Encoder encoder = new CB2GraphStimulusEncoder();
		FlowRateProfiler cp_frp = new CBFlowRateProfiler(obs_factory, encoder, 0);
		GraphBaseline baselines = new GraphBaseline(new Receiver());

	}

	private static List<String> getStreamFileNamesFromQuery(String query) throws Exception {
		Set<String> resultSet = new HashSet<String>();
		String[] streamSegments = query.trim().split("stream");
		if (streamSegments.length == 1)
			throw new Exception("Error parsing query, no stream statements found for: " + query);
		else {
			for (int i = 1; i < streamSegments.length; i++) {
				int indexOfLeftBracket = streamSegments[i].trim().indexOf("<");
				int indexOfRightBracket = streamSegments[i].trim().indexOf(">");
				String streamURI = streamSegments[i].substring(indexOfLeftBracket + 2, indexOfRightBracket + 1);
				log.info("Stream detected: " + streamURI);
				resultSet.add(streamURI.split("#")[1] + ".stream");
			}
		}

		List<String> results = new ArrayList<String>();
		results.addAll(resultSet);
		return results;
	}

	private static Map<String, EventDeclaration> startCSPARQLStreamsFromQuery(List<String> streamNames) throws Exception {
		Map<String, EventDeclaration> event_declarations = new HashMap<String, EventDeclaration>();
		for (String sn : streamNames) {
			log.info(sn);
			String uri = RDFFileManager.defaultPrefix + sn.split("\\.")[0];
			EventDeclaration ed = er.getEds().get(uri);
			String path = streams + "/" + sn;
			event_declarations.put(sn, ed);
			// if (!startedStreams.contains(uri)) {
			// startedStreams.add(uri);
			// CSPARQLSensorStream css;
			//
			// if (ed.getEventType().contains("traffic")) {
			// css = new CSPARQLAarhusTrafficStream(uri, path, ed, start, end);
			// } else if (ed.getEventType().contains("pollution")) {
			// css = new CSPARQLAarhusPollutionStream(uri, path, ed, start,
			// end);
			// } else if (ed.getEventType().contains("weather")) {
			// css = new CSPARQLAarhusWeatherStream(uri, path, ed, start, end);
			// } else if (ed.getEventType().contains("location"))
			// css = new CSPARQLLocationStream(uri, path, ed);
			// else if (ed.getEventType().contains("parking"))
			// css = new CSPARQLAarhusParkingStream(uri, path, ed, start, end);
			// else
			// throw new Exception("Sensor type not supported.");
			// css.setRate(rate);
			// css.setFreq(frequency);
			// }
		}
		return event_declarations;
	}
}
