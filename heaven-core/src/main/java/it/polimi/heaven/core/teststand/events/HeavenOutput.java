package it.polimi.heaven.core.teststand.events;

import it.polimi.heaven.core.teststand.collector.Collectable;
import it.polimi.services.FileService;
import lombok.Data;
import lombok.extern.log4j.Log4j;

@Data
@Log4j
public class HeavenOutput implements HeavenEvent, Collectable {

	private String id;
	private int stimulus_number, response_number;
	private double memory_before_processing;
	private double memory_after_processing;
	private long heaven_input_creation_timestamp;
	private long heaven_output_creation_timestamp;
	private long stimuli_encoding_latency;
	private long response_decoding_latency;
	private long query_latency;
	private String query;

	@Override
	public boolean save(String where) {
		log.debug("Save Data [" + where + "]");

		return saveCSV(where);
	}

	public boolean saveCSV(String where) {

		long heaven_processing_latency = heaven_output_creation_timestamp - heaven_input_creation_timestamp;
		String s = id + "," + stimulus_number + "," + response_number + "," + memory_before_processing + "," + memory_after_processing + ","
				+ query_latency + "," + heaven_processing_latency + "," + stimuli_encoding_latency + System.getProperty("line.separator");
		return FileService.write(where + ".csv", s);
	}
}
