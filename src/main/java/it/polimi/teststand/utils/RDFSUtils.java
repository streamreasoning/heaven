package it.polimi.teststand.utils;

public class RDFSUtils {

	public static final String[] DATATYPEPROPERTIES = {
			"http://swat.cse.lehigh.edu/onto/univ-bench.owl#age",
			"http://swat.cse.lehigh.edu/onto/univ-bench.owl#name",
			"http://swat.cse.lehigh.edu/onto/univ-bench.owl#emailAddress",
			"http://swat.cse.lehigh.edu/onto/univ-bench.owl#officeNumber",
			"http://swat.cse.lehigh.edu/onto/univ-bench.owl#researchInterest",
			"http://swat.cse.lehigh.edu/onto/univ-bench.owl#title",
			"http://swat.cse.lehigh.edu/onto/univ-bench.owl#telephone" };

	public static boolean isDatatype(String s) {
		for (String p : DATATYPEPROPERTIES) {
			if (p.equals(s))
				return true;
		}
		return false;
	}

	public static boolean isType(String s) {
		return " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>".equals(s);
	}
}
