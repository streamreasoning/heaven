package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.ontology.classes.publication;

public class JournalArticle extends Article {

	private static final long serialVersionUID = -6624719005165085861L;

	public JournalArticle(String object) {
		super(object);
	}

	public JournalArticle() {
		super("http://swat.cse.lehigh.edu/onto/univ-bench.owl#JournalArticle");
	}

}
