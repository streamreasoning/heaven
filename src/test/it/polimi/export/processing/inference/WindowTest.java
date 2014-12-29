package plain.inference;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.apache.jena.riot.RDFDataMgr;
import org.junit.Test;

import com.hp.hpl.jena.query.Dataset;

public class WindowTest {

	/**
	 * Run different Model on same input ensuring that, with the same WindowStrategt, controlled by
	 * the TestStand, i'll have the same
	 * windows on each model.
	 */
	@Test
	public void WindowsAreEqualByResult() {
		String expath = "src/main/resources/data/output/2014-11-30/";
		Dataset plain = RDFDataMgr.loadDataset(expath + "plain2369/" + "Window_EN0__2014_11_30_University0_0_clean.trig");
		Dataset rhodf = RDFDataMgr.loadDataset(expath + "jenarhodf/" + "Window_EN0__2014_11_30_University0_0_clean.trig");
		Dataset smpl = RDFDataMgr.loadDataset(expath + "jenasmpl/" + "Window_EN0__2014_11_30_University0_0_clean.trig");

		Iterator<String> listNames = plain.listNames();
		while (listNames.hasNext()) {
			String modelName = listNames.next();
			assertEquals(true, plain.getNamedModel(modelName).difference(rhodf.getNamedModel(modelName)).isEmpty());
			assertEquals(true, plain.getNamedModel(modelName).difference(smpl.getNamedModel(modelName)).isEmpty());
		}

	}

}
