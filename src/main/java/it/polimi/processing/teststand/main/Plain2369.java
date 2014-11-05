package it.polimi.processing.teststand.main;

import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.collector.saver.CSVEventSaver;
import it.polimi.processing.collector.saver.SQLLiteEventSaver;
import it.polimi.processing.collector.saver.TrigEventSaver;
import it.polimi.processing.events.StreamingEvent;
import it.polimi.processing.events.result.ExperimentResultEvent;
import it.polimi.processing.events.result.StreamingEventResult;
import it.polimi.processing.rspengine.RSPEngine;
import it.polimi.processing.rspengine.esper.plain.PlainCompleteRHODF;
import it.polimi.processing.streamer.Streamer;
import it.polimi.processing.teststand.collector.CollectorEventResult;
import it.polimi.processing.teststand.collector.CollectorExperimentResult;
import it.polimi.processing.teststand.core.TestStand;
import it.polimi.processing.teststand.streamer.TriGStreamer;
import it.polimi.utils.FileUtils;

import java.sql.SQLException;

public class Plain2369 {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException {

		int experimentNumber = 0;
		String[] files = new String[] { "inputTrigD3GF10000.trig" };

		TestStand<RSPEngine> testStand = new TestStand<RSPEngine>();

		StartableCollector<StreamingEventResult> streamingEventResultCollector = new CollectorEventResult(testStand, new TrigEventSaver(),
				new CSVEventSaver());
		StartableCollector<ExperimentResultEvent> experimentResultCollector = new CollectorExperimentResult(testStand, new SQLLiteEventSaver());

		RSPEngine engine = new PlainCompleteRHODF("plain2369", testStand, FileUtils.UNIV_BENCH_RHODF_MODIFIED);

		Streamer<StreamingEvent> streamer = new TriGStreamer<StreamingEvent>(testStand);

		testStand.build(streamingEventResultCollector, experimentResultCollector, engine, streamer);

		testStand.init();
		try {
			for (String f : files) {

				experimentNumber += testStand.run(f, experimentNumber);
			}
		} catch (Exception e) {
			testStand.stop();
		}

		testStand.close();

	}

}
