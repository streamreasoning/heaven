package it.polimi.heaven.citybench;

import it.polimi.heaven.baselines.enums.OntoLanguage;
import it.polimi.heaven.baselines.jena.GraphBaseline;
import it.polimi.heaven.baselines.jena.query.BaselineQuery;
import it.polimi.heaven.citybench.encoders.CB2GraphStimulusEncoder;
import it.polimi.heaven.core.teststand.TestStand;
import it.polimi.heaven.core.teststand.collector.ResultCollector;
import it.polimi.heaven.core.teststand.data.Experiment;
import it.polimi.heaven.core.teststand.rspengine.Receiver;
import it.polimi.heaven.core.teststand.streamer.Encoder;
import it.polimi.heaven.core.teststand.streamer.FlowRateProfiler;
import it.polimi.heaven.core.teststand.streamer.ParsingTemplate;
import it.polimi.heaven.core.teststand.streamer.Streamer;
import it.polimi.heaven.core.teststand.streamer.TSStreamer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
		String query = "select * " + "	where { ?s ?p ?o ." + "} ";

		RDFFileManager.initializeDataset(dataset);

		BaselineQuery q = new BaselineQuery();
		q.setEsperStreams(new String[] { "AarhusTrafficData182955", "AarhusTrafficData158505" });
		q.setEsper_queries(" select * from   AarhusTrafficData182955.win:time(3000 msec)  output snapshot every 10000 msec");
		q.setSparql_query(query);
		q.setTbox(RDFFileManager.dataset.getDefaultModel());

		er = RDFFileManager.buildRepoFromFile(0);
		Map<String, EventDeclaration> event_declarations = startCSPARQLStreamsFromQuery(Arrays.asList(q.getEsperStreams()));
		ParsingTemplate obs_factory = new CBParser(event_declarations);
		Encoder encoder = new CB2GraphStimulusEncoder();
		FlowRateProfiler cp_frp = new CBFlowRateProfiler(obs_factory, encoder, 0, 1406872000000L);
		Receiver receiver = new Receiver();
		GraphBaseline baseline = new GraphBaseline(receiver);
		baseline.setOntology_language(OntoLanguage.SMPL);
		baseline.registerQuery(q);
		ResultCollector result_collector = new ResultCollector(true, true, true);
		Streamer streamer = new TSStreamer(1000, baseline, cp_frp, obs_factory);
		TestStand ts = new TestStand(streamer, baseline, result_collector, receiver);

		ts.init(createExperiment());
		ts.run();
	}

	private static Experiment createExperiment() {
		Experiment experiment = new Experiment();
		experiment.setExperimentNumber(0);
		experiment.setExecutionNumber(0);
		experiment.setDate(new Date().toString());

		experiment.setInputSource("./Q1.stream");
		experiment.setOutputPath("/Users/Riccardo/_Projects/SR/heaven-citybench/data/output/");
		experiment.setResponsivity(100L);

		return experiment;
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
				log.debug("Stream detected: " + streamURI);
				resultSet.add(streamURI.split("#")[1] + ".stream");
			}
		}

		List<String> results = new ArrayList<String>();
		results.addAll(resultSet);
		return results;
	}

	private static Map<String, EventDeclaration> startCSPARQLStreamsFromQuery(List<String> streamNames) throws Exception {
		Map<String, EventDeclaration> event_declarations = new HashMap<String, EventDeclaration>();
		Map<String, EventDeclaration> eds = er.getEds();
		Set<String> keySet = eds.keySet();
		for (String i : keySet) {
			log.debug(i);
		}
		log.debug("----");
		for (String sn : streamNames) {
			String uri = RDFFileManager.defaultPrefix + sn.split("\\.")[0];
			if (eds.containsKey(uri))
				log.debug(eds.get(uri));
			EventDeclaration ed = eds.get(uri);
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
