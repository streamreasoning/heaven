package it.polimi.heaven.core.tsimpl.streamer;

public class Parser {

	public static String[] parseTriple(String tripleInput) {

		String[] values = new String[3];

		String triple = tripleInput.replace("><", "> <");

		// Parse subject
		if (triple.startsWith("<")) {
			values[0] = triple.substring(0, triple.indexOf('>') + 1).replace("<", "").replace(">", "");
		} else { // Is a bnode
			values[0] = triple.substring(0, triple.indexOf(' ')).replace("<", "").replace(">", "");
		}

		triple = triple.substring(triple.indexOf(' ') + 1);
		// Parse predicate. It can be only a URI
		values[1] = triple.substring(0, triple.indexOf('>') + 1).replace("<", "").replace(">", "");

		// Parse object
		triple = triple.substring(triple.indexOf(' ') + 1);
		if (triple.startsWith("<")) { // URI
			values[2] = triple.substring(0, triple.indexOf('>') + 1).replace("<", "").replace(">", "");
		} else if (triple.charAt(0) == '"') { // Literal
			values[2] = triple.substring(0, triple.substring(1).indexOf('"') + 2);
			triple = triple.substring(values[2].length(), triple.length());
			values[2] += triple.substring(0, triple.indexOf(' '));
		} else { // Bnode
			values[2] = triple.substring(0, triple.indexOf(' ')).replace("<", "").replace(">", "");
		}

		values[0] = values[0].replace("<", "");
		values[0] = values[0].replace(">", "");

		values[1] = values[1].replace("<", "");
		values[1] = values[1].replace(">", "");

		values[2] = values[2].replace("<", "");
		values[2] = values[2].replace(">", "");

		return values;
	}

}
