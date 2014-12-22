package processing.jena.graph;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.graph.compose.Union;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

public class UnionGraphTest {

	public static void unionTest() {

		Graph finalGraph = ModelFactory.createMemModelMaker().createDefaultModel().getGraph();
		Graph abox;
		for (int i = 1; i < 5; i++) {

			abox = ModelFactory.createMemModelMaker().createDefaultModel().getGraph();

			Resource subject = ResourceFactory.createResource("http://www.Department1.University1.edu/AssociateProfessor2/Publication" + i + "" + i);
			Property predicate = ResourceFactory.createProperty("http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor");
			RDFNode object = ResourceFactory.createResource("http://www.Department1.University1.edu/AssociateProfessor2" + i);

			abox.add(new Triple(subject.asNode(), predicate.asNode(), object.asNode()));

			finalGraph = new Union(finalGraph, abox);

		}

	}

	public static void main(String[] args) {
		unionTest();
	}

}
