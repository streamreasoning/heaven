package it.polimi.main;

import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.enums.EventBuilderMode;
import it.polimi.processing.enums.ExperimentType;
import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.factory.ConstantEventBuilder;
import it.polimi.processing.events.factory.StepEventBuilder;
import it.polimi.processing.events.factory.abstracts.EventBuilder;
import it.polimi.processing.events.interfaces.EventResult;
import it.polimi.processing.events.interfaces.ExperimentResult;
import it.polimi.processing.rspengine.abstracts.RSPEngine;
import it.polimi.processing.rspengine.windowed.esper.listeners.ResultCollectorListener;
import it.polimi.processing.rspengine.windowed.esper.plain.Plain2369;
import it.polimi.processing.rspengine.windowed.jena.JenaEngineGraph;
import it.polimi.processing.rspengine.windowed.jena.JenaEngineStmt;
import it.polimi.processing.rspengine.windowed.jena.JenaEngineTEvent;
import it.polimi.processing.rspengine.windowed.jena.enums.JenaEventType;
import it.polimi.processing.rspengine.windowed.jena.enums.Reasoner;
import it.polimi.processing.rspengine.windowed.jena.listener.JenaFullListener;
import it.polimi.processing.rspengine.windowed.jena.listener.JenaRhoDFListener;
import it.polimi.processing.rspengine.windowed.jena.listener.JenaSMPLListener;
import it.polimi.processing.services.FileService;
import it.polimi.processing.streamer.RSPEventStreamer;
import it.polimi.processing.system.ExecutionEnvirorment;
import it.polimi.processing.system.GetPropertyValues;
import it.polimi.processing.workbench.collector.CollectorEventResult;
import it.polimi.processing.workbench.collector.CollectorExperimentResult;
import it.polimi.processing.workbench.core.RSPTestStand;
import it.polimi.processing.workbench.streamer.NTStreamer;
import it.polimi.processing.workbench.timecontrol.AggregationStrategy;
import it.polimi.processing.workbench.timecontrol.InternalTiming;
import it.polimi.processing.workbench.timecontrol.NoAggregationStrategy;
import it.polimi.processing.workbench.timecontrol.TimeStrategy;
import it.polimi.utils.FileUtils;
import it.polimi.utils.RDFSUtils;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

