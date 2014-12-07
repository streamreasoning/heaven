package processing.events.factory;

import static org.junit.Assert.assertEquals;
import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.events.factory.ConstantEventBuilder;
import it.polimi.processing.events.factory.abstracts.EventBuilder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ConstantEventBuilderTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void constantEventBuilderTest() {
		EventBuilder<RSPEvent> eb = new ConstantEventBuilder(1, 0);

		TripleContainer tc1 = new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication9",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" });

		TripleContainer tc2 = new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication8",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" });

		Set<TripleContainer> set = new HashSet<TripleContainer>();

		set.add(tc1);

		assertEquals(false, eb.canSend()); // The first RSPEvent

		eb.append(set);

		assertEquals(true, eb.canSend());

		RSPEvent event = eb.getEvent();

		assertEquals(1, event.getEventTriples().size());

		assertEquals(true, eb.canSend());

		set = new HashSet<TripleContainer>();
		set.add(tc2);

		eb.append(set);

		assertEquals(true, eb.canSend());

		event = eb.getEvent();
		assertEquals(1, event.getEventTriples().size());

	}

	@Test
	public void constantEventBuilderNotFullIAETest() {
		EventBuilder<RSPEvent> eb = new ConstantEventBuilder(1, 0);

		TripleContainer tc1 = new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication9",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" });

		TripleContainer tc2 = new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication8",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" });

		Set<TripleContainer> set = new HashSet<TripleContainer>();
		set.add(tc1);
		set.add(tc2);

		exception.expect(IllegalArgumentException.class);
		eb.append(set);

	}

	@Test
	public void constantEventBuilderRealCaseTest() {
		EventBuilder<RSPEvent> eb = new ConstantEventBuilder(1, 0);

		assertEquals(false, eb.canSend());

		for (int i = 0; i < 10; i++) {

			eb.append(new HashSet<TripleContainer>(Arrays.asList(new TripleContainer[] { new TripleContainer(
					new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication" + i,
							"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor",
							"http://www.Department1.University1.edu/AssociateProfessor2" }) })));
			assertEquals(1, eb.getEvent().size());
			assertEquals(true, eb.canSend());

		}

	}

	@Test
	public void constantEventBuilderTestSingleAdd() {
		EventBuilder<RSPEvent> eb = new ConstantEventBuilder(1, 0);

		TripleContainer tc1 = new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication9",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" });

		TripleContainer tc2 = new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication8",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" });

		assertEquals(false, eb.canSend()); // The first RSPEvent

		eb.append(tc1);

		assertEquals(true, eb.canSend());

		RSPEvent event = eb.getEvent();

		assertEquals(1, event.getEventTriples().size());

		assertEquals(true, eb.canSend());

		eb.append(tc2);

		assertEquals(true, eb.canSend());

		event = eb.getEvent();
		assertEquals(1, event.getEventTriples().size());

	}

	@Test
	public void constantEventBuilderRealCaseTestSingleAdd() {
		EventBuilder<RSPEvent> eb = new ConstantEventBuilder(1, 0);

		assertEquals(false, eb.canSend());

		for (int i = 0; i < 10; i++) {

			eb.append(new TripleContainer(
					new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication" + i,
							"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor",
							"http://www.Department1.University1.edu/AssociateProfessor2" }));
			assertEquals(1, eb.getEvent().size());
			assertEquals(true, eb.canSend());

		}

	}

}
