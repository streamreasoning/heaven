package it.polimi.processing.events.factory;

import static org.junit.Assert.assertEquals;
import it.polimi.processing.events.RSPTripleSet;
import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.events.factory.StepFlowRateProfiler;
import it.polimi.processing.events.factory.abstracts.FlowRateProfiler;
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
		int initSize = 5;

		FlowRateProfiler<RSPTripleSet> eb = new StepFlowRateProfiler(width, height, initSize, 0); // 1 5 10

		assertEquals(false, eb.canSend()); // The first RSPEvent

		eb.append(new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication0",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" }));
		eb.append(new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication13",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor21" }));
		eb.append(new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication12",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor22" }));
		eb.append(new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication13",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor23" }));
		eb.append(new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication14",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor24" }));

		assertEquals(true, eb.canSend()); // The first RSPEvent

		RSPTripleSet event = eb.getEvent();

		assertEquals(initSize, event.size());

		for (int eventNumber = 2; eventNumber < 10; eventNumber++) {
			for (int i = 0; i < eventNumber * height; i++) {
				eb.append(new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication" + i + "" + i,
						"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor",
						"http://www.Department1.University1.edu/AssociateProfessor2" + i }));

				if (i + 1 == eventNumber * height) {
					assertEquals(true, eb.canSend()); // The first RSPEvent
				} else {
					assertEquals(false, eb.canSend()); // The first RSPEvent
				}
			}
			assertEquals(true, eb.canSend()); // The first RSPEvent
			event = eb.getEvent();
			log.info("Event Size [" + event.size() + "]");
			assertEquals(height * eventNumber, event.size());
		}

	}

}
