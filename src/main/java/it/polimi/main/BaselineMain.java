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
import it.polimi.processing.workbench.core.TimeStrategy;
import it.polimi.processing.workbench.streamer.NTStreamer;
import it.polimi.utils.ExecutionEnvirorment;
import it.polimi.utils.FileUtils;
import it.polimi.utils.Memory;

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

	private static int EXPERIMENTTYPE;
	private static final int RESULT = 0;
	private static final int LATENCY = 1;
	private static final int MEMORY = 2;

	private static CSVEventSaver csv = new CSVEventSaver();
	private static String whereOutput, whereWindow, outputFileName, windowFileName, experimentDescription;
	private static RSPEventStreamer streamer;
	private static int CURRENTREASONER;
	private static BuildingStrategy MODE;
	private static String engineName;
	private static String eventBuilderCodeName;

	public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {
		file = "_CLND_UNIV10INDEX0SEED0.nt";

		boolean input = false;
		Scanner in = new Scanner(System.in);

		if (args.length < 5 || input) {
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
			MODE = BuildingStrategy.getById(Integer.parseInt(args[3]));
			comment = "n".toUpperCase().equals(args[4].toUpperCase()) ? "" : args[4];
		}

		exeperimentDate = FileUtils.d;

		log.info("Experiment [" + EXPERIMENTNUMBER + "] of [" + exeperimentDate + "]");

		testStand = new RSPTestStand(new TimeStrategy() {

			private int numberEvents = 0;

			@Override
			public boolean apply(RSPEvent e, RSPTestStand ts) {
				boolean process = false;
				if (numberEvents == 0 || numberEvents % 5 == 1) {
					ts.setMemoryB(Memory.getMemoryUsage());
					ts.setTimestamp(System.currentTimeMillis());
				}
				RSPEngine rspEngine = ts.getRspEngine();
				if (numberEvents != 0 && numberEvents % 5 == 0) { // Stream 50 events at time
					ts.setMemoryA(Memory.getMemoryUsage());
					process = ts.processDone();
					ts.setMemoryA(0D);
					ts.setMemoryB(0D);
					ts.setTimestamp(0L);
					ts.setResultTimestamp(0L);
				} else {
					process = rspEngine.process(e);

				}
				numberEvents++;
				rspEngine.progress(5); // for rspesperengine move times forward according to the
										// size of the current window
				return process;
			}
		});

		eventBuilderCodeName = input ? streamerSelection(in) : streamerSelection(args);

		engineName = engineNames[CURRENTENGINE];

		experimentDescription = "EXPERIMENT_ON_" + file + "_WITH_ENGINE_" + engineName + "EVENT_" + CURRENTENGINE;

		FileUtils.createOutputFolder("exp" + EXPERIMENTNUMBER + "/" + engineName);

		String generalName = "EN" + EXPERIMENTNUMBER + "_" + comment + "_" + dt.format(exeperimentDate) + "_" + file.split("\\.")[0] + "R"
				+ CURRENTREASONER + "E" + CURRENTENGINE + eventBuilderCodeName;

		outputFileName = "Result_" + generalName;
		windowFileName = "Window_" + generalName;

		whereOutput = "exp" + EXPERIMENTNUMBER + "/" + engineName + "/" + outputFileName;
		whereWindow = "exp" + EXPERIMENTNUMBER + "/" + engineName + "/" + windowFileName;

		log.info("Output file name will be: [" + whereOutput + "]");
		log.info("Window file name will be: [" + whereWindow + "]");

		if (input) {
			log.info("Choose Experiment Type: 0:TRIGRESULT 1:LATENCY 2:MEMORY");
			EXPERIMENTTYPE = in.nextInt();
		} else {
			EXPERIMENTTYPE = Integer.parseInt(args[args.length - 1]);
		}

		collectorSelection();

		reasonerSelection();

		engineSelection();

		in.close();

		run(file, comment, EXPERIMENTNUMBER, exeperimentDate, experimentDescription);

	}

	protected static String streamerSelection(Scanner in) {
		EventBuilder<RSPEvent> eb;
		log.debug("Event Builder insert RSPEvent init size");
		int initSize = in.nextInt();
		String code = "EB";
		switch (MODE) {
			case CONSTANT:
				code += "C" + initSize;
				log.info("CONSTANT Event Builder");
				eb = new ConstantEventBuilder(initSize);
				break;
			case STEP:
				log.info("STEP Event Builder, insert step height and width");
				int height = in.nextInt();
				int width = in.nextInt();
				eb = new StepEventBuilder(height, width, initSize);
				code += "S" + initSize + "H" + height + "W" + width;
				break;
			default:
				eb = new ConstantEventBuilder(initSize);
				break;

		}

		streamer = new NTStreamer(testStand, eb);
		return code;
	}

	protected static String streamerSelection(String[] args) {
		EventBuilder<RSPEvent> eb;
		int initSize = Integer.parseInt(args[5]);
		String code = "EB";
		switch (MODE) {
			case CONSTANT:
				code += "C" + initSize;
				log.info("CONSTANT Event Builder Initial Size [" + initSize + "]");
				eb = new ConstantEventBuilder(initSize);
				break;
			case STEP:
				int height = Integer.parseInt(args[6]);
				int width = Integer.parseInt(args[7]);
				log.info("STEP Event Builder, Initial Size [" + initSize + "] step height [" + height + "] and width[" + width + "]");
				eb = new StepEventBuilder(height, width, initSize);
				code += "S" + initSize + "H" + height + "W" + width;
				break;
			default:
				eb = new ConstantEventBuilder(initSize);
				break;

		}

		streamer = new NTStreamer(testStand, eb);
		return code;
	}

	protected static void engineSelection() {
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

	protected static void collectorSelection() throws SQLException, ClassNotFoundException {
		experimentResultCollector = new CollectorExperimentResult(testStand, new SQLLiteEventSaver());

		switch (EXPERIMENTTYPE) {
			case MEMORY:
				ExecutionEnvirorment.memoryEnabled = true;
				streamingEventResultCollector = new CollectorEventResult(testStand, new VoidSaver(), csv, engineName + "/");
				break;
			case LATENCY:
				streamingEventResultCollector = new CollectorEventResult(testStand, new VoidSaver(), csv, engineName + "/");
				break;
			case RESULT:
				streamingEventResultCollector = new CollectorEventResult(testStand, new TrigEventSaver(), csv, engineName + "/");
			default:
				streamingEventResultCollector = new CollectorEventResult(testStand, new TrigEventSaver(), csv, engineName + "/");
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
