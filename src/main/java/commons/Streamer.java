package commons;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Streamer {
	private static final Logger logger = LoggerFactory
			.getLogger(Streamer.class);
	String UNIV_BENCH = "http://swat.cse.lehigh.edu/onto/univ-bench.owl#";
	String RDF_SYNTAX = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	String UNI_PREF = "http://www.Department0.University0.edu/";
	private long previousTs = -1;
	private final long firstTimestamp = 0;
	private static List<String[]> events = new ArrayList<String[]>();
	private static boolean dataType = false;
	private static final String[] datatypeProperties = { "age", "name",
			"emailAddress", "officeNumber", "researchInterest", "title",
			"telephone" };

	public void stream() throws Exception {
		File file = new File("src/main/resource/University0_0.nt");
		// File file = new File("/home/fabio/Scrivania/full-game");
		int count = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;

			// initialization
			while ((line = br.readLine()) != null) {

				String[] s = Parser.parseTriple(line,
						"src/main/resources/sample", false);
				if (s.length > 3) {
					throw new Exception("Too much arguments");
				}

				s[0] = s[0].replace("<", "");
				s[0] = s[0].replace(">", "");
				s[0] = s[0].replace(RDF_SYNTAX, "");
				s[0] = s[0].replace(UNIV_BENCH, "");
				s[0] = s[0].replace(UNI_PREF, "");

				s[1] = s[1].split("#")[1];
				s[1] = s[1].replace("<", "");
				s[1] = s[1].replace(">", "");

				s[2] = s[2].replace("<", "");
				s[2] = s[2].replace(">", "");
				s[2] = s[2].replace(RDF_SYNTAX, "");
				s[2] = s[2].replace(UNIV_BENCH, "");
				s[2] = s[2].replace(UNI_PREF, "");
				if (datatype(s[1]))
					continue;

				if (count > 1)
					events.add(s);

				count++;
			}
			System.out.println("streamer count: " + count);
			br.close();
		} catch (IOException e) {
			logger.error("Error while reading the input file", e);
		}
	}

	private boolean datatype(String s) {
		for (String p : datatypeProperties) {
			if (p.equals(s))
				return true;
		}
		return false;
	}

	public static String[] getEvent() {
		if (events != null && !events.isEmpty()) {
			System.out.println("remain " + events.size() + " events");
			return events.remove(0);
			//TODO qui mi devo inserire per creare il nuovo evento.
		}
		return null;
	}
	
	

	public static boolean hasNext() {
		return events.size() > 0;
	}
}