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

	private static final String UNIV_BENCH = "http://swat.cse.lehigh.edu/onto/univ-bench.owl#";
	private static final String RDF_SYNTAX = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	private static final String UNI_PREF = "http://www.Department0.University0.edu/";

	public static final boolean USEDATATYPEPROPERTIES = false;

	private static final String[] DATATYPEPROPERTIES = { "age", "name",
			"emailAddress", "officeNumber", "researchInterest", "title",
			"telephone" };

	private static List<String[]> eventBuffer = new ArrayList<String[]>();
	private static EsperModel model;

	private static BufferedReader br;

	public static void stream(EsperModel m) throws Exception {

		model = m;

		File file = new File("src/main/resource/University0_0.nt");

		int count = 0;

		try {
			br = new BufferedReader(new FileReader(file));
			String line;

			// initialization
			while ((line = br.readLine()) != null || !eventBuffer.isEmpty()) {

				if (line != null) {

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

					if (isDatatype(s[1]))
						continue;
					else if (model.isBusy()) {
						eventBuffer.add(s);
					} else {
						model.sendEvent(s);
						count++;
					}
				}else{
					br.close();
				}

			}

			System.out.println("streamer count: " + count);
			br.close();
			

		} catch (IOException e) {
			logger.error("Error while reading the input file", e);
		}
	}

	private static boolean isDatatype(String s) {
		for (String p : DATATYPEPROPERTIES) {
			if (p.equals(s))
				return true;
		}
		return false;
	}

}