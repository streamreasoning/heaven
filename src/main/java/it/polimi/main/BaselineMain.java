package it.polimi.main;

import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.enums.EventBuilderMode;
import it.polimi.processing.enums.ExperimentType;
import it.polimi.processing.enums.Reasoning;
import it.polimi.processing.ets.collector.CollectorEventResult;
import it.polimi.processing.ets.collector.CollectorExperimentResult;
import it.polimi.processing.ets.core.RSPTestStand;
import it.polimi.processing.ets.streamer.NTStreamer;
import it.polimi.processing.ets.timecontrol.InternalTiming;
import it.polimi.processing.ets.timecontrol.NaiveStrategy;
import it.polimi.processing.ets.timecontrol.TimeStrategy;
import it.polimi.processing.events.Experiment;
import it.polimi.processing.events.RSPTripleSet;
import it.polimi.processing.events.factory.ConstantEventBuilder;
import it.polimi.processing.events.factory.RandomEventBuilder;
import it.polimi.processing.events.factory.StepEventBuilder;
import it.polimi.processing.events.factory.abstracts.EventBuilder;
import it.polimi.processing.events.interfaces.ExperimentResult;
import it.polimi.processing.events.results.EventResult;
import it.polimi.processing.rspengine.abstracts.RSPEngine;
import it.polimi.processing.rspengine.jena.JenaRSPEngineFactory;
import it.polimi.processing.rspengine.jena.JenaReasoningListenerFactory;
import it.polimi.processing.rspengine.jena.enums.JenaEventType;
import it.polimi.processing.rspengine.jena.enums.Reasoner;
import it.polimi.processing.streamer.RSPTripleSetStreamer;
import it.polimi.services.FileService;
import it.polimi.services.system.ExecutionEnvirorment;
import it.polimi.services.system.GetPropertyValues;
import it.polimi.utils.FileUtils;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

import com.espertech.esper.client.UpdateListener;

