package it.polimi;

import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.collector.saver.CSVEventSaver;
import it.polimi.processing.collector.saver.SQLLiteEventSaver;
import it.polimi.processing.collector.saver.TrigEventSaver;
import it.polimi.processing.collector.saver.VoidSaver;
import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.interfaces.EventResult;
import it.polimi.processing.events.interfaces.ExperimentResult;
import it.polimi.processing.rspengine.RSPEngine;
import it.polimi.processing.rspengine.esper.plain.Plain2369;
import it.polimi.processing.rspengine.jena.JenaEngine;
import it.polimi.processing.rspengine.jena.JenaEngineRhoDF;
import it.polimi.processing.rspengine.jena.JenaEsperSMPL;
import it.polimi.processing.streamer.Streamer;
import it.polimi.processing.teststand.collector.CollectorEventResult;
import it.polimi.processing.teststand.collector.CollectorExperimentResult;
import it.polimi.processing.teststand.core.TestStand;
import it.polimi.processing.teststand.streamer.TriGStreamer;
import it.polimi.utils.ExecutionEnvirorment;
import it.polimi.utils.FileUtils;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonMain {
	public static final int PLAIN2369 = 0, JENA = 1, JENARHODF = 2;
	public static final int PLAIN2369NW = 3, JENANW = 4, JENARHODFNW = 5;
	public static final int PLAIN2369NM = 6, JENANM = 7, JENARHODFNM = 8;
	public static final int PLAIN2369NWM = 9, JENANWM = 10, JENARHODFNWM = 11;
	public static int CURRENTENGINE;
	public static int EXPERIMENTNUMBER;
	public static final String ontologyClass = "Ontology";

	private static StartableCollector<EventResult> streamingEventResultCollector;
	private static TestStand<RSPEngine<RSPEvent>> testStand;

	public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {
		// String[] files = new String[] { "inputTrigINIT50D1GF0SN1R.trig" };
		// String[] files = new String[] { "inputTrigINIT100D1GF0SN1R.trig" };
		// String[] files = new String[] { "inputTrigINIT250D1GF0SN1R.trig" };
		String[] files = new String[] { "inputTrigINIT50D1GF0SN1R1.trig" };

		EXPERIMENTNUMBER = Integer.parseInt(args[0]);
		CURRENTENGINE = Integer.parseInt(args[1]);
		String comment = args.length > 2 ? args[2] : "";

		Date exeperimentDate = FileUtils.d;

		testStand = new TestStand<RSPEngine<RSPEvent>>();

		StartableCollector<ExperimentResult> experimentResultCollector = new CollectorExperimentResult(testStand, new SQLLiteEventSaver());

		String JENASMPLNAME = "jenasmpl";
		String JENARHODFNAME = "jenarhodf";
		String PLAIN2369NAME = "plain2369";
		RSPEngine<RSPEvent> engine;
		for (String f : files) {
			switch (CURRENTENGINE) {
				case JENA:
					engine = new JenaEsperSMPL(JENASMPLNAME, testStand, FileUtils.UNIV_BENCH_RDFS_MODIFIED);
					break;
				case JENARHODF:
					engine = new JenaEngineRhoDF(JENARHODFNAME, FileUtils.UNIV_BENCH_RHODF_MODIFIED, FileUtils.RHODF_RULE_SET_RUNTIME, testStand);
					break;
				case PLAIN2369:
					engine = new Plain2369(PLAIN2369NAME, testStand, FileUtils.UNIV_BENCH_RHODF_MODIFIED, ontologyClass);
					break;

				// NO output with memory
				case JENANW:
					engine = new JenaEngine(JENASMPLNAME + "NW", testStand);
					break;
				case JENARHODFNW:
					engine = new JenaEngineRhoDF(JENARHODFNAME + "NW", FileUtils.UNIV_BENCH_RHODF_MODIFIED, FileUtils.RHODF_RULE_SET_RUNTIME,
							testStand);
					break;
				case PLAIN2369NW:
					engine = new Plain2369(PLAIN2369NAME + "NW", testStand, FileUtils.UNIV_BENCH_RHODF_MODIFIED, ontologyClass);
					break;

				// NO Memory with output
				case JENANM:
					engine = new JenaEngine(JENASMPLNAME + "NM", testStand);
					break;
				case JENARHODFNM:
					engine = new JenaEngineRhoDF(JENARHODFNAME + "NM", FileUtils.UNIV_BENCH_RHODF_MODIFIED, FileUtils.RHODF_RULE_SET_RUNTIME,
							testStand);
					break;
				case PLAIN2369NM:
					engine = new Plain2369(PLAIN2369NAME + "NM", testStand, FileUtils.UNIV_BENCH_RHODF_MODIFIED, ontologyClass);
					break;

				// NO memory no Output
				case JENANWM:
					engine = new JenaEngine(JENASMPLNAME + "NWM", testStand);
					break;
				case JENARHODFNWM:
					engine = new JenaEngineRhoDF(JENARHODFNAME + "NWM", FileUtils.UNIV_BENCH_RHODF_MODIFIED, FileUtils.RHODF_RULE_SET_RUNTIME,
							testStand);
					break;
				case PLAIN2369NWM:
					engine = new Plain2369(PLAIN2369NAME + "NWM", testStand, FileUtils.UNIV_BENCH_RHODF_MODIFIED, ontologyClass);
					break;

				default:
					engine = null;

			}

			Streamer<RSPEvent> streamer = new TriGStreamer(testStand);

			log.info("Experiment [" + EXPERIMENTNUMBER + "] of [" + exeperimentDate + "]");

			DateFormat dt = new SimpleDateFormat("yyyy_MM_dd");
			String outputFileName = "Result_" + "EN" + EXPERIMENTNUMBER + "_" + comment + "_" + dt.format(exeperimentDate) + "_" + f.split("\\.")[0];

			String where = engine.getName() + "/" + outputFileName;

			log.info("output file name will be: [" + where + "]");
			streamingEventResultCollector = ExecutionEnvirorment.isWritingProtected() ? new CollectorEventResult(testStand, new VoidSaver(),
					new CSVEventSaver(), where) : new CollectorEventResult(testStand, new TrigEventSaver(), new CSVEventSaver(), where);

			run(f, comment, EXPERIMENTNUMBER, testStand, streamingEventResultCollector, experimentResultCollector, engine, streamer, exeperimentDate);

		}
	}

	private static void run(String f, String comment, int experimentNumber, TestStand<RSPEngine<RSPEvent>> testStand,
			StartableCollector<EventResult> streamingEventResultCollector, StartableCollector<ExperimentResult> experimentResultCollector,
			RSPEngine<RSPEvent> engine, Streamer<RSPEvent> streamer, Date d) throws ClassNotFoundException, SQLException {

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
