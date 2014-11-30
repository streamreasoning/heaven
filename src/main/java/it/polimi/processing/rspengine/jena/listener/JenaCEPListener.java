package it.polimi.processing.rspengine.jena.listener;

import it.polimi.processing.collector.ResultCollector;
import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.interfaces.EventResult;
import it.polimi.processing.rspengine.esper.plain.events.TEvent;
import it.polimi.processing.rspengine.jena.JenaEsperSMPL;
import it.polimi.utils.Memory;
import it.polimi.utils.RDFSUtils;

import java.io.IOException;
import java.util.Arrays;
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
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;

@Log4j
public class JenaCEPListener implements UpdateListener {

	private final Model tbox_star;
	private Model abox;
	private InfModel abox_star;
	private Reasoner reasoner;
	private final ResultCollector<EventResult> collector;
	private final JenaEsperSMPL engine;

	public JenaCEPListener(String ontology, JenaEsperSMPL engine, ResultCollector<EventResult> collector) {
		FileManager.get().addLocatorClassLoader(JenaCEPListener.class.getClassLoader());
		this.collector = collector;
		this.tbox_star = FileManager.get().loadModel(ontology, null, "RDF/XML");
		this.engine = engine;
	}

	@Override
	public void update(EventBean[] newData, EventBean[] oldData) {

		abox = ModelFactory.createMemModelMaker().createDefaultModel();

		for (EventBean e : newData) {
			abox.add(createStatement((TEvent) e.getUnderlying()));
		}

		reasoner = getRDFSSimpleReasoner();
		InfGraph graph = reasoner.bindSchema(tbox_star.getGraph()).bind(abox.getGraph());
		abox_star = new InfModelImpl(graph);

		Set<String[]> statements = new HashSet<String[]>();
		StmtIterator iterator = abox_star.difference(tbox_star).listStatements();

		Triple t;
		String[] statementStrings;
		while (iterator.hasNext()) {
			t = iterator.next().asTriple();
			statementStrings = new String[] { t.getSubject().toString(), t.getPredicate().toString(), t.getObject().toString() };
			statements.add(statementStrings);
			log.debug(Arrays.deepToString(statementStrings));
		}

		try {

			RSPEvent e = engine.getRSPEvent();
			e.setAll_triples(statements);
			e.setResultTimestamp(System.currentTimeMillis());
			e.setMemoryAR(Memory.getMemoryUsage());
			collector.store(e);

		} catch (IOException e1) {
			log.error(e1.getMessage());
		}
	}

	private Statement createStatement(TEvent e) {

		Resource subject = ResourceFactory.createResource(e.getS());
		Property predicate = (e.getP() != RDFSUtils.TYPE_PROPERTY) ? ResourceFactory.createProperty(e.getP()) : RDF.type;
		RDFNode object = ResourceFactory.createResource(e.getO());
		return ResourceFactory.createStatement(subject, predicate, object);
	}

	private Reasoner getRDFSSimpleReasoner() {
		Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();
		reasoner.setParameter(ReasonerVocabulary.PROPsetRDFSLevel, ReasonerVocabulary.RDFS_SIMPLE);
		return reasoner;
	}

}