package it.polimi.processing.collector.saver.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.StmtIterator;

@AllArgsConstructor
@Data
public class TriG implements CollectableData {
	private final String e_id, name;
	private final Model m;

	@Override
	public String getData() {
		String eol = System.getProperty("line.separator");
		String s = "<http://example.org/" + e_id.hashCode() + " {";
		StmtIterator iterator = m.listStatements();
		while (iterator.hasNext()) {

			Triple t = iterator.next().asTriple();
			s += eol + "<" + t.getSubject() + ">" + "<" + t.getPredicate()
					+ ">" + "<" + t.getObject() + "> .";
		}

		s += eol + "}";
		return s;

	}
}
