import static org.junit.Assert.*;
import it.polimi.streamer.Parser;
import it.polimi.utils.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.jena.riot.RDFDataMgr;
import org.apache.log4j.Logger;
import org.junit.Test;

import com.hp.hpl.jena.query.Dataset;

public class JenaTest {

	private List<Dataset> datasetList;
	private BufferedReader br;

	public JenaTest() {

		String[] files = {
				"src/main/resource/data/output/trig/jena/_Result_file1.trig",
				"src/main/resource/data/output/trig/plain/_Result_file1.trig" };
		this.datasetList = new ArrayList<Dataset>();

		for (String f : files) {
			datasetList.add(RDFDataMgr.loadDataset(f));
		}
	}

	@Test
	public void testSoundness() throws IOException {

		String line;
		File file = new File(FileUtils.INPUT_FILE_PATH + "file1.txt");
		br = new BufferedReader(new FileReader(file));
		String[] s = null;
		while ((line = br.readLine()) != null) {
			s = Parser.parseTriple(line, "src/main/resources/data/", false);
			if (s.length > 3) {
				throw new IllegalArgumentException("Too much arguments");
			}

			System.out.println("ciao");
			s[0] = s[0].replace("<", "");
			s[0] = s[0].replace(">", "");

			s[1] = s[1].replace("<", "");
			s[1] = s[1].replace(">", "");

			s[2] = s[2].replace("<", "");
			s[2] = s[2].replace(">", "");
			
			String key = "http://example.org/"
					+ ("[" + Arrays.deepToString(s) + "]").hashCode();

			assertFalse(isSound(key));
			assertTrue(isComplete(key));

		}
		
		br.close();
		

	}

	public boolean isCorrect(String key) {
		if (datasetList.size() > 2) {
			Logger.getLogger("it.polimi.logger").info("Too much argumetns");
			return false;
		}
		Dataset ref = datasetList.get(0);
		boolean equals = ref.getNamedModel(key).isIsomorphicWith(
				datasetList.get(1).getNamedModel(key));
		return equals;
	}

	public boolean isComplete(String key) {
		// Model jenaGraph = datasetList.get(0).getNamedModel(key);
		// Model engineGraph = datasetList.get(1).getNamedModel(key);
		// Model diff = jenaGraph.difference(engineGraph);
		return datasetList.get(1).getNamedModel(key)
				.difference(datasetList.get(0).getNamedModel(key)).isEmpty();
	}

	public boolean isSound(String key) {
		// Model jenaGraph = datasetList.get(0).getNamedModel(key);
		// Model engineGraph = datasetList.get(1).getNamedModel(key);
		// Model diff = engineGraph.difference(jenaGraph);
		return datasetList.get(0).getNamedModel(key)
				.difference(datasetList.get(1).getNamedModel(key)).isEmpty();
	}
}
