package it.polimi.main;

import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.collector.saver.CSVEventSaver;
import it.polimi.processing.collector.saver.SQLLiteEventSaver;
import it.polimi.processing.collector.saver.TrigEventSaver;
import it.polimi.processing.collector.saver.VoidSaver;
import it.polimi.processing.enums.BuildingStrategy;
import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.factory.ConstantEventBuilder;
import it.polimi.processing.events.factory.StepEventBuilder;
import it.polimi.processing.events.factory.abstracts.EventBuilder;
import it.polimi.processing.events.interfaces.EventResult;
import it.polimi.processing.events.interfaces.ExperimentResult;
import it.polimi.processing.rspengine.windowed.RSPEngine;
import it.polimi.processing.rspengine.windowed.esper.commons.listener.ResultCollectorListener;
import it.polimi.processing.rspengine.windowed.esper.plain.Plain2369;
import it.polimi.processing.rspengine.windowed.jena.JenaEngine;
import it.polimi.processing.rspengine.windowed.jena.listener.plain.JenaFullListener;
import it.polimi.processing.rspengine.windowed.jena.listener.plain.JenaRhoDFListener;
import it.polimi.processing.rspengine.windowed.jena.listener.plain.JenaSMPLListener;
import it.polimi.processing.streamer.RSPEventStreamer;
import it.polimi.processing.workbench.collector.CollectorEventResult;
import it.polimi.processing.workbench.collector.CollectorExperimentResult;
import it.polimi.processing.workbench.core.RSPTestStand;
import it.polimi.processing.workbench.streamer.NTStreamer;
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

import com.espertech.esper.client.UpdateListener;

