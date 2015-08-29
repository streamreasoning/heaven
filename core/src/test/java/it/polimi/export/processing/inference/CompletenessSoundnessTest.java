package it.polimi.export.processing.inference;

import it.polimi.export.processing.validation.DatasetAnalyser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import lombok.extern.log4j.Log4j;

import org.apache.jena.riot.RDFDataMgr;
import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;

@Log4j
public class CompletenessSoundnessTest {
	@Rule
	public ErrorCollector jenasmplCompletenessCollector = new ErrorCollector();
	@Rule
	public ErrorCollector jenarhodfCompletenessCollector = new ErrorCollector();
	@Rule
	public ErrorCollector jenafullCompletenessCollector = new ErrorCollector();
	@Rule
	public ErrorCollector jenarhodfSoundnessCollector = new ErrorCollector();
	@Rule
	public ErrorCollector jenasmplSoundnessCollector = new ErrorCollector();
	@Rule
	public ErrorCollector jenafullSoundnessCollector = new ErrorCollector();

	String expPath = "src/main/resources/data/output/2014-11-30/";

	@Test
	public void jenarhodfTest() throws IOException {

		Dataset plain = RDFDataMgr.loadDataset(expPath + "plain2369/" + "Result_EN0__2014_11_30__CLND_UNIV10INDEX0SEED01000Lines.trig");
		Dataset rhodf = RDFDataMgr.loadDataset(expPath + "jenarhodf/" + "Result_EN0__2014_11_30__CLND_UNIV10INDEX0SEED01000Lines.trig");
		DatasetAnalyser analyser = new DatasetAnalyser(rhodf);

		Iterator<String> listNames = plain.listNames();
		while (listNames.hasNext()) {
			String modelName = listNames.next();
			Boolean complete = analyser.isComplete(modelName, plain.getNamedModel(modelName));
			Boolean sound = analyser.isSound(modelName, plain.getNamedModel(modelName));

			if (!complete) {
				log.info("RHODF Not Complete " + modelName);
				Model completeDiff = analyser.getCompleteDiff(modelName, plain.getNamedModel(modelName));
				write(modelName, completeDiff, "/jenarhodf/COMPLETEDIFF");

			}
			if (!sound) {
				log.info("RHODF Not Sound " + modelName);
				write(modelName, analyser.getSoundDiff(modelName, plain.getNamedModel(modelName)), "/jenarhodf/SOUNDIFF");
			}

			jenarhodfCompletenessCollector.checkThat(complete, CoreMatchers.equalTo(true));
			jenarhodfSoundnessCollector.checkThat(sound, CoreMatchers.equalTo(true));
		}

	}

	@Test
	public void smplTest() throws IOException {

		Dataset plain = RDFDataMgr.loadDataset(expPath + "plain2369/" + "Result_EN0__2014_11_30__CLND_UNIV10INDEX0SEED01000Lines.trig");
		Dataset smpl = RDFDataMgr.loadDataset(expPath + "jenasmpl/" + "Result_EN0__2014_11_30__CLND_UNIV10INDEX0SEED01000Lines.trig");
		DatasetAnalyser analyser = new DatasetAnalyser(smpl);

		Iterator<String> listNames = plain.listNames();
		while (listNames.hasNext()) {
			String modelName = listNames.next();
			Boolean complete = analyser.isComplete(modelName, plain.getNamedModel(modelName));
			Boolean sound = analyser.isSound(modelName, plain.getNamedModel(modelName));

			if (!complete) {
				log.info("JenaSMPL Not Complete " + modelName);
				Model completeDiff = analyser.getCompleteDiff(modelName, plain.getNamedModel(modelName));
				write(modelName, completeDiff, "/jenasmpl/COMPLETEDIFF");

			}
			if (!sound) {
				log.info("JenaSMPL Not Sound " + modelName);
				write(modelName, analyser.getSoundDiff(modelName, plain.getNamedModel(modelName)), "/jenasmpl/SOUNDDIFF");
			}

			jenasmplCompletenessCollector.checkThat(complete, CoreMatchers.equalTo(true));
			jenasmplSoundnessCollector.checkThat(sound, CoreMatchers.equalTo(true));
		}

	}

	@Test
	public void fullTest() throws IOException {

		Dataset plain = RDFDataMgr.loadDataset(expPath + "plain2369/" + "Result_EN0__2014_11_30__CLND_UNIV10INDEX0SEED01000Lines.trig");
		Dataset full = RDFDataMgr.loadDataset(expPath + "jenafull/" + "Result_EN0__2014_11_30__CLND_UNIV10INDEX0SEED01000Lines.trig");
		DatasetAnalyser analyser = new DatasetAnalyser(full);

		Iterator<String> listNames = plain.listNames();
		while (listNames.hasNext()) {
			String modelName = listNames.next();
			Boolean complete = analyser.isComplete(modelName, plain.getNamedModel(modelName));
			Boolean sound = analyser.isSound(modelName, plain.getNamedModel(modelName));

			if (!complete) {
				log.info("JenaFULL Not Complete " + modelName);
				Model completeDiff = analyser.getCompleteDiff(modelName, plain.getNamedModel(modelName));
				write(modelName, completeDiff, "/jenafull/COMPLETEDIFF");

			}
			if (!sound) {
				log.info("JenaFULL Not Sound " + modelName);
				write(modelName, analyser.getSoundDiff(modelName, plain.getNamedModel(modelName)), "/jenafull/SOUNDDIFF");
			}

			jenafullCompletenessCollector.checkThat(complete, CoreMatchers.equalTo(true));
			jenafullSoundnessCollector.checkThat(sound, CoreMatchers.equalTo(true));
		}

	}

	protected void write(String name, Model m, String w) {
		File file = new File(expPath + w + "_EN0__2014_11_30__CLND_UNIV10INDEX0SEED01000Lines.nt");
		try (FileOutputStream fop = new FileOutputStream(file, true)) {
			if (!file.exists()) {
				file.createNewFile();
			}
			fop.write(name.getBytes());
			fop.write(System.getProperty("line.separator").getBytes());
			m.write(fop, "N-TRIPLE");
			fop.write(System.getProperty("line.separator").getBytes());
			fop.flush();
			fop.close();

		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
