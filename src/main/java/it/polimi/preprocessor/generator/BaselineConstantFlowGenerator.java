package it.polimi.preprocessor.generator;

import it.polimi.processing.enums.ExperimentType;
import it.polimi.processing.enums.FlowRateProfile;
import it.polimi.processing.enums.Reasoning;
import it.polimi.processing.rspengine.jena.enums.JenaEventType;
import it.polimi.processing.rspengine.jena.enums.OntoLanguage;
import it.polimi.services.FileService;

import java.io.IOException;

public class BaselineConstantFlowGenerator {

	private static int experimentNumber = 1;
	private static final String eol = System.getProperty("line.separator");
	private static final String date = "2015-01-19";

	private static boolean memory = false, latency = true, abox = false, result = false;
	private static int maxEventStream = 2500;
	private static final String inputFile = "BIG_SHUFFLED.nt";

	private static final OntoLanguage[] langs = new OntoLanguage[] { OntoLanguage.SMPL, OntoLanguage.RHODF };
	private static final ExperimentType[] experimentTypes = new ExperimentType[] { ExperimentType.LATENCY, ExperimentType.MEMORY };
	private static final FlowRateProfile profile = FlowRateProfile.CONSTANT;
	private static final Reasoning[] reasoning = new Reasoning[] { Reasoning.NAIVE, Reasoning.INCREMENTAL };
	private static final JenaEventType[] jenaEventTypes = new JenaEventType[] { JenaEventType.STMT, JenaEventType.GRAPH };

	public static void main(String[] args) throws IOException {

		String outputFolder = "./properties/ESWC15/" + profile + "FR" + "/";

		String content = "";
		String name = "";
		for (OntoLanguage lang : langs) {

			for (int rsp_events_in_window = 1; rsp_events_in_window <= 1000; rsp_events_in_window *= 10) {
				for (int init_size = 1; init_size <= 1000; init_size *= 10) {
					for (JenaEventType eventType : jenaEventTypes) {
						for (Reasoning reasoning_mode : reasoning) {
							for (ExperimentType type : experimentTypes) {
								if (ExperimentType.MEMORY.equals(type)) {
									memory = true;
								}
								name += profile + "_" + reasoning_mode + "_" + type + "_" + lang.name() + "_" + eventType + "INIT" + init_size + "W"
										+ rsp_events_in_window;

								for (int executionNumber = 0; executionNumber < 5; executionNumber++) {
									content = experimentProperties(content, executionNumber, date, type.name());
									content = engineProperties(content, "JENA", eventType, reasoning_mode, lang);
									content = eventsProperties(content, init_size, profile, rsp_events_in_window, maxEventStream);
									content = timeProperties(content, true);

									writeOnFile(outputFolder + reasoning_mode + "/", content, name, lang, eventType, rsp_events_in_window, init_size,
											type, executionNumber);
								}
								System.out.println("Generate experiment [" + experimentNumber + "] name [" + name + "]");

								name = "";
								content = "";

							}
						}

					}
					experimentNumber++;
				}
			}
		}

		System.out.println("Generated [" + (experimentNumber - 1) + "] Experiments");
	}

	private static void writeOnFile(String outputFolder, String content, String name, OntoLanguage lang, JenaEventType eventType,
			int rsp_events_in_window, int init_size, ExperimentType type, int executionNumber) {
		String currentOutputFolder = outputFolder + "/" + type + "/" + lang + "/" + "/" + eventType + "/DIAG" + rsp_events_in_window * init_size
				+ "/";
		FileService.createFolders(currentOutputFolder);
		if (rsp_events_in_window * init_size <= 1000) {
			FileService.createFolders(outputFolder + "/Experiments/");

			FileService.write(currentOutputFolder + "" + name + "EN" + executionNumber + ".properties", content);
			FileService.write(outputFolder + "/Experiments/" + name + "EN" + executionNumber + ".properties", content);
		}
	}

	private static String timeProperties(String content, boolean externalTiming) {
		content += "#Timing";
		content += eol;
		content += "external_time_control_on = " + maxEventStream;
		content += eol;
		content += "#Timing End";
		content += eol;
		return content;
	}

	private static String eventsProperties(String content, int init_size, FlowRateProfile profile, int rsp_events_in_window, int maxEventStream) {
		content += "#Events";
		content += eol;
		content += "max_event_stream = " + maxEventStream;
		content += eol;
		content += "rsp_events_in_window = " + rsp_events_in_window;
		content += eol;
		content += "flow_rate_profile = " + profile;
		content += eol;
		content += "init_size = " + init_size;
		content += eol;
		content += "x_size = " + 0;
		content += eol;
		content += "y_size = " + 0;
		content += eol;
		content += "#Events End";
		content += eol;
		return content;
	}

	private static String engineProperties(String content, String engine, JenaEventType eventType, Reasoning reasoning, OntoLanguage lang) {
		content += "#Engine";
		content += eol;
		content += "current_engine = " + engine;
		content += eol;
		content += "cep_event_type = " + eventType;
		content += eol;
		content += "onto_lang = " + lang;
		content += eol;
		content += "reasoning_mode = " + reasoning;
		content += eol;
		content += "#Engine End";
		content += eol;
		return content;
	}

	private static String experimentProperties(String content, int executionNumber, String date, String type) {
		content += "#ESWC15 Baselines Constant Flow Rate Experiments";
		content += eol;
		content += "#Experiment";
		content += eol;
		content += "experiment_name = ESWC15 BASELINES CONSTANT FLOW RATE";
		content += eol;
		content += "experiment_number = " + experimentNumber;
		content += eol;
		content += "execution_number = " + executionNumber;
		content += eol;
		content += "experiment_date = " + date;
		content += eol;
		content += "experiment_type = " + type;
		content += eol;
		content += "result_log_enabled = " + result;
		content += eol;
		content += "latency_log_enabled = " + latency;
		content += eol;
		content += "memory_log_enabled = " + memory;
		content += eol;
		content += "save_abox_log = " + abox;
		content += eol;
		content += "input_file = " + inputFile;
		content += eol;
		content += "#Experiment End";
		content += eol;
		return content;
	}
}
