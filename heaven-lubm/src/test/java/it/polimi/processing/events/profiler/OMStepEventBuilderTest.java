package it.polimi.processing.events.profiler;

import static org.junit.Assert.assertEquals;
import it.polimi.heaven.core.teststand.events.HeavenInput;
import it.polimi.heaven.core.teststand.rspengine.events.Stimulus;
import it.polimi.heaven.core.teststand.streamer.Encoder;
import it.polimi.heaven.core.teststand.streamer.flowrateprofiler.FlowRateProfiler;
import it.polimi.heaven.core.teststand.streamer.flowrateprofiler.profiles.StepFactorFlowRateProfiler;
import it.polimi.heaven.core.teststand.streamer.lubm.LUBMFlowRateProfiler;
import it.polimi.heaven.core.teststand.streamer.lubm.LUBMParser;
import lombok.extern.log4j.Log4j;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

@Log4j
public class OMStepEventBuilderTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();
	private final LUBMParser parser = new LUBMParser();

	public void stepEventBuilderTest() {

		int height = 5;
		int factor = 10;
		int initSize = 5;

		LUBMFlowRateProfiler eb = new StepFactorFlowRateProfiler(parser, factor, height, initSize, 0, 100); // 1
		// 5
		// 10
		eb.setEncoder(new Encoder() {

			@Override
			public Stimulus[] encode(HeavenInput e) {
				return null;
			}
		});
		assertEquals(false, eb.isReady()); // The first RSPEvent

		eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication0> "
				+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> "
				+ "<http://www.Department1.University1.edu/AssociateProfessor2>");
		eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication13> "
				+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> "
				+ "<http://www.Department1.University1.edu/AssociateProfessor21>");
		eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication12> "
				+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> "
				+ "<http://www.Department1.University1.edu/AssociateProfessor22>");
		eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication13> "
				+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> "
				+ "<http://www.Department1.University1.edu/AssociateProfessor23>");
		eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication14> "
				+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> "
				+ "<http://www.Department1.University1.edu/AssociateProfessor24>");

		assertEquals(true, eb.isReady()); // The first RSPEvent

		HeavenInput event = eb.build();

		assertEquals(initSize, event.size());

		for (int eventNumber = 2; eventNumber < 10; eventNumber++) {
			for (int i = 0; i < eventNumber * height; i++) {
				eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication" + i + "" + i + "> "
						+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> "
						+ "<http://www.Department1.University1.edu/AssociateProfessor2" + i + ">");

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
		FlowRateProfiler eb = new StepFactorFlowRateProfiler(parser, width, factor, size, 0, 100);

		eb.setEncoder(new Encoder() {

			@Override
			public Stimulus[] encode(HeavenInput e) {
				return null;
			}
		});
		/*
		 * / / 10 10 10 10 10 / / 100 100 100 100 100
		 * 
		 * 1000 1000 1000 1000
		 */

		assertEquals(false, eb.isReady()); // The first RSPEvent

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < size; j++) {
				eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication14" + j + i + "> "
						+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> "
						+ "<http://www.Department1.University1.edu/AssociateProfessor24>");

			}

			assertEquals(true, eb.isReady()); // The first RSPEvent

			event = eb.build();
			System.out.println(event.size());
			assertEquals(size, event.size());
		}

		size *= factor;

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < size; j++) {
				eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication14" + j + i + "> "
						+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> "
						+ "<http://www.Department1.University1.edu/AssociateProfessor24>");

			}

			assertEquals(true, eb.isReady()); // The first RSPEvent

			event = eb.build();
			System.out.println(event.size());
			assertEquals(size, event.size());
		}

		size *= factor;

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < size; j++) {
				eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication14" + j + i + "> "
						+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> "
						+ "<http://www.Department1.University1.edu/AssociateProfessor24>");

			}

			assertEquals(true, eb.isReady()); // The first RSPEvent

			event = eb.build();
			System.out.println(event.size());
			assertEquals(size, event.size());
		}

	}
}
