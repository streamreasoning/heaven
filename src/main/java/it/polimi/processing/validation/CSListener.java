package it.polimi.processing.validation;

import it.polimi.processing.collector.ResultCollector;
import it.polimi.processing.events.interfaces.EventResult;
import it.polimi.processing.rspengine.esper.plain.events.Out;
import it.polimi.utils.RDFSUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import lombok.extern.log4j.Log4j;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.rdf.model.impl.InfModelImpl;
import com.hp.hpl.jena.reasoner.InfGraph;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDF;

@Log4j
public abstract class CSListener implements UpdateListener {

	protected final Model TBoxStar;
	protected Model esperResult, jenaABox;
	protected InfModel jenaResult;
	protected Reasoner reasoner;
	protected final ResultCollector<EventResult> collector;
	protected int eventNumber = 0;
	protected Set<String[]> statements;
	protected Set<String[]> ABoxTriples;
	protected boolean completeness;
	protected boolean soundness;

	public CSListener(String tbox, ResultCollector<EventResult> collector) {
		FileManager.get().addLocatorClassLoader(this.getClass().getClassLoader());
		this.TBoxStar = FileManager.get().loadModel(tbox, null, "RDF/XML");
		this.collector = collector;
	}

	@Override
	public void update(EventBean[] newData, EventBean[] oldData) {

		jenaABox = ModelFactory.createMemModelMaker().createDefaultModel();
		esperResult = ModelFactory.createMemModelMaker().createDefaultModel();
		ABoxTriples = new HashSet<String[]>();
		statements = new HashSet<String[]>();

		for (EventBean e : newData) {

			Out underlying = (Out) e.getUnderlying();
			for (String[] triple : underlying.getTriples()) {
				statements.add(triple);
				Statement createdStatement = createStatement(triple);
				esperResult.add(createdStatement);
				if (e.get("channel").equals("Input")) {
					System.out.println("FOUND ABOX");
					ABoxTriples.add(triple);
					jenaABox.add(createdStatement);
				}
			}

		}

		evaluation();

		Set<String[]> statements = new HashSet<String[]>();
		StmtIterator iterator = jenaResult.difference(TBoxStar).listStatements();

		Triple t;
		String[] statementStrings;
		while (iterator.hasNext()) {
			t = iterator.next().asTriple();
			statementStrings = new String[] { t.getSubject().toString(), t.getPredicate().toString(), t.getObject().toString() };
			statements.add(statementStrings);
		}

		try {

			sendResult();
			eventNumber += ABoxTriples.size();

		} catch (IOException e1) {
			log.error(e1.getMessage());
		}
	}

	protected void evaluation() {
		log.info("EVALUATION");
		reasoner = getReasoner();
		InfGraph graph = reasoner.bindSchema(TBoxStar.getGraph()).bind(jenaABox.getGraph());
		jenaResult = new InfModelImpl(graph);
		completeness = ModelAnalyser.isComplete(jenaResult, esperResult);
		soundness = ModelAnalyser.isSound(jenaResult, esperResult);

		System.out.println("COMPLETE");

		StmtIterator iterator = ModelAnalyser.getCompleteDiff(jenaABox, esperResult).listStatements();
		while (iterator.hasNext()) {
			Triple t = iterator.next().asTriple();
			System.out.println(t.toString());
		}

		System.out.println("SOUND");
		iterator = ModelAnalyser.getSoundDiff(jenaABox, esperResult).listStatements();
		while (iterator.hasNext()) {
			Triple t = iterator.next().asTriple();
			System.out.println(t.toString());
		}

	}

	protected abstract void sendResult() throws IOException;

	private Statement createStatement(String[] t) {

		Resource subject = ResourceFactory.createResource(t[0]);
		Property predicate = (t[1] != RDFSUtils.TYPE_PROPERTY) ? ResourceFactory.createProperty(t[1]) : RDF.type;
		RDFNode object = ResourceFactory.createResource(t[2]);
		return ResourceFactory.createStatement(subject, predicate, object);
	}

	protected abstract Reasoner getReasoner();

}
