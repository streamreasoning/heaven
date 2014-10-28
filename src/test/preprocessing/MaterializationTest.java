package preprocessing;

import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.impl.InfModelImpl;
import com.hp.hpl.jena.reasoner.InfGraph;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;

public class MaterializationTest {

	private Model m;
	private Model abox;

	@Test
	public void differentMaterialization() {

		m = FileManager.get().loadModel("src/main/resources/data/inference/univ-bench-rdfs.rdf", null, "RDF/XML");
		abox = FileManager.get().loadModel("src/main/resources/data/input/file1.nt", null, "NT");

		Reasoner reasoner = getRDFSReasonerSimpleRuleset();

		InfGraph graph = reasoner.bindSchema(m.getGraph()).bind(abox.getGraph());

		Model infmodel_rdfs_simple = new InfModelImpl(graph);

	}

	private Reasoner getRDFSReasonerSimpleRuleset() {
		Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();
		reasoner.setParameter(ReasonerVocabulary.PROPsetRDFSLevel, ReasonerVocabulary.RDFS_SIMPLE);
		return reasoner;
	}
}
