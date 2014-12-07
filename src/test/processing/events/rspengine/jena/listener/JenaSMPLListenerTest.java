package processing.events.rspengine.jena.listener;

import it.polimi.processing.rspengine.windowed.jena.events.GraphEvent;
import it.polimi.processing.rspengine.windowed.jena.events.StatementEvent;
import it.polimi.processing.rspengine.windowed.jena.events.TripleEvent;
import it.polimi.processing.rspengine.windowed.jena.listener.JenaSMPLListener;
import it.polimi.utils.FileUtils;
import it.polimi.utils.RDFSUtils;

import org.junit.Test;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventType;
import com.espertech.esper.client.PropertyAccessException;
import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDF;

public class JenaSMPLListenerTest {

	private JenaSMPLListener listener;

	@Test
	public void stmtEventTest() {

		listener = new JenaSMPLListener(FileUtils.UNIV_BENCH_RDFS_MODIFIED, null);

		String[] eventTriple = new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication9",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" };

		Resource subject = ResourceFactory.createResource(eventTriple[0]);
		Property predicate = (eventTriple[1] != RDFSUtils.TYPE_PROPERTY) ? ResourceFactory.createProperty(eventTriple[1]) : RDF.type;
		RDFNode object = ResourceFactory.createResource(eventTriple[2]);
		Statement stmt = ResourceFactory.createStatement(subject, predicate, object);

		final StatementEvent statementEvent = new StatementEvent(stmt, 0, System.currentTimeMillis());

		listener.update(new EventBean[] { new EventBean() {

			@Override
			public Object getUnderlying() {
				return statementEvent;
			}

			@Override
			public Object getFragment(String propertyExpression) throws PropertyAccessException {
				return null;
			}

			@Override
			public EventType getEventType() {
				return null;
			}

			@Override
			public Object get(String propertyExpression) throws PropertyAccessException {
				// TODO Auto-generated method stub
				return null;
			}
		} }, null);

	}

	@Test
	public void tripleEventTest() {

		listener = new JenaSMPLListener(FileUtils.UNIV_BENCH_RDFS_MODIFIED, null);

		String[] eventTriple = new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication9",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" };

		Resource subject = ResourceFactory.createResource(eventTriple[0]);
		Property predicate = (eventTriple[1] != RDFSUtils.TYPE_PROPERTY) ? ResourceFactory.createProperty(eventTriple[1]) : RDF.type;
		RDFNode object = ResourceFactory.createResource(eventTriple[2]);

		final TripleEvent statementEvent = new TripleEvent(subject, predicate, object, 0, System.currentTimeMillis());

		listener.update(new EventBean[] { new EventBean() {

			@Override
			public Object getUnderlying() {
				return statementEvent;
			}

			@Override
			public Object getFragment(String propertyExpression) throws PropertyAccessException {
				return null;
			}

			@Override
			public EventType getEventType() {
				return null;
			}

			@Override
			public Object get(String propertyExpression) throws PropertyAccessException {
				// TODO Auto-generated method stub
				return null;
			}
		} }, null);

	}

	@Test
	public void graphEventTest() {

		listener = new JenaSMPLListener(FileUtils.UNIV_BENCH_RDFS_MODIFIED, null);

		String[] eventTriple = new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor" };

		Graph graph = ModelFactory.createMemModelMaker().createDefaultModel().getGraph();

		for (int i = 0; i < 10; i++) {

			Resource subject = ResourceFactory.createResource(eventTriple[0] + 1);
			Property predicate = (eventTriple[1] != RDFSUtils.TYPE_PROPERTY) ? ResourceFactory.createProperty(eventTriple[1]) : RDF.type;
			RDFNode object = ResourceFactory.createResource(eventTriple[2] + (10 - i));
			graph.add(new Triple(subject.asNode(), predicate.asNode(), object.asNode()));
		}

		final GraphEvent statementEvent = new GraphEvent(graph, 0, System.currentTimeMillis());

		listener.update(new EventBean[] { new EventBean() {

			@Override
			public Object getUnderlying() {
				return statementEvent;
			}

			@Override
			public Object getFragment(String propertyExpression) throws PropertyAccessException {
				return null;
			}

			@Override
			public EventType getEventType() {
				return null;
			}

			@Override
			public Object get(String propertyExpression) throws PropertyAccessException {
				// TODO Auto-generated method stub
				return null;
			}
		} }, null);

	}
}