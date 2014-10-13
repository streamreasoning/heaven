package it.polimi.comparator.engine;

import it.polimi.collector.ResultCollector;
import it.polimi.comparator.events.ComparisonExperimentResult;
import it.polimi.comparator.events.ComparisonResultEvent;
import it.polimi.enums.ExecutionStates;
import it.polimi.events.Experiment;
import it.polimi.events.StreamingEvent;
import it.polimi.output.filesystem.FileManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.jena.riot.RDFDataMgr;
import org.apache.log4j.Logger;

import com.hp.hpl.jena.query.Dataset;

public class ComparatorImpl extends EngineComparator {

	private List<Dataset> datasetList;

	public ComparatorImpl(
			String[] comparing_files,
			ResultCollector<ComparisonResultEvent, ComparisonExperimentResult> resultCollector) {
		super(resultCollector, "Comparator First");
		this.datasetList = new ArrayList<Dataset>();
		for (String f : comparing_files) {
			datasetList.add(RDFDataMgr.loadDataset(FileManager.OUTPUT_FILE_PATH
					+ f));
		}
	}

	@Override
	public boolean sendEvent(StreamingEvent e) {
		String key = "http://example.org/"
				+ getEventKey(e.getEventTriples()).hashCode() + "";
		Logger.getRootLogger().debug(key);
		try {
			// TODO mi vado a recuperare dal log memoria e latenza
			resultCollector.store(new ComparisonResultEvent(
					experiment.getName(), e.getTripleToString(), e
							.getEvent_timestamp(), experiment.getTimestamp(),
					isComplete(key), isSound(key), 0, 0));

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return true;
	}

	public boolean isCorrect(String key) {
		if (datasetList.size() > 2) {
			Logger.getLogger("it.polimi.logger").info("Too much argumetns");
			return false;
		}
		Dataset ref = datasetList.get(0);
		boolean equals = ref.getNamedModel(key).isIsomorphicWith(
				datasetList.get(1).getNamedModel(key));
		return equals;
	}

	public boolean isComplete(String key) {
		// Model jenaGraph = datasetList.get(0).getNamedModel(key);
		// Model engineGraph = datasetList.get(1).getNamedModel(key);
		// Model diff = jenaGraph.difference(engineGraph);
		return datasetList.get(1).getNamedModel(key)
				.difference(datasetList.get(0).getNamedModel(key)).isEmpty();
	}

	public boolean isSound(String key) {
		// Model jenaGraph = datasetList.get(0).getNamedModel(key);
		// Model engineGraph = datasetList.get(1).getNamedModel(key);
		// Model diff = engineGraph.difference(jenaGraph);
		return datasetList.get(0).getNamedModel(key)
				.difference(datasetList.get(1).getNamedModel(key)).isEmpty();
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
	public ExecutionStates init() {
		return status = ExecutionStates.READY;
	}

	@Override
	public ExecutionStates close() {
		return status = ExecutionStates.CLOSED;
	}

	public List<Dataset> getDatasetList() {
		return datasetList;
	}

	public void setDatasetList(List<Dataset> datasetList) {
		this.datasetList = datasetList;
	}

	@Override
	public ExecutionStates stopProcessing(Experiment e) {
		if (e != null) {
			Logger.getLogger("oobqa-comparison").info(
					"Start Experiment :" + e.toString());
			return status = ExecutionStates.OFF;
		} else {
			return status = ExecutionStates.ERROR;

		}
	}

	@Override
	public ExecutionStates startProcessing(Experiment e) {
		if (e != null) {
			this.experiment = e;
			Logger.getLogger("oobqa-comparison").info(
					"Stop Experiment :" + e.toString());
			return status = ExecutionStates.READY;
		} else {
			return status = ExecutionStates.ERROR;
		}
	}

}
