package it.polimi;

import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.collector.saver.CSVEventSaver;
import it.polimi.processing.collector.saver.SQLLiteEventSaver;
import it.polimi.processing.collector.saver.TrigEventSaver;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonMain {
	private static final int PLAIN2369 = 0, JENA = 1, JENARHODF = 2;

	public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {
		String[] files = new String[] { "inputTrigINIT100D1GF0I.trig" };

		int rspengine = 1;

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date exeperimentDate = df.parse("2014-11-05");
		int experimentNumber = 0;
		TestStand<RSPEngine> testStand = new TestStand<RSPEngine>();

		StartableCollector<StreamingEventResult> streamingEventResultCollector = new CollectorEventResult(testStand, new TrigEventSaver(),
				new CSVEventSaver());

		StartableCollector<ExperimentResultEvent> experimentResultCollector = new CollectorExperimentResult(testStand, new SQLLiteEventSaver());

		Streamer<StreamingEvent> streamer = new TriGStreamer<StreamingEvent>(testStand);

		for (String f : files) {
			switch (rspengine) {
				case JENA:
					run(f, experimentNumber, testStand, streamingEventResultCollector, experimentResultCollector, new JenaEngine("jenasmpl",
							testStand), streamer, exeperimentDate);
				case JENARHODF:
					run(f, experimentNumber, testStand, streamingEventResultCollector, experimentResultCollector, new JenaEngineRhoDF("jenarhodf",
							testStand), streamer, exeperimentDate);
				case PLAIN2369:
					run(f, experimentNumber, testStand, streamingEventResultCollector, experimentResultCollector, new PlainCompleteRHODF("plain2369",
							testStand, FileUtils.UNIV_BENCH_RHODF_MODIFIED), streamer, exeperimentDate);
				default:
					break;
			}

		}
	}

	public static void run(String f, int experimentNumber, TestStand<RSPEngine> testStand,
			StartableCollector<StreamingEventResult> streamingEventResultCollector,
			StartableCollector<ExperimentResultEvent> experimentResultCollector, RSPEngine engine, Streamer<StreamingEvent> streamer, Date d)
			throws ClassNotFoundException, SQLException {

		testStand.build(streamingEventResultCollector, experimentResultCollector, engine, streamer);

		testStand.init();
		try {
			experimentNumber += testStand.run(f, experimentNumber, "", d);
		} catch (Exception e) {
			e.printStackTrace();
			testStand.stop();
		}

		testStand.close();
	}
}
