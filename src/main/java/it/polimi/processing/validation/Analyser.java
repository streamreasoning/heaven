package it.polimi.processing.validation;

import it.polimi.utils.FileUtils;
import lombok.Getter;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;

/**
 * @author Riccardo
 * 
 */
@Getter
public class Analyser {

	public static boolean isCorrect(Model reference, Model compare) {
		return reference.isIsomorphicWith(compare);
	}

	// jena - modello = 0 se completo, != otherwise
	public static Model getCompleteDiff(Model reference, Model compare) {
		return reference.difference(compare);
	}

	public static boolean isComplete(Model reference, Model compare) {
		return getCompleteDiff(reference, compare).isEmpty();
	}

	// modello - jena = 0 se sound, != otherwise
	public static Model getSoundDiff(Model reference, Model compare) {
		return compare.difference(reference);
	}

	public static boolean isSound(Model reference, Model compare) {
		return getSoundDiff(reference, compare).isEmpty();
	}

	protected Reasoner getRDFSReasoner() {
		Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();
		reasoner.setParameter(ReasonerVocabulary.PROPsetRDFSLevel, ReasonerVocabulary.RDFS_SIMPLE);
		return reasoner;
	}

	protected Reasoner getRHODFReasoner() {
		return new GenericRuleReasoner(Rule.rulesFromURL(FileUtils.RHODF_RULE_SET_RUNTIME));
	}
}
