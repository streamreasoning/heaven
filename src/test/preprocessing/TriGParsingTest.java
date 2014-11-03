package preprocessing;

import java.util.regex.Pattern;

public class TriGParsingTest {

	private static final Pattern TRIPLE_REGEX = Pattern.compile("\\p{Blank}*" + "(<(.+?)>|_:(.+?))\\p{Blank}+" + "(<(.+?)>)\\p{Blank}+"
			+ "(<(.+?)>|_:(.+?)|(.+?))\\p{Blank}*\\.\\p{Blank}*");
	private static final int SUBJECT_GROUP = 1;
	private static final int PREDICATE_GROUP = 4;
	private static final int OBJECT_GROUP = 6;

	public static final Pattern SUBJECT_REGEX = Pattern.compile("(<([\\x20-\\x7E]+?)>|_:(\\p{Alpha}[\\x20-\\x7E]*?))");

	/**
	 * Pattern for Predicate in NTriples triple pattern.
	 */
	public static final Pattern PREDICATE_REGEX = Pattern.compile("(<([\\x20-\\x7E]+?)>)");

	/**
	 * Pattern for Object in NTriples triple pattern.
	 */
	public static final Pattern OBJECT_REGEX = Pattern.compile("(<([\\x20-\\x7E]+?)>|_:(\\p{Alpha}[\\x20-\\x7E]*?)|([\\x20-\\x7E]+?))");

	public static void main(String[] args) {
		String eof = System.getProperty("line.separator");
		String line = "<http://example.org/0/0> {"
				+ eof
				+ "<http://www.Department0.University0.edu/UndergraduateStudent4><http://www.w3.org/1999/02/22-rdf-syntax-ns#type><http://swat.cse.lehigh.edu/onto/univ-bench.owl#UndergraduateStudent> ."
				+ eof + "}";

		// Parser.parseTrigGraph(line);
		//
		// line = "<http://example.org/0/0> {"
		// + eof
		// +
		// "<http://www.Department0.University0.edu/UndergraduateStudent4><http://www.w3.org/1999/02/22-rdf-syntax-ns#type><http://swat.cse.lehigh.edu/onto/univ-bench.owl#UndergraduateStudent> ."
		// + eof
		// +
		// "<http://www.Department0.University0.edu/UndergraduateStudent4><http://www.w3.org/1999/02/22-rdf-syntax-ns#type><http://swat.cse.lehigh.edu/onto/univ-bench.owl#UndergraduateStudent> ."
		// + eof + "}";
		//
		// Parser.parseTrigGraph(line);

		line = "<http://example.org/0/0> {"
				+ eof
				+ "<http://www.Department0.University0.edu/UndergraduateStudent4><http://www.w3.org/1999/02/22-rdf-syntax-ns#type><http://swat.cse.lehigh.edu/onto/univ-bench.owl#UndergraduateStudent> ."
				+ eof
				+ "<http://www.Department0.University0.edu/UndergraduateStudent4><http://www.w3.org/1999/02/22-rdf-syntax-ns#type><http://www.w3.org/2000/01/rdf-schema#Resource> ."
				+ eof
				+ "<http://www.Department0.University0.edu/AssistantProfessor4><http://www.w3.org/1999/02/22-rdf-syntax-ns#type><http://swat.cse.lehigh.edu/onto/univ-bench.owl#Employee> ."
				+ eof + "}";

	}
}