import com.espertech.esper.client.UpdateListener;
import com.hp.hpl.jena.rdf.model.Model;

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
	private static RSPEventStreamer streamer;
	private static Reasoner CURRENT_REASONER;
	private static EventBuilderMode STREAMING_MODE;
	private static String engineName;
	private static String eventBuilderCodeName;
	private static int X;
	private static int Y;
	private static int INIT_SIZE;
	private static int EVENTS;
	private static String RSPENGINE;
	private static Integer X_SIZE;
	private static Integer Y_SIZE;

	public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {

		file = GetPropertyValues.getProperty(GetPropertyValues.INPUT_FILE);// _CLND_UNIV10INDEX0SEED0.nt

		EXPERIMENT_NUMBER = GetPropertyValues.getIntegerProperty("experiment_number");
		EXPERIMENT_DATE = GetPropertyValues.getDateProperty("experiment_date");

		EXECUTION_NUMBER = GetPropertyValues.getIntegerProperty("execution_number");
		COMMENT = GetPropertyValues.contains("comment") ? GetPropertyValues.getProperty("comment") : "";

		RSPENGINE = GetPropertyValues.getProperty("current_engine");
		if (RSPENGINE.equals("JENA")) {
			CURRENT_REASONER = GetPropertyValues.getEnumProperty(Reasoner.class, "jena_current_reasoner");
			engineName = CURRENT_REASONER.name().toLowerCase();
			CEP_EVENT_TYPE = GetPropertyValues.getEnumProperty(JenaEventType.class, "cep_event_type");
		} else {
			engineName = Plain2369.class.getSimpleName().toLowerCase();
			genearlEngineSelection();
		}

		EVENTS = GetPropertyValues.getIntegerProperty("rspevent_number");
		STREAMING_MODE = GetPropertyValues.getEnumProperty(EventBuilderMode.class, "streaming_mode");
		INIT_SIZE = GetPropertyValues.getIntegerProperty("init_size");
		X_SIZE = GetPropertyValues.getIntegerProperty("x_size");
		Y_SIZE = GetPropertyValues.getIntegerProperty("y_size");

		log.info("Experiment [" + EXPERIMENT_NUMBER + "] on [" + file + "] of [" + EXPERIMENT_DATE + "] Number of Events [" + EVENTS + "]");

		TimeStrategy strategy = (GetPropertyValues.getBooleanProperty("external_time_control_on")) ? timeStrategySelection() : new InternalTiming();
		testStand = new RSPTestStand(strategy);

		eventBuilderCodeName = streamerSelection();

		experimentDescription = "EXPERIMENT_ON_" + file + "_WITH_ENGINE_" + engineName + "EVENT_" + CEP_EVENT_TYPE;

		FileService.createOutputFolder(FileUtils.daypath + "/exp" + EXPERIMENT_NUMBER + "/" + engineName);

		String generalName = "EN" + EXPERIMENT_NUMBER + "_" + "EXE" + EXECUTION_NUMBER + "_" + COMMENT + "_" + DT.format(EXPERIMENT_DATE) + "_"
				+ file.split("\\.")[0] + "_R" + CURRENT_REASONER + "E" + CEP_EVENT_TYPE + eventBuilderCodeName;

		EXPERIMENT_TYPE = GetPropertyValues.getEnumProperty(ExperimentType.class, "experiment_type");
		outputFileName = EXPERIMENT_TYPE.ordinal() + "Result_" + generalName;
		windowFileName = EXPERIMENT_TYPE.ordinal() + "Window_" + generalName;

		whereOutput = "exp" + EXPERIMENT_NUMBER + "/" + engineName + "/" + outputFileName;
		whereWindow = "exp" + EXPERIMENT_NUMBER + "/" + engineName + "/" + windowFileName;

		log.info("Output file name will be: [" + whereOutput + "]");
		log.info("Window file name will be: [" + whereWindow + "]");

		reasonerSelection();

		collectorSelection();

		jenaEngineSelection();

		run(file, COMMENT, EXPERIMENT_NUMBER, EXPERIMENT_DATE, experimentDescription);

	}

	private static TimeStrategy timeStrategySelection() {
		return (GetPropertyValues.getProperty("time_strategy").equals("AGGREGATION")) ? new AggregationStrategy() : new NoAggregationStrategy();
	}

	protected static String streamerSelection() {
		EventBuilder<RSPEvent> eb = null;

		String code = "_EB";
		switch (STREAMING_MODE) {
			case CONSTANT:
				log.info("Event Builder Selection: Constant [" + INIT_SIZE + "] ");
				code += "K" + INIT_SIZE;
				log.info("CONSTANT Event Builder Initial Size [" + INIT_SIZE + "]");
				eb = new ConstantEventBuilder(INIT_SIZE, EXPERIMENT_NUMBER);
				break;
			case STEP:
				log.info("Event Builder Selection: Step [" + INIT_SIZE + "] Heigh [" + X + "] Width [" + Y + "] ");
				eb = new StepEventBuilder(X, Y, INIT_SIZE, EXPERIMENT_NUMBER);
				code += "S" + INIT_SIZE + "H" + X + "W" + Y;
				break;
			default:
				log.error("Not valid case [" + STREAMING_MODE + "]");
		}

		if (eb != null) {
			streamer = new NTStreamer(testStand, eb, EVENTS);
			return code;
		}
		throw new IllegalArgumentException("Not valid case [" + STREAMING_MODE + "]");
	}

	protected static void jenaEngineSelection() {
		switch (CEP_EVENT_TYPE) {
			case TEVENT:
				log.info("Engine Selection: JenaPlain [" + engineName + "] ");
				engine = new JenaEngineTEvent(engineName, testStand, listener);
				return;
			case STMT:
				log.info("Engine Selection: Jena Stmt [" + engineName + "] ");
				engine = new JenaEngineStmt(engineName, testStand, listener);
				return;
			case GRAPH:
				log.info("Engine Selection: JenaGraph [" + engineName + "] ");
				engine = new JenaEngineGraph(engineName, testStand, listener);
				return;
			default:
				log.error("Not valid case [" + CEP_EVENT_TYPE + "]");
		}
		throw new IllegalArgumentException("Not valid case [" + CEP_EVENT_TYPE + "]");
	}

	protected static void genearlEngineSelection() {
		listener = new ResultCollectorListener(testStand, engineName, 0);
		engine = new Plain2369(engineName, testStand, FileUtils.UNIV_BENCH_RHODF_MODIFIED, GetPropertyValues.getProperty("ontology_class"), listener);
		// case PLAIN2369CnS:
		// listener = new CompleteSoundListener(FileUtils.UNIV_BENCH_RHODF_MODIFIED,
		// FileUtils.RHODF_RULE_SET_RUNTIME, testStand);
		// engine = new Plain2369(engineName, testStand, FileUtils.UNIV_BENCH_RHODF_MODIFIED,
		// ontologyClass, listener);
		// break;
		// case PLAIN2369RHODF:
		// listener = new JenaRhoDFCSListener(FileUtils.UNIV_BENCH_RHODF_MODIFIED,
		// FileUtils.RHODF_RULE_SET_RUNTIME, testStand);
		// engine = new Plain2369(engineName, testStand, FileUtils.UNIV_BENCH_RHODF_MODIFIED,
		// ontologyClass, listener);
		// break;
		// case PLAIN2369SMPL:
		// listener = new JenaSMPLCSListener(FileUtils.UNIV_BENCH_RDFS_MODIFIED, testStand);
		// engine = new Plain2369(engineName, testStand, FileUtils.UNIV_BENCH_RHODF_MODIFIED,
		// ontologyClass, listener);
		// break;
	}

	protected static void reasonerSelection() {
		Model tbox;
		log.info("Reasoner Selection: [" + CURRENT_REASONER + "]");
		switch (CURRENT_REASONER) {
			case SMPL:
				tbox = RDFSUtils.loadModel(FileUtils.UNIV_BENCH_RDFS_MODIFIED);
				listener = new JenaSMPLListener(tbox, testStand);
				break;
			case RHODF:
				tbox = RDFSUtils.loadModel(FileUtils.UNIV_BENCH_RHODF_MODIFIED);
				listener = new JenaRhoDFListener(tbox, FileUtils.RHODF_RULE_SET_RUNTIME, testStand);
				break;
			case FULL:
				tbox = RDFSUtils.loadModel(FileUtils.UNIV_BENCH_RHODF_MODIFIED);
				listener = new JenaFullListener(tbox, testStand);
				break;
			default:
				log.error("Not valid case [" + CURRENT_REASONER + "]");
				throw new IllegalArgumentException("Not valid case [" + CURRENT_REASONER + "]");
		}
	}

	protected static void collectorSelection() {

		experimentResultCollector = new CollectorExperimentResult();
		streamingEventResultCollector = new CollectorEventResult(engineName + "/");

		if (ExecutionEnvirorment.finalresultTrigLogEnabled)
			log.info("Execution of Result C&S Experiment");
		if (ExecutionEnvirorment.memoryLogEnabled)
			log.info("Execution of Memory Experiment");
		if (ExecutionEnvirorment.latencyLogEnabled)
			log.info("Execution of Latency Experiment");
	}

	private static void run(String f, String comment, int experimentNumber, Date d, String experimentDescription) {

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
