package it.polimi.processing.events.profiler;

import static org.junit.Assert.assertEquals;
import it.polimi.heaven.core.ts.events.Stimulus;
import it.polimi.heaven.core.ts.events.TripleContainer;
import it.polimi.heaven.core.ts.streamer.flowrateprofiler.FlowRateProfiler;
import it.polimi.heaven.core.tsimpl.streamer.rdf2rdfstream.flowrateprofiler.ConstantFlowRateProfiler;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ConstantEventBuilderTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void constantEventBuilderTest() {
		FlowRateProfiler<Stimulus, TripleContainer> eb = new ConstantFlowRateProfiler(1, 0, 100);

		TripleContainer tc1 = new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication9",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" });

		TripleContainer tc2 = new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication8",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" });

		assertEquals(false, eb.isReady()); // The first RSPEvent

		eb.append(tc1);

		assertEquals(true, eb.isReady());

		Stimulus event = eb.getEvent();

		assertEquals(1, event.getEventTriples().size());

		assertEquals(true, eb.isReady());

		eb.append(tc1);
		eb.append(tc2);

		assertEquals(true, eb.isReady());

		event = eb.getEvent();
		assertEquals(1, event.getEventTriples().size());

	}

	@Test
	public void constantEventBuilderRealCaseTest() {
		FlowRateProfiler<Stimulus, TripleContainer> eb = new ConstantFlowRateProfiler(1, 0, 100);

		assertEquals(false, eb.isReady());

		for (int i = 0; i < 10; i++) {

			eb.append(new TripleContainer(
					new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication" + i,
							"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor",
							"http://www.Department1.University1.edu/AssociateProfessor2" }));
			assertEquals(1, eb.getEvent().size());
			assertEquals(true, eb.isReady());

		}

	}

	@Test
	public void constantEventBuilderTestSingleAdd() {
		FlowRateProfiler<Stimulus, TripleContainer> eb = new ConstantFlowRateProfiler(1, 0, 100);

		TripleContainer tc1 = new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication9",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" });

		TripleContainer tc2 = new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication8",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" });

		assertEquals(false, eb.isReady()); // The first RSPEvent

		eb.append(tc1);

		assertEquals(true, eb.isReady());

		Stimulus event = eb.getEvent();

		assertEquals(1, event.getEventTriples().size());

		assertEquals(true, eb.isReady());

		eb.append(tc2);

		assertEquals(true, eb.isReady());

		event = eb.getEvent();
		assertEquals(1, event.getEventTriples().size());

	}

	@Test
	public void constantEventBuilderRealCaseTestSingleAdd() {
		FlowRateProfiler<Stimulus, TripleContainer> eb = new ConstantFlowRateProfiler(1, 0, 100);

		assertEquals(false, eb.isReady());

		for (int i = 0; i < 10; i++) {

			eb.append(new TripleContainer(
					new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication" + i,
							"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor",
							"http://www.Department1.University1.edu/AssociateProfessor2" }));
			assertEquals(1, eb.getEvent().size());
			assertEquals(true, eb.isReady());

		}

	}

}
