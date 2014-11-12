package it.polimi;

import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.collector.saver.CSVEventSaver;
import it.polimi.processing.collector.saver.EventSaver;
import it.polimi.processing.collector.saver.SQLLiteEventSaver;
import it.polimi.processing.collector.saver.TrigEventSaver;
import it.polimi.processing.collector.saver.data.CollectableData;
import it.polimi.processing.enums.ExecutionStates;
import it.polimi.processing.events.StreamingEvent;
import it.polimi.processing.events.result.ExperimentResultEvent;
import it.polimi.processing.events.result.StreamingEventResult;
import it.polimi.processing.rspengine.RSPEngine;
import it.polimi.processing.rspengine.esper.plain.PlainCompleteRHODF;
import it.polimi.processing.rspengine.jena.JenaEngine;
import it.polimi.processing.rspengine.jena.JenaEngineRhoDF;
import it.polimi.processing.streamer.Streamer;
import it.polimi.processing.teststand.collector.CollectorEventResult;
import it.polimi.processing.teststand.collector.CollectorExperimentResult;
import it.polimi.processing.teststand.core.TestStand;
import it.polimi.processing.teststand.streamer.TriGStreamer;
import it.polimi.utils.FileUtils;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonMain {
	private static final int PLAIN2369 = 0, JENA = 1, JENARHODF = 2;
	private static final int PLAIN2369NW = 3, JENANW = 4, JENARHODFNW = 5;

	public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {
		String[] files = new String[] { "inputTrigINIT500D1GF0SN2R.trig" };

		int rspengine = PLAIN2369NW;
		String comment = "1000EventsV2";

		Date exeperimentDate = FileUtils.d;
		int experimentNumber = 0;
		TestStand<RSPEngine> testStand = new TestStand<RSPEngine>();

		StartableCollector<StreamingEventResult> streamingEventResultCollector = new CollectorEventResult(testStand, new TrigEventSaver(),
				new CSVEventSaver());

		StartableCollector<StreamingEventResult> ligthStreamingEventResultCollector = new CollectorEventResult(testStand, new EventSaver() {

			@Override
			public boolean save(CollectableData d, String where) {
				return true;
			}

			@Override
			public ExecutionStates init() {
				return ExecutionStates.READY;
			}

			@Override
			public ExecutionStates close() {
				return ExecutionStates.CLOSED;
			}
		}, new CSVEventSaver());

		StartableCollector<ExperimentResultEvent> experimentResultCollector = new CollectorExperimentResult(testStand, new SQLLiteEventSaver());

		Streamer<StreamingEvent> streamer = new TriGStreamer<StreamingEvent>(testStand);

		for (String f : files) {
			switch (rspengine) {
				case JENA:
					run(f, comment, experimentNumber, testStand, streamingEventResultCollector, experimentResultCollector, new JenaEngine("jenasmpl",
							testStand), streamer, exeperimentDate);
					break;
				case JENARHODF:
					run(f, comment, experimentNumber, testStand, streamingEventResultCollector, experimentResultCollector, new JenaEngineRhoDF(
							"jenarhodf", FileUtils.UNIV_BENCH_RHODF_MODIFIED, FileUtils.RHODF_RULE_SET_RUNTIME, testStand), streamer, exeperimentDate);
					break;
				case PLAIN2369:
					run(f, comment, experimentNumber, testStand, streamingEventResultCollector, experimentResultCollector, new PlainCompleteRHODF(
							"plain2369", testStand, FileUtils.UNIV_BENCH_RHODF_MODIFIED), streamer, exeperimentDate);
					break;
				case JENANW:
					run(f, comment, experimentNumber, testStand, ligthStreamingEventResultCollector, experimentResultCollector, new JenaEngine(
							"jenasmplNW", testStand), streamer, exeperimentDate);
					break;
				case JENARHODFNW:
					run(f, comment, experimentNumber, testStand, ligthStreamingEventResultCollector, experimentResultCollector, new JenaEngineRhoDF(
							"jenarhodfNW", FileUtils.UNIV_BENCH_RHODF_MODIFIED, FileUtils.RHODF_RULE_SET_RUNTIME, testStand), streamer,
							exeperimentDate);
					break;
				case PLAIN2369NW:
					run(f, comment, experimentNumber, testStand, ligthStreamingEventResultCollector, experimentResultCollector,
							new PlainCompleteRHODF("plain2369NW", testStand, FileUtils.UNIV_BENCH_RHODF_MODIFIED), streamer, exeperimentDate);
					break;

				default:

					run(f, comment, experimentNumber, testStand, ligthStreamingEventResultCollector, experimentResultCollector,
							new PlainCompleteRHODF("DEF", testStand, FileUtils.UNIV_BENCH_RHODF_MODIFIED), streamer, exeperimentDate);

			}

		}
	}

	private static void run(String f, String comment, int experimentNumber, TestStand<RSPEngine> testStand,
			StartableCollector<StreamingEventResult> streamingEventResultCollector,
			StartableCollector<ExperimentResultEvent> experimentResultCollector, RSPEngine engine, Streamer<StreamingEvent> streamer, Date d)
			throws ClassNotFoundException, SQLException {

		testStand.build(streamingEventResultCollector, experimentResultCollector, engine, streamer);

		testStand.init();
		try {
			experimentNumber += testStand.run(f, experimentNumber, comment, d);
		} catch (Exception e) {
			log.error(e.getMessage());
			testStand.stop();
		}

		testStand.close();
	}
}
