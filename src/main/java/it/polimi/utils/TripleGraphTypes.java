package it.polimi.utils;

/**
 * 
 * This Class represents the encoding format of the triple graph that we can
 * exploit in our system.
 * 
 * @author Riccardo
 * 
 */
public class TripleGraphTypes {

	private TripleGraphTypes() {
	}

	/**
	 * * A graph of three rdf triples were we have a simple rdf triple un a
	 * subject S an object O and a generic non-schema property P, a triple of
	 * the form O typeOf C1 and the form S typeOf C2
	 */
	public static final int UTRIPLES = 3;
	/**
	 * A graph of two rdf triples were we have a simple rdf triple un a subject
	 * S an object O and a generic non-schema property P and a triple of the
	 * form O typeOf C
	 */
	public static final int LTRIPLES = 2;
	/**
	 * A graph of two rdf triples were we have a simple rdf triple un a subject
	 * S an object O and a generic non-schema property P and a triple of the
	 * form S typeOf C
	 */
	public static final int JTRIPLES = 2;
	/**
	 * A simple rdf triple un a subject S an object O and a generic non-schema
	 * property P
	 */
	public static final int TRIPLES_ = 1;

}
