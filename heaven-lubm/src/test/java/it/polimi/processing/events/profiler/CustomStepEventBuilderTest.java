package it.polimi.processing.events.profiler;

import static org.junit.Assert.assertEquals;
import it.polimi.heaven.core.teststand.events.HeavenInput;
import it.polimi.heaven.core.teststand.rspengine.events.Stimulus;
import it.polimi.heaven.core.teststand.streamer.Encoder;
import it.polimi.heaven.core.teststand.streamer.flowrateprofiler.FlowRateProfiler;
import it.polimi.heaven.core.teststand.streamer.flowrateprofiler.profiles.CustomStepFlowRateProfiler;
import it.polimi.heaven.core.teststand.streamer.lubm.LUBMParser;
import lombok.extern.log4j.Log4j;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

@Log4j
public class CustomStepEventBuilderTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();
	private final LUBMParser parser = new LUBMParser();

	@Test
	public void customStepEventBuilderTest() {

		int initSize = 5;
		int finalSize = 10;
		int width = 2;

		FlowRateProfiler eb = new CustomStepFlowRateProfiler(parser, width, finalSize, initSize, 0, 100);
		eb.setEncoder(new Encoder() {

			@Override
			public Stimulus[] encode(HeavenInput e) {
				return null;
			}
		});
		assertEquals(false, eb.isReady()); // The first RSPEvent

		eb.append("<<http://www.Department1.University1.edu/AssociateProfessor2/Publication0> "
				+ "<<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> <<http://www.Department1.University1.edu/AssociateProfessor2>");
		eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication13> "
				+ "<<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> <<http://www.Department1.University1.edu/AssociateProfessor21>>");
		eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication12> "
				+ "<<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> <<http://www.Department1.University1.edu/AssociateProfessor22>");
		eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication13> "
				+ "<<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> <<http://www.Department1.University1.edu/AssociateProfessor23>");
		eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication14> "
				+ "<<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> <<http://www.Department1.University1.edu/AssociateProfessor24>");

		assertEquals(true, eb.isReady()); // The first RSPEvent

		HeavenInput event = eb.build();

		assertEquals(initSize, event.size());

		eb.append("<<http://www.Department1.University1.edu/AssociateProfessor2/Publication01> "
				+ "<<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor"
				+ "<<http://www.Department1.University1.edu/AssociateProfessor26>");
		eb.append("<<http://www.Department1.University1.edu/AssociateProfessor2/Publication113> "
				+ "<<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor>"
				+ "<<http://www.Department1.University1.edu/AssociateProfessor261>");
		eb.append("<<http://www.Department1.University1.edu/AssociateProfessor2/Publication112> "
				+ "<<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor>"
				+ "<<http://www.Department1.University1.edu/AssociateProfessor2452>");
		eb.append("<<http://www.Department1.University1.edu/AssociateProfessor2/Publication123> "
				+ "<<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor>"
				+ "<<http://www.Department1.University1.edu/AssociateProfessor233>");
		eb.append("<<http://www.Department1.University1.edu/AssociateProfessor2/Publication114> "
				+ "<<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor>"
				+ "<<http://www.Department1.University1.edu/AssociateProfessor2324>");

		assertEquals(true, eb.isReady()); // The first RSPEvent

		event = eb.build();

		assertEquals(initSize, event.size());

		eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication01> "
				+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor>  <http://www.Department1.University1.edu/AssociateProfessor26>");
		eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication113>"
				+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> <http://www.Department1.University1.edu/AssociateProfessor261>");
		eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication112>"
				+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> <http://www.Department1.University1.edu/AssociateProfessor2452>");
		eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication123>"
				+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> <http://www.Department1.University1.edu/AssociateProfessor233>");
		eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication114>"
				+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> <http://www.Department1.University1.edu/AssociateProfessor2324>");

		assertEquals(false, eb.isReady()); // The first RSPEvent

		eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication031> "
				+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> <http://www.Department1.University1.edu/AssociateProfessor234536");
		eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication1133> "
				+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> "
				+ "<http://www.Department1.University1.edu/AssociateProfessor25661> ");
		eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication1142>"
				+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor>"
				+ "<http://www.Department1.University1.edu/AssociateProfessor254452>");
		eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication15223> "
				+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> <http://www.Department1.University1.edu/AssociateProfessor2533>");
		eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication11344> "
				+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> "
				+ "<http://www.Department1.University1.edu/AssociateProfessor234524>");

		assertEquals(true, eb.isReady());
		event = eb.build();

		assertEquals(finalSize, event.size());

		eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication01> "
				+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> <http://www.Department1.University1.edu/AssociateProfessor26>");
		eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication113> "
				+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> <http://www.Department1.University1.edu/AssociateProfessor261>");
		eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication112> "
				+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> <http://www.Department1.University1.edu/AssociateProfessor2452>");
		eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication123> "
				+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> <http://www.Department1.University1.edu/AssociateProfessor233>");
		eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication114> "
				+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> <http://www.Department1.University1.edu/AssociateProfessor2324>");

		assertEquals(false, eb.isReady()); // The first RSPEvent

		eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication031> "
				+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> <http://www.Department1.University1.edu/AssociateProfessor234536");
		eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication1133> "
				+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> "
				+ "<http://www.Department1.University1.edu/AssociateProfessor25661> ");
		eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication1142>"
				+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor>"
				+ "<http://www.Department1.University1.edu/AssociateProfessor254452>");
		eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication15223> "
				+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> <http://www.Department1.University1.edu/AssociateProfessor2533>");
		eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication11344> "
				+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> "
				+ "<http://www.Department1.University1.edu/AssociateProfessor234524>");

		assertEquals(true, eb.isReady());
		event = eb.build();

		assertEquals(finalSize, event.size());

	}

	@Test
	public void customStepEventBuilderTestLong() {

		int finalSize = 15;
		int width = 5;
		int size = 5;
		HeavenInput event;
		FlowRateProfiler eb = new CustomStepFlowRateProfiler(parser, width, finalSize, size, 0, 100); // 555551515151515
		eb.setEncoder(new Encoder() {

			@Override
			public Stimulus[] encode(HeavenInput e) {
				return null;
			}
		});

		assertEquals(false, eb.isReady()); // The first RSPEvent
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < size; j++) {
				eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication14" + j + i + "> "
						+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> "
						+ "<http://www.Department1.University1.edu/AssociateProfessor24>");

			}
			assertEquals(true, eb.isReady()); // The first RSPEvent
			event = eb.build();
			assertEquals(size, event.size());
		}
		size = finalSize;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < size; j++) {
				eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication14" + j + i + ">"
						+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> "
						+ "<http://www.Department1.University1.edu/AssociateProfessor24" + i + j + ">");
			}
			assertEquals(true, eb.isReady()); // The first RSPEvent
			event = eb.build();
			assertEquals(size, event.size());
		}
	}

	@Test
	public void customStepEventBuilderTestDesc() {

		int finalSize = 5;
		int width = 5;
		int size = 15;
		HeavenInput event;
		FlowRateProfiler eb = new CustomStepFlowRateProfiler(parser, width, finalSize, size, 0, 100); // 555551515151515

		eb.setEncoder(new Encoder() {

			@Override
			public Stimulus[] encode(HeavenInput e) {
				return null;
			}
		});
		assertEquals(false, eb.isReady()); // The first RSPEvent
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < size; j++) {
				eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication14" + j + i + "> "
						+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor>"
						+ "<http://www.Department1.University1.edu/AssociateProfessor24> ");

			}
			assertEquals(true, eb.isReady()); // The first RSPEvent
			event = eb.build();
			assertEquals(size, event.size());
		}
		size = finalSize;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < size; j++) {
				eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication14" + j + i + "> "
						+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> "
						+ "<http://www.Department1.University1.edu/AssociateProfessor24" + i + j + ">");
			}
			assertEquals(true, eb.isReady()); // The first RSPEvent
			event = eb.build();
			assertEquals(size, event.size());
		}
	}
}
