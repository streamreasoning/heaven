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
import it.polimi.processing.streamer.RSPEventStreamer;
import it.polimi.processing.validation.CompleteSoundListener;
import it.polimi.processing.validation.JenaRhoDFCSListener;
import it.polimi.processing.validation.JenaSMPLCSListener;
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
public class CommonMain {
	public static final int PLAIN2369 = 0, PLAIN2369CnS = 1, PLAIN2369SMPL = 2, PLAIN2369RHODF = 3;

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
	private static String whereOutput, whereWindow, outputFileName, windowFileName, experimentDescription;
	private static RSPEventStreamer streamer;
	private static BuildingStrategy MODE;

	private static int EXPERIMENTTYPE;

	private static String engineName;

	private static String eventBuilderCodeName;
	private static final int RESULT = 0;
	private static final int LATENCY = 1;
	private static final int MEMORY = 2;

	public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {

		file = "_CLND_UNIV10INDEX0SEED0.nt";

		boolean input = false;
		Scanner in = new Scanner(System.in);

		if (args.length < 5 || input) {
			log.info("Write Experiment Number: ");
			EXPERIMENTNUMBER = in.nextInt();
			log.info("Chose an Engine: 0:PLAIN2369 1:CS 2:CSSMPL 3:CSRHODF");
			CURRENTENGINE = in.nextInt();
			log.info("Chose Streaming Mode: 0:CONSTANT 1:LINEAR 2:STEP 3:EXP");
			MODE = BuildingStrategy.getById(in.nextInt());
			log.info("Add Some Comment?: n:NO or comment");
			String next = in.next().toUpperCase();
			comment = next != "n".toUpperCase() ? next : "";
		} else {
			EXPERIMENTNUMBER = Integer.parseInt(args[0]);
			CURRENTENGINE = Integer.parseInt(args[1]);
			MODE = BuildingStrategy.getById(Integer.parseInt(args[2]));
			comment = "n".toUpperCase().equals(args[3].toUpperCase()) ? "" : args[3];
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

				if (numberEvents != 0 && numberEvents % 5 == 0) { // Stream 50 events at time
					ts.setMemoryA(Memory.getMemoryUsage());
					process = ts.processDone();
					ts.setMemoryA(0D);
					ts.setMemoryB(0D);
					ts.setTimestamp(0L);
					ts.setResultTimestamp(0L);
				} else {
					process = ts.getRspEngine().process(e);
				}
				numberEvents++;
				return process;
			}
		});

		eventBuilderCodeName = input ? streamerSelection(in) : streamerSelection(args);

		engineName = engineNames[CURRENTENGINE];

		experimentDescription = "EXPERIMENT_ON_" + file + "_WITH_ENGINE_" + engineName + "EVENT_" + CURRENTENGINE;

		FileUtils.createOutputFolder("exp" + EXPERIMENTNUMBER + "/" + engineName);

		String generalName = "EN" + EXPERIMENTNUMBER + "_" + comment + "_" + dt.format(exeperimentDate) + "_" + file.split("\\.")[0] + "_"
				+ eventBuilderCodeName;

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

		engineSelection();

		in.close();

		run(file, comment, EXPERIMENTNUMBER, exeperimentDate, experimentDescription);

	}

	protected static void engineSelection() {
		switch (CURRENTENGINE) {
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
