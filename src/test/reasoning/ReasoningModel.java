package reasoning;

import static org.junit.Assert.assertEquals;
import it.polimi.utils.FileUtils;

import org.junit.Test;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.InfGraph;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;

public class ReasoningModel {

	@Test
	public void infModelTest() {

		InfModel model = ModelFactory.createInfModel(new GenericRuleReasoner(Rule.rulesFromURL(FileUtils.RHODF_RULE_SET_RUNTIME)), ModelFactory
				.createMemModelMaker().createDefaultModel());

		assertEquals(true, model.getGraph() instanceof InfGraph);

	}
}
