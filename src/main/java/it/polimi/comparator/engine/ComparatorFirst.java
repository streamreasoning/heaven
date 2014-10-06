package it.polimi.comparator.engine;

import it.polimi.comparator.events.ComparisonResultEvent;
import it.polimi.output.filesystem.FileManager;
import it.polimi.teststand.events.Experiment;
import it.polimi.teststand.events.StreamingEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.jena.riot.RDFDataMgr;

import com.hp.hpl.jena.query.Dataset;

public class ComparatorFirst extends EngineComparator {

	private List<Dataset> datasetList;

	public ComparatorFirst(String[] comparing_files) {
		super(null, "Comparator First");
		this.datasetList = new ArrayList<Dataset>();
		for (String f : comparing_files) {
			datasetList.add(RDFDataMgr.loadDataset(FileManager.OUTPUT_FILE_PATH
					+ f));
		}
	}

	@Override
	public boolean sendEvent(StreamingEvent e) {
		String key = getEventKey(e.getEventTriples());
		try {
			resultCollector.storeEventResult(new ComparisonResultEvent(
					experiment.getName(), e.getTripleToString(), e
							.getEvent_timestamp(), experiment.getTimestamp(),
					checkCompletenes(key), checkCompletenes(key), 0, 0));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return true;
	}

	public boolean checkCompletenes(String key) {
		if (datasetList.size() > 2) {
			_logger.info("Too much argumetns");
			return false;
		}
		Dataset ref = datasetList.get(0);
		boolean equals = ref.getNamedModel(key).isIsomorphicWith(
				datasetList.get(1).getNamedModel(key));
		System.out.println(equals);
		return equals;
	}

	private String getEventKey(Set<String[]> triples) {
		String key = "[";
		for (String[] triple : triples) {
			key += Arrays.deepToString(triple);

		}
		key += "]";

		return key;
	}

	@Override
	public boolean startProcessing(Experiment e) {
		this.experiment = e;
		initLogger();
		return true;
	}

	@Override
	public Experiment stopProcessing() {
		return null;
	}

	@Override
	public void turnOn() {
	}

	@Override
	public void turnOff() {
	}

	public List<Dataset> getDatasetList() {
		return datasetList;
	}

	public void setDatasetList(List<Dataset> datasetList) {
		this.datasetList = datasetList;
	}

}
