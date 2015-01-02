package it.polimi.processing.events.factory;

import static org.junit.Assert.assertEquals;
import it.polimi.processing.events.RSPTripleSet;
import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.events.profiler.ConstantFlowRateProfiler;
import it.polimi.processing.events.profiler.abstracts.FlowRateProfiler;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ConstantEventBuilderTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void constantEventBuilderTest() {
		FlowRateProfiler<RSPTripleSet> eb = new ConstantFlowRateProfiler(1, 0);

		TripleContainer tc1 = new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication9",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" });

		TripleContainer tc2 = new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication8",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" });

		assertEquals(false, eb.canSend()); // The first RSPEvent

		eb.append(tc1);

		assertEquals(true, eb.canSend());

		RSPTripleSet event = eb.getEvent();

		assertEquals(1, event.getEventTriples().size());

		assertEquals(true, eb.canSend());

		eb.append(tc1);
		eb.append(tc2);

		assertEquals(true, eb.canSend());

		event = eb.getEvent();
		assertEquals(1, event.getEventTriples().size());

	}

	@Test
	public void constantEventBuilderRealCaseTest() {
		FlowRateProfiler<RSPTripleSet> eb = new ConstantFlowRateProfiler(1, 0);

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

	@Test
	public void constantEventBuilderTestSingleAdd() {
		FlowRateProfiler<RSPTripleSet> eb = new ConstantFlowRateProfiler(1, 0);

		TripleContainer tc1 = new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication9",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" });

		TripleContainer tc2 = new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication8",
				"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor", "http://www.Department1.University1.edu/AssociateProfessor2" });

		assertEquals(false, eb.canSend()); // The first RSPEvent

		eb.append(tc1);

		assertEquals(true, eb.canSend());

		RSPTripleSet event = eb.getEvent();

		assertEquals(1, event.getEventTriples().size());

		assertEquals(true, eb.canSend());

		eb.append(tc2);

		assertEquals(true, eb.canSend());

		event = eb.getEvent();
		assertEquals(1, event.getEventTriples().size());

	}

	@Test
	public void constantEventBuilderRealCaseTestSingleAdd() {
		FlowRateProfiler<RSPTripleSet> eb = new ConstantFlowRateProfiler(1, 0);

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
