package it.polimi.processing.validation;

import it.polimi.export.processing.rspengine.windowed.esper.plain.events.Out;
import it.polimi.processing.ets.core.EventProcessor;
import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.events.interfaces.Event;
import it.polimi.processing.system.Memory;
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
	protected final EventProcessor<Event> collector;
	protected int eventNumber = 0;
	protected Set<TripleContainer> statements;
	protected Set<TripleContainer> ABoxTriples;
	protected boolean completeness;
	protected boolean soundness;

	protected long ouputcurrentTimeMillis;
	protected double outputmemoryUsage;

	public CSListener(String tbox, EventProcessor<Event> collector) {
		FileManager.get().addLocatorClassLoader(this.getClass().getClassLoader());
		this.TBoxStar = FileManager.get().loadModel(tbox, null, "RDF/XML");
		this.collector = collector;
	}

	@Override
	public void update(EventBean[] newData, EventBean[] oldData) {

		jenaABox = ModelFactory.createMemModelMaker().createDefaultModel();
		esperResult = ModelFactory.createMemModelMaker().createDefaultModel();
		ABoxTriples = new HashSet<TripleContainer>();
		statements = new HashSet<TripleContainer>();
		ouputcurrentTimeMillis = System.currentTimeMillis();
		outputmemoryUsage = Memory.getMemoryUsage();
		for (EventBean e : newData) {

			Out underlying = (Out) e.getUnderlying();
			for (TripleContainer triple : underlying.getTriples()) {
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

		Set<TripleContainer> statements = new HashSet<TripleContainer>();
		StmtIterator iterator = jenaResult.difference(TBoxStar).listStatements();

		Triple t;
		TripleContainer statementStrings;
		while (iterator.hasNext()) {
			t = iterator.next().asTriple();
			statementStrings = new TripleContainer(t.getSubject().toString(), t.getPredicate().toString(), t.getObject().toString());
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

	private Statement createStatement(TripleContainer tc) {
		String[] t = tc.getTriple();
		Resource subject = ResourceFactory.createResource(t[0]);
		Property predicate = (t[1] != RDFSUtils.TYPE_PROPERTY) ? ResourceFactory.createProperty(t[1]) : RDF.type;
		RDFNode object = ResourceFactory.createResource(t[2]);
		return ResourceFactory.createStatement(subject, predicate, object);
	}

	protected abstract Reasoner getReasoner();

}
