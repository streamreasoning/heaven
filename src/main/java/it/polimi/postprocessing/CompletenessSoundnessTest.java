package it.polimi.postprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import lombok.extern.log4j.Log4j;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

@Log4j
public class CompletenessSoundnessTest {
	@Rule
	public ErrorCollector jenasmplCollector = new ErrorCollector();
	@Rule
	public ErrorCollector jenarhodfCollector = new ErrorCollector();

	@Test
	public void jenarhodfTest() throws IOException {
		String expPath = PostUtils.COMPARATION_OUT_PATH;
		for (String num : new String[] { "50", "100", "250", "500" }) {
			String fileNmae = "_EN0__2014_11_19_inputTrigINIT" + num + "D1GF0SN1R" + ".csv";
			String pathname = expPath + "jenarhodf_vs_plain2369" + "/" + fileNmae;
			log.info("Comparing on [" + pathname + "]");

			File f = new File(pathname);
			BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
			String line;
			int linenumber = 0;
			while ((line = bufferedReader.readLine()) != null) {
				String[] parts = line.split(",");
				Boolean complete = Boolean.parseBoolean(parts[parts.length - 2]);
				Boolean sound = Boolean.parseBoolean(parts[parts.length - 1]);
				if (!(complete && sound))
					log.info("parsing line [" + linenumber + "]");

				jenarhodfCollector.checkThat(complete, CoreMatchers.equalTo(true));
				jenarhodfCollector.checkThat(sound, CoreMatchers.equalTo(true));
				linenumber++;

			}
			bufferedReader.close();

		}

	}

	@Test
	public void jenasmplTest() throws IOException {
		String expPath = PostUtils.COMPARATION_OUT_PATH;
		for (String num : new String[] { "50", "100", "250", "500" }) {
			String fileNmae = "_EN0__2014_11_19_inputTrigINIT" + num + "D1GF0SN1R" + ".csv";
			String pathname = expPath + "jenasmpl_vs_plain2369" + "/" + fileNmae;
			log.info("Comparing on [" + pathname + "]");
			File f = new File(pathname);
			BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
			String line;
			int linenumber = 0;
			while ((line = bufferedReader.readLine()) != null) {
				String[] parts = line.split(",");
				Boolean complete = Boolean.parseBoolean(parts[parts.length - 2]);
				Boolean sound = Boolean.parseBoolean(parts[parts.length - 1]);
				if (!sound)
					log.info("Not Sound at event [" + linenumber + "]");
				if (complete && sound)
					log.info("Complete and sound at event [" + linenumber + "]");

				jenasmplCollector.checkThat(sound, CoreMatchers.equalTo(true));
				// TODO check per la completeness va definito per cosa può non essere sound (query
				// con
				// jena)
				linenumber++;
			}
			bufferedReader.close();
		}

	}
}
