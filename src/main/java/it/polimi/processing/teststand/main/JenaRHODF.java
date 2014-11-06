package it.polimi.processing.teststand.main;

import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.collector.saver.CSVEventSaver;
import it.polimi.processing.collector.saver.SQLLiteEventSaver;
import it.polimi.processing.collector.saver.TrigEventSaver;
import it.polimi.processing.events.StreamingEvent;
import it.polimi.processing.events.result.ExperimentResultEvent;
import it.polimi.processing.events.result.StreamingEventResult;
import it.polimi.processing.rspengine.RSPEngine;
import it.polimi.processing.rspengine.jena.JenaEngineRhoDF;
import it.polimi.processing.streamer.Streamer;
import it.polimi.processing.teststand.collector.CollectorEventResult;
import it.polimi.processing.teststand.collector.CollectorExperimentResult;
import it.polimi.processing.teststand.core.TestStand;
import it.polimi.processing.teststand.streamer.TriGStreamer;

import java.sql.SQLException;

public class JenaRHODF {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		String[] files = new String[] { "inputTrigD5GF100.trig" };
		int experimentNumber = 0;
		TestStand<RSPEngine> testStand = new TestStand<RSPEngine>();

		StartableCollector<StreamingEventResult> streamingEventResultCollector = new CollectorEventResult(testStand, new TrigEventSaver(),
				new CSVEventSaver());
		StartableCollector<ExperimentResultEvent> experimentResultCollector = new CollectorExperimentResult(testStand, new SQLLiteEventSaver());
		RSPEngine engine = new JenaEngineRhoDF("jenarhodf", testStand);
		Streamer<StreamingEvent> streamer = new TriGStreamer<StreamingEvent>(testStand);

		testStand.build(streamingEventResultCollector, experimentResultCollector, engine, streamer);

		testStand.init();
		try {
			for (String f : files) {

				experimentNumber += testStand.run(f, experimentNumber);
			}
		} catch (Exception e) {
			e.printStackTrace();
			testStand.stop();
		}

		testStand.close();
	}

	public static void run(String f, int experimentNumber, TestStand<RSPEngine> testStand,
			StartableCollector<StreamingEventResult> streamingEventResultCollector,
			StartableCollector<ExperimentResultEvent> experimentResultCollector, RSPEngine engine, Streamer<StreamingEvent> streamer)
			throws ClassNotFoundException, SQLException {

		testStand.build(streamingEventResultCollector, experimentResultCollector, engine, streamer);

		testStand.init();
		try {
			experimentNumber += testStand.run(f, experimentNumber);
		} catch (Exception e) {
			e.printStackTrace();
			testStand.stop();
		}

		testStand.close();
	}
}
