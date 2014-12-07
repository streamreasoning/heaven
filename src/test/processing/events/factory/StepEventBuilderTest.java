package processing.events.factory;

import static org.junit.Assert.assertEquals;
import it.polimi.processing.events.RSPEvent;
import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.events.factory.StepEventBuilder;
import it.polimi.processing.events.factory.abstracts.EventBuilder;

import java.util.HashSet;
import java.util.Set;

import lombok.extern.log4j.Log4j;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

@Log4j
public class StepEventBuilderTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void stepEventBuilderTest() {

		int height = 5;
		int width = 1;
		int initSize = 1;

		EventBuilder<RSPEvent> eb = new StepEventBuilder(height, width, initSize, 0); // 1 5 10 15
																						// 20

		assertEquals(false, eb.canSend()); // The first RSPEvent
		Set<TripleContainer> set;

		set = new HashSet<TripleContainer>();
		set.add(new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication0",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" }));
		eb.append(set);

		assertEquals(true, eb.canSend()); // The first RSPEvent

		RSPEvent event = eb.getEvent();
		assertEquals(initSize, event.size());

		for (int eventNumber = 1; eventNumber < 5; eventNumber++) {
			set = new HashSet<TripleContainer>();
			for (int i = 0; i < eventNumber * height; i++) {
				set.add(new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication" + i + "" + i,
						"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor",
						"http://www.Department1.University1.edu/AssociateProfessor2" + i }));

			}
			eb.append(set);
			event = eb.getEvent();
			log.info("Event Size [" + event.size() + "]");
			assertEquals(height * eventNumber, event.size());
		}
	}

	@Test
	public void constantEventBuilderNotFullIAETest() {
		exception.expect(IllegalArgumentException.class);
		int height = 5;
		int width = 1;
		int initSize = 1;
		int wrongHeight = 9;

		EventBuilder<RSPEvent> eb = new StepEventBuilder(height, width, initSize, 0); // 1 5 10 15
																						// 20

		assertEquals(false, eb.canSend()); // The first RSPEvent
		Set<TripleContainer> set;

		set = new HashSet<TripleContainer>();
		set.add(new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication0",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" }));
		eb.append(set);
		RSPEvent event = eb.getEvent();
		assertEquals(initSize, event.size());

		for (int eventNumber = 1; eventNumber < 5; eventNumber++) {
			set = new HashSet<TripleContainer>();
			for (int i = 0; i < eventNumber * wrongHeight; i++) {
				set.add(new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication" + i + "" + i,
						"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor",
						"http://www.Department1.University1.edu/AssociateProfessor2" + i }));

			}
			eb.append(set);
			event = eb.getEvent();
			log.info("Event Size [" + event.size() + "] Expected [" + height * eventNumber + "]");
			assertEquals(height * eventNumber, event.size());
		}
	}

	@Test
	public void stepEventBuilderTestSingleAdd() {

		int height = 5;
		int width = 1;
		int initSize = 1;

		EventBuilder<RSPEvent> eb = new StepEventBuilder(height, width, initSize, 0); // 1 5 10 15
																						// 20

		assertEquals(false, eb.canSend()); // The first RSPEvent

		eb.append(new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication0",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" }));
		RSPEvent event = eb.getEvent();
		assertEquals(true, eb.canSend());
		assertEquals(initSize, event.size());

		for (int eventNumber = 1; eventNumber < 5; eventNumber++) {
			for (int i = 0; i < eventNumber * height; i++) {
				eb.append(new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication" + i + "" + i,
						"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor",
						"http://www.Department1.University1.edu/AssociateProfessor2" + i }));

			}
			event = eb.getEvent();
			assertEquals(true, eb.canSend());
			log.info("Event Size [" + event.size() + "]");
			assertEquals(height * eventNumber, event.size());
		}
	}

	@Test
	public void stepEventBuilderNotFullIAETestSingleAdd() {
		int height = 5;
		int width = 1;
		int initSize = 1;
		int wrongHeight = 9; // I'll have 45 total triple in 5 cycles, obtaingin 7 events

		StepEventBuilder eb = new StepEventBuilder(height, width, initSize, 0); // 1
																				// 5
																				// 10
																				// 15
																				// 20

		assertEquals(false, eb.canSend()); // The first RSPEvent

		eb.append(new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication0",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" }));
		RSPEvent event = eb.getEvent();
		assertEquals(initSize, event.size());
		int eventNumber = 1;
		for (eventNumber = 1; eventNumber < 5; eventNumber++) {
			for (int i = 0; i < eventNumber * wrongHeight; i++) {
				eb.append(new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication" + i + "" + i,
						"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor",
						"http://www.Department1.University1.edu/AssociateProfessor2" + i }));
			}
		}
		assertEquals(eventNumber + 2, eb.getEventNumber()); // it automatically split received
															// triples into events
	}
}
