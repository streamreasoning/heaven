package it.polimi.processing.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import it.polimi.heaven.core.teststand.data.Line;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class TripleContainerTest {

	@Test
	public void zeroHashCodeTest() {

		Line tc = new Line();
		assertEquals(tc.hashCode(), 0);
	}

	@Test
	public void emptyHashCodeTest() {

		Line tc = new Line(new String[] { "", "", "" });
		assertEquals(tc.hashCode(), 31 * ("".hashCode() * 3));
	}

	@Test
	public void simpleHashCodeTest() {
		// <http://www.Department1.University1.edu/AssociateProfessor2/Publication9>
		// <http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor>
		// <http://www.Department1.University1.edu/AssociateProfessor2> .
		Line tc = new Line(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication9",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" });
		assertEquals(
				tc.hashCode(),
				31 * ("http://www.Department1.University1.edu/AssociateProfessor2/Publication9".hashCode()
						+ "http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor".hashCode() + "http://www.Department1.University1.edu/AssociateProfessor2"
						.hashCode()));
	}

	@Test
	public void setUniqueTest() {
		// <http://www.Department1.University1.edu/AssociateProfessor2/Publication9>
		// <http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor>
		// <http://www.Department1.University1.edu/AssociateProfessor2> .
		Line tc1 = new Line(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication9",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" });

		Line tc2 = new Line(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication9",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" });

		Set<Line> set = new HashSet<Line>();

		set.add(tc1);
		set.add(tc2);

		assertTrue(tc1.equals(tc2));

		assertEquals(tc1.hashCode(), tc2.hashCode());

		assertEquals(set.size(), 1);

	}
}