@Log4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonMain {
	public static final int PLAIN2369 = 0, JENA = 1, JENARHODF = 2;
	public static final int PLAIN2369NW = 3, JENANW = 4, JENARHODFNW = 5;
	public static final int PLAIN2369NM = 6, JENANM = 7, JENARHODFNM = 8;
	public static final int PLAIN2369NWM = 9, JENANWM = 10, JENARHODFNWM = 11;
	public static final int JENAFULL = 12, JENAFULLNW = 13, JENAFULLNWM = 14;

	public static int CURRENTENGINE;
	public static int EXPERIMENTNUMBER;

	private static String JENASMPLNAME = "jenasmpl";
	private static String JENARHODFNAME = "jenarhodf";
	private static String JENAFULLNAME = "jenafull";
	private static String PLAIN2369NAME = "plain2369";

	public static String[] engineNames = new String[] { PLAIN2369NAME, JENASMPLNAME, JENARHODFNAME,

	PLAIN2369NAME + "NW", JENASMPLNAME + "NW", JENARHODFNAME + "NW",

	PLAIN2369NAME + "NM", JENASMPLNAME + "NM", JENARHODFNAME + "NM",

	PLAIN2369NAME + "NWM", JENASMPLNAME + "NWM", JENARHODFNAME + "NWM",

	JENAFULLNAME };

	public static final String ontologyClass = "Ontology";

	private static RSPEngine engine;

	private static Date exeperimentDate;
	private static String file, comment;

	private static RSPTestStand testStand;
	private static StartableCollector<EventResult> streamingEventResultCollector;
	private static StartableCollector<ExperimentResult> experimentResultCollector;
	private static UpdateListener listener;

	private static final DateFormat dt = new SimpleDateFormat("yyyy_MM_dd");
	private static CSVEventSaver csv = new CSVEventSaver();
	private static CollectorEventResult completeSaver;
	private static CollectorEventResult noTrigSaver;
	private static String whereOutput, whereWindow, outputFileName, windowFileName, experimentDescription;
	private static RSPEventStreamer streamer;
	private static BuildingStrategy MODE;

	public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {
		// String[] files = new String[] { "inputTrigINIT50D1GF0SN1R.trig" };
		// String[] files = new String[] { "inputTrigINIT100D1GF0SN1R.trig" };
		// String[] files = new String[] { "inputTrigINIT250D1GF0SN1R.trig" };

		file = "_CLND_UNIV10INDEX0SEED01000Lines.nt";

		EXPERIMENTNUMBER = Integer.parseInt(args[0]);
		CURRENTENGINE = Integer.parseInt(args[1]);
		MODE = BuildingStrategy.valueOf(args[2]);
		comment = args.length > 3 ? args[3] : "";

		exeperimentDate = FileUtils.d;

		testStand = new RSPTestStand();

		outputFileName = "Result_" + "EN" + EXPERIMENTNUMBER + "_" + comment + "_" + dt.format(exeperimentDate) + "_" + file.split("\\.")[0];
		windowFileName = "Window_" + "EN" + EXPERIMENTNUMBER + "_" + comment + "_" + dt.format(exeperimentDate) + "_" + file.split("\\.")[0];

		String engineName = engineNames[CURRENTENGINE];

		whereOutput = engineName + "/" + outputFileName;
		whereWindow = engineName + "/" + windowFileName;

		experimentDescription = "EXPERIMENT_ON_" + file + "_WITH_ENGINE_" + engineName;

		log.info("output file name will be: [" + whereOutput + "]");
		log.info("window file name will be: [" + whereWindow + "]");

		experimentResultCollector = new CollectorExperimentResult(testStand, new SQLLiteEventSaver());

		noTrigSaver = new CollectorEventResult(testStand, new VoidSaver(), csv, engineName + "/");
		completeSaver = new CollectorEventResult(testStand, new TrigEventSaver(), csv, engineName + "/");

		streamingEventResultCollector = ExecutionEnvirorment.isWritingProtected() ? noTrigSaver : completeSaver;
		EventBuilder<RSPEvent> eb;
		switch (MODE) {
			case CONSTANT:
				eb = new ConstantEventBuilder(1);
				break;
			case STEP:
				eb = new StepEventBuilder(10, 10, 10);
				break;
			default:
				eb = new ConstantEventBuilder(50);
				break;
		}
		streamer = new NTStreamer(testStand, eb);

		log.info("Experiment [" + EXPERIMENTNUMBER + "] of [" + exeperimentDate + "]");

		switch (CURRENTENGINE) {
			case JENA:
				listener = new JenaSMPLListener(FileUtils.UNIV_BENCH_RDFS_MODIFIED, testStand);
				engine = new JenaEngine(engineName, testStand, listener);
				break;
			case JENARHODF:
				listener = new JenaRhoDFListener(FileUtils.UNIV_BENCH_RHODF_MODIFIED, FileUtils.RHODF_RULE_SET_RUNTIME, testStand);
				engine = new JenaEngine(engineName, testStand, listener);

				break;
			case JENAFULL:
				listener = new JenaFullListener(FileUtils.UNIV_BENCH_RHODF_MODIFIED, testStand);
				engine = new JenaEngine(engineName, testStand, listener);
				break;
			case PLAIN2369:
				listener = new ResultCollectorListener(testStand, engineName, 0);
				// listener = new CompleteSoundListener(FileUtils.UNIV_BENCH_RHODF_MODIFIED,
				// FileUtils.RHODF_RULE_SET_RUNTIME, testStand);
				// listener = new JenaRhoDFCSListener(FileUtils.UNIV_BENCH_RHODF_MODIFIED,
				// FileUtils.RHODF_RULE_SET_RUNTIME, testStand);
				engine = new Plain2369(engineName, testStand, FileUtils.UNIV_BENCH_RHODF_MODIFIED, ontologyClass, listener);
				break;

			default:
				engine = null;

		}

		run(file, comment, EXPERIMENTNUMBER, exeperimentDate, experimentDescription);

	}

	private static void run(String f, String comment, int experimentNumber, Date d, String experimentDescription) throws ClassNotFoundException,
			SQLException {

		testStand.build(streamingEventResultCollector, experimentResultCollector, engine, streamer);

		testStand.init();
		try {
			experimentNumber += testStand.run(f, experimentNumber, comment, outputFileName, windowFileName, experimentDescription);
		} catch (Exception e) {

			log.error(e.getMessage());
			testStand.stop();
		}

		testStand.close();
	}
}
