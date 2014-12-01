package it.polimi.processing.validation;

import lombok.Getter;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;

/**
 * @author Riccardo
 * 
 */
@Getter
public class DatasetAnalyser {

	private final Dataset reference;

	public DatasetAnalyser(Dataset loadDataset) {
		this.reference = loadDataset;
	}

	public boolean isCorrect(String key, Model compare) {
		return reference.getNamedModel(key).isIsomorphicWith(compare);
	}

	// jena - modello = 0 se completo, != otherwise
	public Model getCompleteDiff(String key, Model compare) {
		return reference.getNamedModel(key).difference(compare);
	}

	public boolean isComplete(String key, Model compare) {
		return getCompleteDiff(key, compare).isEmpty();
	}

	// modello - jena = 0 se sound, != otherwise
	public Model getSoundDiff(String key, Model compare) {
		return compare.difference(reference.getNamedModel(key));
	}

	public boolean isSound(String key, Model compare) {
		return getSoundDiff(key, compare).isEmpty();
	}
}
