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
import it.polimi.processing.rspengine.windowed.jena.JenaEngine;
import it.polimi.processing.rspengine.windowed.jena.events.GraphEvent;
import it.polimi.processing.rspengine.windowed.jena.events.StatementEvent;
import it.polimi.processing.rspengine.windowed.jena.events.TripleEvent;
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
import java.util.Scanner;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

import com.espertech.esper.client.UpdateListener;

@Log4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BaselineMain {

	// EVENT TYPES
	public static final int JENAPLAIN = 0, JENATRIPLE = 1, JENASTMT = 2, JENAGRAPH = 3;
	// REASONER
	public static final int JENAFULL = 0, JENARHODF = 1, JENASMPL = 2;

	public static int CURRENTENGINE;
	public static int EXPERIMENTNUMBER;

	private static String JENANAME = "jena";
	private static String SMPL = "smpl";
	private static String RHODF = "rhodf";
	private static String FULL = "full";

	public static String[] engineNames = new String[] { JENANAME + SMPL, JENANAME + RHODF, JENANAME + FULL };

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
	private static int CURRENTREASONER;
	private static BuildingStrategy MODE;

	public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {
		// String[] files = new String[] { "inputTrigINIT50D1GF0SN1R.trig" };
		// String[] files = new String[] { "inputTrigINIT100D1GF0SN1R.trig" };
		// String[] files = new String[] { "inputTrigINIT250D1GF0SN1R.trig" };

		file = "_CLND_UNIV10INDEX0SEED0.nt";
		Scanner in = new Scanner(System.in);
		if (args.length < 5) {
			log.info("Write Experiment Number: ");
			EXPERIMENTNUMBER = in.nextInt();
			log.info("Chose an Engine: 0:PLAIN 1:TRIPLE 2:STMT 3:GRAPH");
			CURRENTENGINE = in.nextInt();
			log.info("Chose a Reasoner: 0:SMPL 1:RHODF 2:FULL ");
			CURRENTREASONER = in.nextInt();
			log.info("Chose Streaming Mode: 0:CONSTANT 1:LINEAR 2:STEP 3:EXP");
			MODE = BuildingStrategy.getById(in.nextInt());
			log.info("Add Some Comment?: n:NO or comment");
			String next = in.next().toUpperCase();
			comment = next != "n".toUpperCase() ? next : "";
		} else {
			EXPERIMENTNUMBER = Integer.parseInt(args[0]);
			CURRENTENGINE = Integer.parseInt(args[1]);
			CURRENTREASONER = Integer.parseInt(args[2]);
			MODE = BuildingStrategy.valueOf(args[3]);
			comment = args.length > 2 ? args[4] : "";
		}
		exeperimentDate = FileUtils.d;

		testStand = new RSPTestStand();

		outputFileName = "Result_" + "EN" + EXPERIMENTNUMBER + "_" + comment + "_" + dt.format(exeperimentDate) + "_" + file.split("\\.")[0];
		windowFileName = "Window_" + "EN" + EXPERIMENTNUMBER + "_" + comment + "_" + dt.format(exeperimentDate) + "_" + file.split("\\.")[0];

		String engineName = engineNames[CURRENTENGINE];

		FileUtils.createOutputFolder("exp" + EXPERIMENTNUMBER + "/" + engineName);

		streamerSelection(args, in);

		whereOutput = "exp" + EXPERIMENTNUMBER + "/" + engineName + "/" + outputFileName;
		whereWindow = "exp" + EXPERIMENTNUMBER + "/" + engineName + "/" + windowFileName;

		experimentDescription = "EXPERIMENT_ON_" + file + "_WITH_ENGINE_" + engineName + "EVENT_" + CURRENTENGINE;

		log.info("Output file name will be: [" + whereOutput + "]");
		log.info("Window file name will be: [" + whereWindow + "]");

		experimentResultCollector = new CollectorExperimentResult(testStand, new SQLLiteEventSaver());

		noTrigSaver = new CollectorEventResult(testStand, new VoidSaver(), csv, engineName + "/");
		completeSaver = new CollectorEventResult(testStand, new TrigEventSaver(), csv, engineName + "/");

		streamingEventResultCollector = ExecutionEnvirorment.isWritingProtected() ? noTrigSaver : completeSaver;

		log.info("Experiment [" + EXPERIMENTNUMBER + "] of [" + exeperimentDate + "]");

		reasonerSelection();

		engineSelection(engineName);

		in.close();

		run(file, comment, EXPERIMENTNUMBER, exeperimentDate, experimentDescription);

	}

	protected static void streamerSelection(String[] args, Scanner in) {
		EventBuilder<RSPEvent> eb;
		log.info("Event Builder insert RSPEvent init size");
		int initSize = args.length < 5 ? in.nextInt() : 10;

		switch (MODE) {
			case CONSTANT:
				log.info("CONSTANT Event Builder");
				eb = new ConstantEventBuilder(initSize);
				break;
			case STEP:
				log.info("STEP Event Builder, insert step height and width");
				int height = args.length < 5 ? in.nextInt() : 10;
				int width = args.length < 5 ? in.nextInt() : 10;
				eb = new StepEventBuilder(height, width, initSize);
				break;
			default:
				eb = new ConstantEventBuilder(initSize);
				break;
		}

		streamer = new NTStreamer(testStand, eb);
	}

	protected static void reasonerSelection() {
		switch (CURRENTREASONER) {
			case JENARHODF:
				listener = new JenaRhoDFListener(FileUtils.UNIV_BENCH_RHODF_MODIFIED, FileUtils.RHODF_RULE_SET_RUNTIME, testStand);
				break;
			case JENASMPL:
				listener = new JenaSMPLListener(FileUtils.UNIV_BENCH_RDFS_MODIFIED, testStand);
				break;
			case JENAFULL:
				listener = new JenaFullListener(FileUtils.UNIV_BENCH_RHODF_MODIFIED, testStand);
				break;
			default:
				listener = new JenaRhoDFListener(FileUtils.UNIV_BENCH_RHODF_MODIFIED, FileUtils.RHODF_RULE_SET_RUNTIME, testStand);
		}
	}

	protected static void engineSelection(String engineName) {
		switch (CURRENTENGINE) {
			case JENAPLAIN:
				engine = new JenaEngine(engineName, testStand, listener);
				break;
			case JENATRIPLE:
				engine = new JenaEngine(engineName, testStand, listener, TripleEvent.class);
				break;
			case JENASTMT:
				engine = new JenaEngine(engineName, testStand, listener, StatementEvent.class);
				break;
			case JENAGRAPH:
				engine = new JenaEngine(engineName, testStand, listener, GraphEvent.class);
				break;
			default:
				engine = null;

		}
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
