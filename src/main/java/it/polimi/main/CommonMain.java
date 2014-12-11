package it.polimi.main;

import it.polimi.processing.collector.StartableCollector;
import it.polimi.processing.collector.saver.CSVEventSaver;
import it.polimi.processing.collector.saver.SQLLiteEventSaver;
import it.polimi.processing.collector.saver.TrigEventSaver;
import it.polimi.processing.collector.saver.VoidSaver;
import it.polimi.processing.enums.EventBuilderMode;
import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.factory.ConstantEventBuilder;
import it.polimi.processing.events.factory.StepEventBuilder;
import it.polimi.processing.events.factory.abstracts.EventBuilder;
import it.polimi.processing.events.interfaces.EventResult;
import it.polimi.processing.events.interfaces.ExperimentResult;
import it.polimi.processing.rspengine.windowed.RSPEngine;
import it.polimi.processing.rspengine.windowed.esper.commons.listener.ResultCollectorListener;
import it.polimi.processing.rspengine.windowed.esper.plain.Plain2369;
import it.polimi.processing.streamer.RSPEventStreamer;
import it.polimi.processing.validation.CompleteSoundListener;
import it.polimi.processing.validation.JenaRhoDFCSListener;
import it.polimi.processing.validation.JenaSMPLCSListener;
import it.polimi.processing.workbench.collector.CollectorEventResult;
import it.polimi.processing.workbench.collector.CollectorExperimentResult;
import it.polimi.processing.workbench.core.RSPTestStand;
import it.polimi.processing.workbench.streamer.NTStreamer;
import it.polimi.processing.workbench.timecontrol.AggregationStrategy;
import it.polimi.processing.workbench.timecontrol.OneToOneStrategy;
import it.polimi.processing.workbench.timecontrol.TimeStrategy;
import it.polimi.utils.CommonInputOrder;
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
	private static final int PLAIN2369 = 0, PLAIN2369CnS = 1, PLAIN2369SMPL = 2, PLAIN2369RHODF = 3;

	private static int CURRENT_ENGINE;
	private static int EXPERIMENT_NUMBER;

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
	private static String whereOutput, whereWindow, outputFileName, windowFileName, experimentDescription;
	private static RSPEventStreamer streamer;
	private static EventBuilderMode EVENT_BUILDER;

	private static int EXPERIMENTTYPE;

	private static String engineName;

	private static String eventBuilderCodeName;
	private static final int RESULT = 0;
	private static final int LATENCY = 1;
	private static final int MEMORY = 2;

	private static int EXECUTION;
	private static int X;
	private static int Y;
	private static int INITSIZE;
	private static int EVENTS = 1000;

	public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {

		file = args[CommonInputOrder.FILE];// _CLND_UNIV10INDEX0SEED0.nt
		EVENTS = Integer.parseInt(args[CommonInputOrder.EVENTS]);
		EXPERIMENTTYPE = Integer.parseInt(args[CommonInputOrder.EXPERIMENT_TYPE]);
		EXPERIMENT_NUMBER = Integer.parseInt(args[CommonInputOrder.EXPERIMENTNUMBER]);
		EXECUTION = Integer.parseInt(args[CommonInputOrder.EXECUTIONNUMBER]);
		CURRENT_ENGINE = Integer.parseInt(args[CommonInputOrder.CURRENT_ENGINE]);
		comment = "n".equalsIgnoreCase(args[CommonInputOrder.COMMENTS].toUpperCase()) ? "" : args[CommonInputOrder.COMMENTS];
		EVENT_BUILDER = EventBuilderMode.getById(Integer.parseInt(args[CommonInputOrder.EVENTBUILDER]));
		INITSIZE = Integer.parseInt(args[CommonInputOrder.INITSIZE]);
		X = Integer.parseInt(args[CommonInputOrder.X]);
		Y = Integer.parseInt(args[CommonInputOrder.Y]);

		exeperimentDate = FileUtils.d;

		log.info("Experiment [" + EXPERIMENT_NUMBER + "] of [" + exeperimentDate + "]");

		TimeStrategy strategy = (INITSIZE == 1) ? new OneToOneStrategy() : new AggregationStrategy(5);
		testStand = new RSPTestStand(strategy);

		log.info("Experiment [" + EXPERIMENT_NUMBER + "] of [" + exeperimentDate + "]");

		eventBuilderCodeName = streamerSelection();

		engineName = engineNames[CURRENT_ENGINE];

		experimentDescription = "EXPERIMENT_ON_" + file + "_WITH_ENGINE_" + engineName + "EVENT_" + CURRENT_ENGINE;

		FileUtils.createOutputFolder("exp" + EXPERIMENT_NUMBER + "/" + engineName);

		String generalName = "EN" + EXPERIMENT_NUMBER + "_" + "EXE" + EXECUTION + "_" + comment + "_" + dt.format(exeperimentDate) + "_"
				+ file.split("\\.")[0] + "_" + eventBuilderCodeName;

		whereOutput = "exp" + EXPERIMENT_NUMBER + "/" + engineName + "/" + outputFileName;
		whereWindow = "exp" + EXPERIMENT_NUMBER + "/" + engineName + "/" + windowFileName;

		log.info("Output file name will be: [" + whereOutput + "]");
		log.info("Window file name will be: [" + whereWindow + "]");

		outputFileName = EXPERIMENTTYPE + "Result_" + generalName;
		windowFileName = EXPERIMENTTYPE + "Window_" + generalName;

		whereOutput = "exp" + EXPERIMENT_NUMBER + "/" + engineName + "/" + outputFileName;
		whereWindow = "exp" + EXPERIMENT_NUMBER + "/" + engineName + "/" + windowFileName;

		log.info("Output file name will be: [" + whereOutput + "]");
		log.info("Window file name will be: [" + whereWindow + "]");

		collectorSelection();

		engineSelection();

		run(file, comment, EXPERIMENT_NUMBER, exeperimentDate, experimentDescription);

	}

	protected static void engineSelection() {
		switch (CURRENT_ENGINE) {
			case PLAIN2369:
				listener = new ResultCollectorListener(testStand, engineName, 0);
				engine = new Plain2369(engineName, testStand, FileUtils.UNIV_BENCH_RHODF_MODIFIED, ontologyClass, listener);
				break;
			case PLAIN2369CnS:
				listener = new CompleteSoundListener(FileUtils.UNIV_BENCH_RHODF_MODIFIED, FileUtils.RHODF_RULE_SET_RUNTIME, testStand);
				engine = new Plain2369(engineName, testStand, FileUtils.UNIV_BENCH_RHODF_MODIFIED, ontologyClass, listener);
				break;
			case PLAIN2369RHODF:
				listener = new JenaRhoDFCSListener(FileUtils.UNIV_BENCH_RHODF_MODIFIED, FileUtils.RHODF_RULE_SET_RUNTIME, testStand);
				engine = new Plain2369(engineName, testStand, FileUtils.UNIV_BENCH_RHODF_MODIFIED, ontologyClass, listener);
				break;
			case PLAIN2369SMPL:
				listener = new JenaSMPLCSListener(FileUtils.UNIV_BENCH_RDFS_MODIFIED, testStand);
				engine = new Plain2369(engineName, testStand, FileUtils.UNIV_BENCH_RHODF_MODIFIED, ontologyClass, listener);
				break;
			default:
				engine = null;
		}
	}

	protected static String streamerSelection() {
		EventBuilder<RSPEvent> eb;
		String code = "EB";
		switch (EVENT_BUILDER) {
			case CONSTANT:
				code += "C" + INITSIZE;
				log.info("CONSTANT Event Builder Initial Size [" + INITSIZE + "]");
				eb = new ConstantEventBuilder(INITSIZE, EXPERIMENT_NUMBER);
				break;
			case STEP:

				log.info("STEP Event Builder, Initial Size [" + INITSIZE + "] step height [" + X + "] and width[" + Y + "]");
				eb = new StepEventBuilder(X, Y, INITSIZE, EXPERIMENT_NUMBER);
				code += "S" + INITSIZE + "H" + X + "W" + Y;
				break;
			default:
				eb = new ConstantEventBuilder(INITSIZE, EXPERIMENT_NUMBER);
				break;

		}
		streamer = new NTStreamer(testStand, eb);
		return code;
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
				break;
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
