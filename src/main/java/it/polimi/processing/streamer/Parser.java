package it.polimi.processing.streamer;

import it.polimi.processing.collector.data.TriG;
import it.polimi.processing.events.TripleContainer;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO
public class Parser {

	private static final String TRIG_REGEX_V2 = "(<http:\\/\\/[A-Za-z\\.]*\\.[A-Za-z]*\\/[0-9]*>) \\{\\n(((<.+>) \\.\\n)*)\\}";

	private static final Pattern PATTER_TRIG_REGEX_V2 = Pattern.compile(TRIG_REGEX_V2);
	public static final String EOF = System.getProperty("line.separator");

	public static String[] parseTriple(String tripleInput) {
		return parseTriple(tripleInput, "", false);
	}

	public static String[] parseTriple(String tripleInput, String fileId, boolean rewriteBlankNodes) {

		String[] values = new String[3];

		String triple = tripleInput.replace("><", "> <");

		// Parse subject
		if (triple.startsWith("<")) {
			values[0] = triple.substring(0, triple.indexOf('>') + 1).replace("<", "").replace(">", "");
		} else { // Is a bnode
			if (rewriteBlankNodes) {
				values[0] = "_:" + sanitizeBlankNodeName(fileId) + triple.substring(2, triple.indexOf(' '));
			} else {
				values[0] = triple.substring(0, triple.indexOf(' ')).replace("<", "").replace(">", "");
			}
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
			if (rewriteBlankNodes) {
				values[2] = "_:" + sanitizeBlankNodeName(fileId) + triple.substring(2, triple.indexOf(' '));
			} else {
				values[2] = triple.substring(0, triple.indexOf(' ')).replace("<", "").replace(">", "");
			}
		}

		return values;
	}

	private static String sanitizeBlankNodeName(String filename) {
		StringBuffer ret = new StringBuffer(filename.length());
		if (!filename.isEmpty()) {
			char charAt0 = filename.charAt(0);
			if (Character.isLetter(charAt0))
				ret.append(charAt0);
		}
		for (int i = 1; i < filename.length(); i++) {
			char ch = filename.charAt(i);
			if (Character.isLetterOrDigit(ch)) {
				ret.append(ch);
			}
		}
		return ret.toString();
	}

	public static boolean triGMatch(String line) {
		return PATTER_TRIG_REGEX_V2.matcher(line).matches();
	}

	public static TriG parseTrigGraph(String line) {

		Matcher matcher = PATTER_TRIG_REGEX_V2.matcher(line);

		if (matcher.find()) {

			String key = matcher.group(1);
			String body = matcher.group(2);
			String[] tripleBody = Parser.parseTrigBody(body);

			Set<TripleContainer> triples = new HashSet<TripleContainer>();
			if (tripleBody != null) {
				for (String triple : tripleBody) {
					String[] parseTriple = Parser.parseTriple(triple, "", false);
					triples.add(new TripleContainer(parseTriple));
				}
			}

			return new TriG(key, triples);
		}

		return null;
		// TODO exception?
	}

	/**
	 * @param triGBody
	 *            the body of the trig graph
	 * @return the array containing each triple in the graph
	 */
	private static String[] parseTrigBody(String triGBody) {

		return triGBody.split(EOF);

	}
}
