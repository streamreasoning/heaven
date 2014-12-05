package processing.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.events.factory.ConstantEventBuilder;
import it.polimi.processing.events.factory.abstracts.EventBuilder;

import java.util.HashSet;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class EventBuilderTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void constantEventBuilderTest() {
		EventBuilder<RSPEvent> eb = new ConstantEventBuilder(1);

		TripleContainer tc1 = new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication9",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" });

		TripleContainer tc2 = new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication8",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" });

		Set<TripleContainer> set = new HashSet<TripleContainer>();

		set.add(tc1);

		assertEquals(false, eb.canSend()); // The first RSPEvent

		eb.append(set, 0, 0);

		assertEquals(true, eb.canSend());

		RSPEvent event = eb.getEvent();

		assertEquals(1, event.getEventTriples().size());

		assertEquals(true, eb.canSend());

		set = new HashSet<TripleContainer>();
		set.add(tc2);

		eb.append(set, 1, 0);

		assertEquals(true, eb.canSend());

		event = eb.getEvent();
		assertEquals(1, event.getEventTriples().size());

	}

	@Test
	public void constantEventBuilderNotFullIAETest() {
		EventBuilder<RSPEvent> eb = new ConstantEventBuilder(1);

		TripleContainer tc1 = new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication9",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" });

		TripleContainer tc2 = new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication8",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" });

		Set<TripleContainer> set = new HashSet<TripleContainer>();
		set.add(tc1);
		set.add(tc2);

		exception.expect(IllegalArgumentException.class);
		eb.append(set, 2, 0);

	}

	public void emptyHashCodeTest() {

		TripleContainer tc = new TripleContainer(new String[] { "", "", "" });
		assertEquals(tc.hashCode(), 31 * ("".hashCode() * 3));
	}

	public void simpleHashCodeTest() {
		// <http://www.Department1.University1.edu/AssociateProfessor2/Publication9>
		// <http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor>
		// <http://www.Department1.University1.edu/AssociateProfessor2> .
		TripleContainer tc = new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication9",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" });
		assertEquals(
				tc.hashCode(),
				31 * ("http://www.Department1.University1.edu/AssociateProfessor2/Publication9".hashCode()
						+ "http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor".hashCode() + "http://www.Department1.University1.edu/AssociateProfessor2"
						.hashCode()));
	}

	public void setUniqueTest() {
		// <http://www.Department1.University1.edu/AssociateProfessor2/Publication9>
		// <http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor>
		// <http://www.Department1.University1.edu/AssociateProfessor2> .
		TripleContainer tc1 = new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication9",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" });

		TripleContainer tc2 = new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication9",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" });

		Set<TripleContainer> set = new HashSet<TripleContainer>();

		set.add(tc1);
		set.add(tc2);

		assertTrue(tc1.equals(tc2));

		assertEquals(tc1.hashCode(), tc2.hashCode());

		assertEquals(set.size(), 1);

	}
}
