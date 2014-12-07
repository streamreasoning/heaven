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
import it.polimi.processing.rspengine.windowed.esper.plain.events.TEvent;
import it.polimi.processing.rspengine.windowed.jena.JenaEngineGraph;
import it.polimi.processing.rspengine.windowed.jena.JenaEngineStmt;
import it.polimi.processing.rspengine.windowed.jena.JenaEngineTEvent;
import it.polimi.processing.rspengine.windowed.jena.JenaEngineTriple;
import it.polimi.processing.rspengine.windowed.jena.events.GraphEvent;
import it.polimi.processing.rspengine.windowed.jena.events.StatementEvent;
import it.polimi.processing.rspengine.windowed.jena.events.TripleEvent;
import it.polimi.processing.rspengine.windowed.jena.listener.JenaFullListener;
import it.polimi.processing.rspengine.windowed.jena.listener.JenaRhoDFListener;
import it.polimi.processing.rspengine.windowed.jena.listener.JenaSMPLListener;
import it.polimi.processing.streamer.RSPEventStreamer;
import it.polimi.processing.workbench.collector.CollectorEventResult;
import it.polimi.processing.workbench.collector.CollectorExperimentResult;
import it.polimi.processing.workbench.core.RSPTestStand;
import it.polimi.processing.workbench.core.TimeStrategy;
import it.polimi.processing.workbench.streamer.NTStreamer;
import it.polimi.utils.BaseLineInputOrder;
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
	public static int EXPERIMENT_NUMBER;

	private static String JENANAME = "jena";
	private static String SMPL = "smpl";
	private static String RHODF = "rhodf";
	private static String FULL = "full";

	public static String[] engineNames = new String[] { JENANAME + SMPL, JENANAME + RHODF, JENANAME + FULL };
	public static String[] eventTypes = new String[] { TEvent.class.getName(), TripleEvent.class.getName(), StatementEvent.class.getName(),
			GraphEvent.class.getName() };

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

	private static int EXECUTION;

	private static CSVEventSaver csv = new CSVEventSaver();
	private static String whereOutput, whereWindow, outputFileName, windowFileName, experimentDescription;
	private static RSPEventStreamer streamer;
	private static int CURRENTREASONER;
	private static BuildingStrategy EBMODE;
	private static String engineName;
	private static String eventBuilderCodeName;

	private static int eventLimit = 5000;;

	public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {
		file = "_CLND_UNIV10INDEX0SEED0.nt";

		boolean input = Boolean.parseBoolean(args[BaseLineInputOrder.CLI]);

		Scanner in = new Scanner(System.in);

		if (args.length < 5 || input) {
			log.info("Write Experiment Number: ");
			EXPERIMENT_NUMBER = in.nextInt();
			log.info("Write Execution Number: ");
			EXECUTION = in.nextInt();
			log.info("Chose an Engine: 0:PLAIN 1:TRIPLE 2:STMT 3:GRAPH");
			CURRENTENGINE = in.nextInt();
			log.info("Chose a Reasoner: 0:SMPL 1:RHODF 2:FULL ");
			CURRENTREASONER = in.nextInt();
			log.info("Chose Streaming Mode: 0:CONSTANT 1:LINEAR 2:STEP 3:EXP");
			EBMODE = BuildingStrategy.getById(in.nextInt());
			log.info("Add Some Comment?: n:NO or comment");
			String next = in.next().toUpperCase();
			comment = next != "n".toUpperCase() ? next : "";
		} else {
			EXPERIMENT_NUMBER = Integer.parseInt(args[BaseLineInputOrder.EXPERIMENTNUMBER]);
			EXECUTION = Integer.parseInt(args[BaseLineInputOrder.EXECUTIONNUMBER]);
			CURRENTENGINE = Integer.parseInt(args[BaseLineInputOrder.JENA_CURRENTENGINE]);
			CURRENTREASONER = Integer.parseInt(args[BaseLineInputOrder.CURRENTREASONER]);
			EBMODE = BuildingStrategy.getById(Integer.parseInt(args[BaseLineInputOrder.EVENTBUILDER]));
			comment = "n".toUpperCase().equals(args[BaseLineInputOrder.COMMENTS].toUpperCase()) ? "" : args[BaseLineInputOrder.COMMENTS];
		}

		exeperimentDate = FileUtils.d;

		log.info("Experiment [" + EXPERIMENT_NUMBER + "] of [" + exeperimentDate + "]");

		testStand = new RSPTestStand(new TimeStrategy() {

			private int numberEvents = 1;

			@Override
			public boolean apply(RSPEvent e, RSPTestStand ts) {
				boolean process = false;
				RSPEngine rspEngine = ts.getRspEngine();

				if (numberEvents % 5 == 1) {
					double memoryUsage = Memory.getMemoryUsage();
					log.debug("Memory Before Sending [" + memoryUsage + "] On Event " + numberEvents);
					ts.setMemoryB(memoryUsage);
					ts.setTimestamp(System.currentTimeMillis());
					process = rspEngine.process(e);
				} else if (numberEvents % 5 == 0) { // Stream 50 events at time
					double memoryUsage = Memory.getMemoryUsage();
					log.debug("Memory After Sending [" + memoryUsage + "] On Event " + numberEvents);
					ts.setMemoryA(memoryUsage);
					process = ts.processDone();
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

		FileUtils.createOutputFolder("exp" + EXPERIMENT_NUMBER + "/" + engineName);

		String generalName = "EN" + EXPERIMENT_NUMBER + "_" + "EXE" + EXECUTION + "_" + comment + "_" + dt.format(exeperimentDate) + "_"
				+ file.split("\\.")[0] + "R" + CURRENTREASONER + "E" + CURRENTENGINE + eventBuilderCodeName;

		if (input) {
			log.info("Choose Experiment Type: 0:TRIGRESULT 1:LATENCY 2:MEMORY");
			EXPERIMENTTYPE = in.nextInt();
		} else {
			EXPERIMENTTYPE = Integer.parseInt(args[args.length - 1]);
		}

		outputFileName = EXPERIMENTTYPE + "Result_" + generalName;
		windowFileName = EXPERIMENTTYPE + "Window_" + generalName;

		whereOutput = "exp" + EXPERIMENT_NUMBER + "/" + engineName + "/" + outputFileName;
		whereWindow = "exp" + EXPERIMENT_NUMBER + "/" + engineName + "/" + windowFileName;

		log.info("Output file name will be: [" + whereOutput + "]");
		log.info("Window file name will be: [" + whereWindow + "]");

		collectorSelection();

		reasonerSelection();

		engineSelection();

		in.close();

		run(file, comment, EXPERIMENT_NUMBER, exeperimentDate, experimentDescription);

	}

	protected static String streamerSelection(Scanner in) {
		EventBuilder<RSPEvent> eb;
		log.info("Event Builder insert RSPEvent init size");
		int initSize = in.nextInt();
		String code = "EB";
		switch (EBMODE) {
			case CONSTANT:
				code += "C" + initSize;
				log.info("CONSTANT Event Builder");
				eb = new ConstantEventBuilder(initSize, EXPERIMENT_NUMBER);
				break;
			case STEP:
				log.info("STEP Event Builder, insert step height and width");
				int height = in.nextInt();
				int width = in.nextInt();
				eb = new StepEventBuilder(height, width, initSize, EXPERIMENT_NUMBER);
				code += "S" + initSize + "H" + height + "W" + width;
				break;
			default:
				eb = new ConstantEventBuilder(initSize, EXPERIMENT_NUMBER);
				break;

		}

		streamer = new NTStreamer(testStand, eb);
		return code;
	}

	protected static String streamerSelection(String[] args) {
		EventBuilder<RSPEvent> eb;
		int initSize = Integer.parseInt(args[BaseLineInputOrder.INITSIZE]);
		String code = "_EB";
		switch (EBMODE) {
			case CONSTANT:
				code += "K" + initSize;
				log.info("CONSTANT Event Builder Initial Size [" + initSize + "]");
				eb = new ConstantEventBuilder(initSize, EXPERIMENT_NUMBER);
				break;
			case STEP:
				int height = Integer.parseInt(args[BaseLineInputOrder.HEIGHT]);
				int width = Integer.parseInt(args[BaseLineInputOrder.WIDTH]);
				log.info("STEP Event Builder, Initial Size [" + initSize + "] step height [" + height + "] and width[" + width + "]");
				eb = new StepEventBuilder(height, width, initSize, EXPERIMENT_NUMBER);
				code += "S" + initSize + "H" + height + "W" + width;
				break;
			default:
				eb = new ConstantEventBuilder(initSize, EXPERIMENT_NUMBER);
				break;

		}

		streamer = new NTStreamer(testStand, eb, eventLimit);
		return code;
	}

	protected static void engineSelection() {
		switch (CURRENTENGINE) {
			case JENAPLAIN:
				engine = new JenaEngineTEvent(engineName, testStand, listener);
				break;
			case JENATRIPLE:
				engine = new JenaEngineTriple(engineName, testStand, listener);
				break;
			case JENASTMT:
				engine = new JenaEngineStmt(engineName, testStand, listener);
				break;
			case JENAGRAPH:
				engine = new JenaEngineGraph(engineName, testStand, listener);
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

		TrigEventSaver trig = new TrigEventSaver();
		VoidSaver voids = new VoidSaver();
		switch (EXPERIMENTTYPE) {
			case MEMORY:
				ExecutionEnvirorment.memoryEnabled = true;
				streamingEventResultCollector = new CollectorEventResult(testStand, voids, csv, engineName + "/");
				break;
			case LATENCY:
				streamingEventResultCollector = new CollectorEventResult(testStand, voids, csv, engineName + "/");
				break;
			case RESULT:
				streamingEventResultCollector = new CollectorEventResult(testStand, trig, csv, engineName + "/");
			default:
				streamingEventResultCollector = new CollectorEventResult(testStand, trig, csv, engineName + "/");
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
