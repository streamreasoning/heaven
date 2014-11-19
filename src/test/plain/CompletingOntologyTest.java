package plain;

import static org.junit.Assert.assertEquals;
import it.polimi.processing.rspengine.esper.plain.CompletingOntology;
import it.polimi.processing.rspengine.esper.plain.Ontology;
import it.polimi.utils.FileUtils;

import java.util.Arrays;
import java.util.Map;

import org.junit.Test;

public class CompletingOntologyTest {

	@Test
	public void firstTest() {

		String[] classes = new String[] {};
		String[] properties = new String[] { "http://swat.cse.lehigh.edu/onto/univ-bench.owl#worksFor" };
		String[] containsType = classes;
		String[] p = new String[] { CompletingOntology.type()[0], "http://swat.cse.lehigh.edu/onto/univ-bench.owl#mastersDegreeFrom" };

		assertEquals(false, CompletingOntology.containsType(new String[] { "http://swat.cse.lehigh.edu/onto/univ-bench.owl#mastersDegreeFrom" }));
		assertEquals(true, CompletingOntology.containsType(p));

		assertEquals(true, CompletingOntology.notcontainsType(new String[] { "http://swat.cse.lehigh.edu/onto/univ-bench.owl#mastersDegreeFrom" }));
		assertEquals(false, CompletingOntology.notcontainsType(p));

		Map<String, String[]> pmap = CompletingOntology.getProperties();
		String[] expecteds = new String[] { "http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationDate" };
		String[] actuals = pmap.get(Arrays.deepToString(expecteds));
		assertEquals(Arrays.deepToString(expecteds), Arrays.deepToString(actuals));

		// CompletingOntology.subClassOf(classes);
		//
		// CompletingOntology.domain(properties);
		// CompletingOntology.range(new String[] {});
		// ;
		//
		//

	}

	@Test
	public void fullMapsTest() {
		CompletingOntology.init(FileUtils.UNIV_BENCH_RHODF_MODIFIED);
		Map<String, String[]> pmap = CompletingOntology.getProperties();
		for (String props : pmap.keySet()) {
			assertEquals(true, pmap.get(props).length != 0);
		}
	}

	@Test
	public void secondTest() {

		String[] classes = new String[] {};
		String[] properties = new String[] { "http://swat.cse.lehigh.edu/onto/univ-bench.owl#worksFor" };
		String[] containsType = classes;
		String[] p = new String[] { CompletingOntology.type()[0], "http://swat.cse.lehigh.edu/onto/univ-bench.owl#mastersDegreeFrom" };

		assertEquals(false, Ontology.containsType(new String[] { "http://swat.cse.lehigh.edu/onto/univ-bench.owl#mastersDegreeFrom" }));
		assertEquals(true, Ontology.containsType(p));

		assertEquals(true, Ontology.notcontainsType(new String[] { "http://swat.cse.lehigh.edu/onto/univ-bench.owl#mastersDegreeFrom" }));
		assertEquals(false, Ontology.notcontainsType(p));

		Map<String, String[]> pmap = CompletingOntology.getProperties();
		String[] expecteds = new String[] { "http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationDate" };
		String[] actuals = pmap.get(Arrays.deepToString(expecteds));
		assertEquals(Arrays.deepToString(expecteds), Arrays.deepToString(actuals));

		// CompletingOntology.subClassOf(classes);
		//
		// CompletingOntology.domain(properties);
		// CompletingOntology.range(new String[] {});
		// ;
		//
		//

	}

}