@Log4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BaselineMain {

	// EVENT TYPES

	private static JenaEventType CEP_EVENT_TYPE;
	private static int EXPERIMENT_NUMBER;

	private static RSPEngine engine;

	private static Date EXPERIMENT_DATE;
	private static String file, COMMENT;

	private static RSPTestStand testStand;
	private static StartableCollector<EventResult> streamingEventResultCollector;
	private static StartableCollector<ExperimentResult> experimentResultCollector;
	private static UpdateListener listener;

	private static final DateFormat DT = new SimpleDateFormat("yyyy_MM_dd");

	private static ExperimentType EXPERIMENT_TYPE;

	private static int EXECUTION_NUMBER;

	private static String whereOutput, whereWindow, outputFileName, windowFileName, experimentDescription;
	private static RSPTripleSetStreamer streamer;
	private static Reasoner CURRENT_REASONER;
	private static Reasoning REASONING;
	private static EventBuilderMode STREAMING_MODE;

	private static String engineName;
	private static String eventBuilderCodeName;
	private static int X;
	private static int Y;
	private static int INIT_SIZE;
	private static int EVENTS;
	private static String RSPENGINE;
	public static String INPUT_PROPERTIES;
	private static String wINDOWSIZE;

	public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {

		if (args.length >= 1) {
			INPUT_PROPERTIES = args[0];
			log.info(Arrays.deepToString(args));
		}

		file = GetPropertyValues.getProperty(GetPropertyValues.DEFAULT_INPUT_FILE);

		EXPERIMENT_NUMBER = GetPropertyValues.getIntegerProperty("experiment_number");
		EXPERIMENT_DATE = GetPropertyValues.getDateProperty("experiment_date");

		EXECUTION_NUMBER = GetPropertyValues.getIntegerProperty("execution_number");
		COMMENT = GetPropertyValues.contains("comment") ? GetPropertyValues.getProperty("comment") : "";

		RSPENGINE = GetPropertyValues.getProperty("current_engine");

		if (RSPENGINE.equals("JENA")) {
			CURRENT_REASONER = GetPropertyValues.getEnumProperty(Reasoner.class, "jena_current_reasoner");
			engineName = CURRENT_REASONER.name().toLowerCase();
			CEP_EVENT_TYPE = GetPropertyValues.getEnumProperty(JenaEventType.class, "cep_event_type");
			REASONING = Reasoning.NAIVE;
			// GetPropertyValues.getEnumProperty(Reasoning.class, "reasoning_mode");
		}

		EVENTS = GetPropertyValues.getIntegerProperty("max_event_stream");
		STREAMING_MODE = GetPropertyValues.getEnumProperty(EventBuilderMode.class, "streaming_mode");
		INIT_SIZE = GetPropertyValues.getIntegerProperty("init_size");
		X = GetPropertyValues.getIntegerProperty("x_size");
		Y = GetPropertyValues.getIntegerProperty("y_size");

		log.info("Experiment [" + EXPERIMENT_NUMBER + "] on [" + file + "] of [" + EXPERIMENT_DATE + "] Number of Events [" + EVENTS + "]");

		TimeStrategy strategy = (GetPropertyValues.getBooleanProperty("external_time_control_on")) ? timeStrategySelection() : new InternalTiming();
		testStand = new RSPTestStand(strategy);

		eventBuilderCodeName = streamerSelection();

		experimentDescription = "EXPERIMENT_ON_" + file + "_WITH_ENGINE_" + engineName + "EVENT_" + CEP_EVENT_TYPE;

		FileService.createOutputFolder(FileUtils.daypath + "/exp" + EXPERIMENT_NUMBER + "/" + engineName);

		wINDOWSIZE = GetPropertyValues.getProperty("rsp_events_in_window");

		String generalName = "EN" + EXPERIMENT_NUMBER + "_" + "EXE" + EXECUTION_NUMBER + "_" + COMMENT + "_" + DT.format(EXPERIMENT_DATE) + "_"
				+ file.split("\\.")[0] + "_" + CURRENT_REASONER + "_" + CEP_EVENT_TYPE + "_INIT" + INIT_SIZE + eventBuilderCodeName + "_EW_"
				+ wINDOWSIZE;

		EXPERIMENT_TYPE = GetPropertyValues.getEnumProperty(ExperimentType.class, "experiment_type");
		outputFileName = EXPERIMENT_TYPE.ordinal() + "Result_" + generalName;
		windowFileName = EXPERIMENT_TYPE.ordinal() + "Window_" + generalName;

		whereOutput = "exp" + EXPERIMENT_NUMBER + "/" + engineName + "/" + outputFileName;
		whereWindow = "exp" + EXPERIMENT_NUMBER + "/" + engineName + "/" + windowFileName;

		log.info("Output file name will be: ["
				+ whereOutput.replace("0Result", "RESLOG").replace("0Window", "WINLOG").replace("1Result", "LATLOG").replace("1Window", "WINLATLOG")
						.replace("2Result", "MEMLOG").replace("2Window", "WINMEMLOG") + "]");
		log.info("Window file name will be: ["
				+ whereWindow.replace("0Result", "RESLOG").replace("0Window", "WINLOG").replace("1Result", "LATLOG").replace("1Window", "WINLATLOG")
						.replace("2Result", "MEMLOG").replace("2Window", "WINMEMLOG") + "]");

		reasonerSelection();
		collectorSelection();
		jenaEngineSelection();

		run(file, COMMENT, EXPERIMENT_NUMBER, EXPERIMENT_DATE, experimentDescription);

	}

	private static TimeStrategy timeStrategySelection() {
		return new NaiveStrategy();
	}

	protected static String streamerSelection() {
		EventBuilder<RSPTripleSet> eb = null;

		String code = "_EB";
		String message = "Event Builder Selection: [" + STREAMING_MODE + "] [" + INIT_SIZE + "] ";

		switch (STREAMING_MODE) {
			case CONSTANT:
				code += "K" + INIT_SIZE;
				eb = new ConstantEventBuilder(INIT_SIZE, EXPERIMENT_NUMBER);
				break;
			case STEP:
				message += " Heigh [" + X + "] Width [" + Y + "] ";
				eb = new StepEventBuilder(X, Y, INIT_SIZE, EXPERIMENT_NUMBER);
				code += "S" + INIT_SIZE + "H" + X + "W" + Y;
				break;
			case RANDOM:
				message += " RND";
				eb = new RandomEventBuilder(Y, INIT_SIZE, EXPERIMENT_NUMBER);
				code += "S" + INIT_SIZE + "H" + X + "W" + Y;
				break;
			default:
				message = "Not valid case [" + STREAMING_MODE + "]";
		}

		log.info(message);
		if (eb != null) {
			streamer = new NTStreamer(testStand, eb, EVENTS);
			return code;
		}
		throw new IllegalArgumentException("Not valid case [" + STREAMING_MODE + "]");
	}

	protected static void jenaEngineSelection() {
		String message = "Engine Selection: [" + CEP_EVENT_TYPE + "] [" + engineName.toUpperCase() + "] ";
		boolean incremental = Reasoning.INCREMENTAL.equals(REASONING);
		switch (CEP_EVENT_TYPE) {
			case TEVENT:
				engine = incremental ? JenaRSPEngineFactory.getIncrementalSerializedEngine(testStand, listener) : JenaRSPEngineFactory
						.getSerializedEngine(testStand, listener);
				return;
			case STMT:
				engine = incremental ? JenaRSPEngineFactory.getIncrementalStmtEngine(testStand, listener) : JenaRSPEngineFactory.getStmtEngine(
						testStand, listener);
				return;
			case GRAPH:
				engine = incremental ? JenaRSPEngineFactory.getJenaEngineGraph(testStand, listener) : JenaRSPEngineFactory.getJenaEngineGraph(
						testStand, listener);
				return;
			default:
				message = "Not valid case [" + CEP_EVENT_TYPE + "]";
		}
		log.info(message);
		throw new IllegalArgumentException("Not valid case [" + CEP_EVENT_TYPE + "]");
	}

	protected static void reasonerSelection() {
		log.info("Reasoner Selection: [" + CURRENT_REASONER + "]");
		switch (CURRENT_REASONER) {
			case SMPL:
				listener = JenaReasoningListenerFactory.getSMPLListener(testStand);
				break;
			case RHODF:
				listener = JenaReasoningListenerFactory.getRhoDfListener(testStand);
				break;
			case FULL:
				listener = JenaReasoningListenerFactory.getFULLListener(testStand);
				break;
			case SMPL_INC:
				listener = JenaReasoningListenerFactory.getIncrementalSMPLListener(testStand);
				break;
			case RHODF_INC:
				listener = JenaReasoningListenerFactory.getIncrementalRhoDfListener(testStand);
				break;
			case FULL_INC:
				listener = JenaReasoningListenerFactory.getIncrementalFULLListener(testStand);
				break;
			default:
				log.error("Not valid case [" + CURRENT_REASONER + "]");
				throw new IllegalArgumentException("Not valid case [" + CURRENT_REASONER + "]");
		}
	}

	protected static void collectorSelection() {

		experimentResultCollector = new CollectorExperimentResult();
		streamingEventResultCollector = new CollectorEventResult(engineName + "/");
		String exp = "";
		if (ExecutionEnvirorment.finalresultTrigLogEnabled)
			exp += "Result C&S ";
		if (ExecutionEnvirorment.memoryLogEnabled)
			exp += "Memory ";
		if (ExecutionEnvirorment.latencyLogEnabled)
			exp += "Latency ";

		log.info("Execution of " + exp + "Experiment");
	}

	private static void run(String f, String comment, int experimentNumber, Date d, String experimentDescription) {

		testStand.build(streamingEventResultCollector, experimentResultCollector, engine, streamer);
		testStand.init();
		try {
			Experiment experiment = new Experiment(experimentNumber, experimentDescription, RSPENGINE, FileUtils.INPUT_FILE_PATH + f, outputFileName,
					windowFileName);
			experimentNumber += testStand.run(experiment, comment);
		} catch (Exception e) {
			log.error(e.getMessage());
			testStand.stop();
		}

		testStand.close();
	}

}
