package it.polimi.processing.events.profiler;

import static org.junit.Assert.assertEquals;
import it.polimi.heaven.core.ts.events.Stimulus;
import it.polimi.heaven.core.ts.events.TripleContainer;
import it.polimi.heaven.core.ts.streamer.flowrateprofiler.FlowRateProfiler;
import it.polimi.heaven.core.tsimpl.streamer.rdf2rdfstream.flowrateprofiler.StepFlowRateProfiler;
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

		FlowRateProfiler<Stimulus, TripleContainer> eb = new StepFlowRateProfiler(width, height, initSize, 0, 100); // 1
		// 5
		// 10

		assertEquals(false, eb.isReady()); // The first RSPEvent

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

		assertEquals(true, eb.isReady()); // The first RSPEvent

		Stimulus event = eb.getEvent();

		assertEquals(initSize, event.size());

		for (int eventNumber = 2; eventNumber < 10; eventNumber++) {
			for (int i = 0; i < eventNumber * height; i++) {
				eb.append(new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication" + i + "" + i,
						"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor",
						"http://www.Department1.University1.edu/AssociateProfessor2" + i }));

				if (i + 1 == eventNumber * height) {
					assertEquals(true, eb.isReady()); // The first RSPEvent
				} else {
					assertEquals(false, eb.isReady()); // The first RSPEvent
				}
			}
			assertEquals(true, eb.isReady()); // The first RSPEvent
			event = eb.getEvent();
			log.info("Event Size [" + event.size() + "]");
			assertEquals(height * eventNumber, event.size());
		}

	}

	@Test
	public void stepEventBuilderTestLong() {

		int height = 5;
		int width = 5;
		int size = 5;
		Stimulus event;
		FlowRateProfiler<Stimulus, TripleContainer> eb = new StepFlowRateProfiler(width, height, size, 0, 100); /*
																												 * 5
																												 * 5
																												 * 5
																												 * 5
																												 * 5
																												 * /
																												 * /
																												 * 10
																												 * 10
																												 * 10
																												 * 10
																												 * 10
																												 * /
																												 * /
																												 * 15
																												 * 15
																												 * 15
																												 * 15
																												 * 15
																												 */

		assertEquals(false, eb.isReady()); // The first RSPEvent

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < size; j++) {
				eb.append(new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication14" + j + i,
						"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor",
						"http://www.Department1.University1.edu/AssociateProfessor24" }));

			}

			assertEquals(true, eb.isReady()); // The first RSPEvent

			event = eb.getEvent();
			System.out.println(event.size());
			assertEquals(size, event.size());
		}

		size += height;

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < size; j++) {
				eb.append(new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication14" + j + i,
						"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor",
						"http://www.Department1.University1.edu/AssociateProfessor24" }));

			}

			assertEquals(true, eb.isReady()); // The first RSPEvent

			event = eb.getEvent();
			System.out.println(event.size());
			assertEquals(size, event.size());
		}

		size += height;

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < size; j++) {
				eb.append(new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication14" + j + i,
						"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor",
						"http://www.Department1.University1.edu/AssociateProfessor24" }));

			}

			assertEquals(true, eb.isReady()); // The first RSPEvent

			event = eb.getEvent();
			System.out.println(event.size());
			assertEquals(size, event.size());
		}

	}

}