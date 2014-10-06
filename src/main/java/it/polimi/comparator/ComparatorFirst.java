package it.polimi.comparator;

import it.polimi.output.filesystem.FileManager;
import it.polimi.teststand.events.Experiment;
import it.polimi.teststand.events.StreamingEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.jena.riot.RDFDataMgr;

import com.hp.hpl.jena.query.Dataset;

public class ComparatorFirst extends EngineComparator {

	private List<Dataset> datasetList;

	public ComparatorFirst(String[] comparing_files) {
		this.datasetList = new ArrayList<Dataset>();
		for (String f : comparing_files) {
			datasetList.add(RDFDataMgr.loadDataset(FileManager.OUTPUT_FILE_PATH
					+ f));
		}
	}

	@Override
	public boolean sendEvent(StreamingEvent e) {
		String key = getEventKey(e.getEventTriples());
		for (Dataset d : datasetList) {
			for (Dataset dd : datasetList) {
				if (!d.equals(dd)) {
					_logger.info(d.getNamedModel(key).equals(
							d.getNamedModel(key)));
				}
			}

		}
		return true;
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
