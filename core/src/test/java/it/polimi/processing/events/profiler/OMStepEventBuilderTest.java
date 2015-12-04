package it.polimi.processing.events.profiler;

import static org.junit.Assert.assertEquals;
import it.polimi.heaven.core.ts.data.TripleContainer;
import it.polimi.heaven.core.ts.events.heaven.HeavenInput;
import it.polimi.heaven.core.ts.streamer.flowrateprofiler.FlowRateProfiler;
import it.polimi.heaven.core.tsimpl.streamer.rdf2rdfstream.flowrateprofiler.StepFactorFlowRateProfiler;
import lombok.extern.log4j.Log4j;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

@Log4j
public class OMStepEventBuilderTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	public void stepEventBuilderTest() {

		int height = 5;
		int factor = 10;
		int initSize = 5;

		FlowRateProfiler<HeavenInput, TripleContainer> eb = new StepFactorFlowRateProfiler(factor, height, initSize, 0, 100); // 1
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

		HeavenInput event = eb.build();

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
			event = eb.build();
			log.info("Event Size [" + event.size() + "]");
			assertEquals(height * eventNumber, event.size());
		}

	}

	@Test
	public void stepEventBuilderTestLong() {

		int factor = 10;
		int width = 5;
		int size = 10;
		HeavenInput event;
		FlowRateProfiler<HeavenInput, TripleContainer> eb = new StepFactorFlowRateProfiler(width, factor, size, 0, 100); /*
																														 * /
																														 * /
																														 * 10
																														 * 10
																														 * 10
																														 * 10
																														 * 10
																														 * /
																														 * /
																														 * 100
																														 * 100
																														 * 100
																														 * 100
																														 * 100
																														 * 
																														 * 1000
																														 * 1000
																														 * 1000
																														 * 1000
																														 */

		assertEquals(false, eb.isReady()); // The first RSPEvent

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < size; j++) {
				eb.append(new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication14" + j + i,
						"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor",
						"http://www.Department1.University1.edu/AssociateProfessor24" }));

			}

			assertEquals(true, eb.isReady()); // The first RSPEvent

			event = eb.build();
			System.out.println(event.size());
			assertEquals(size, event.size());
		}

		size *= factor;

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < size; j++) {
				eb.append(new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication14" + j + i,
						"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor",
						"http://www.Department1.University1.edu/AssociateProfessor24" }));

			}

			assertEquals(true, eb.isReady()); // The first RSPEvent

			event = eb.build();
			System.out.println(event.size());
			assertEquals(size, event.size());
		}

		size *= factor;

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < size; j++) {
				eb.append(new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication14" + j + i,
						"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor",
						"http://www.Department1.University1.edu/AssociateProfessor24" }));

			}

			assertEquals(true, eb.isReady()); // The first RSPEvent

			event = eb.build();
			System.out.println(event.size());
			assertEquals(size, event.size());
		}

	}

}
