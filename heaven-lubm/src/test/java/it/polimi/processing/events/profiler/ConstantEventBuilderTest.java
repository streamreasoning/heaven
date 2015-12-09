package it.polimi.processing.events.profiler;

import static org.junit.Assert.assertEquals;
import it.polimi.heaven.core.teststand.events.HeavenInput;
import it.polimi.heaven.core.teststand.rspengine.events.Stimulus;
import it.polimi.heaven.core.teststand.streamer.Encoder;
import it.polimi.heaven.core.teststand.streamer.flowrateprofiler.profiles.ConstantFlowRateProfiler;
import it.polimi.heaven.core.teststand.streamer.lubm.LUBMFlowRateProfiler;
import it.polimi.heaven.core.teststand.streamer.lubm.LUBMParser;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ConstantEventBuilderTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();
	private final LUBMParser parser = new LUBMParser();

	@Test
	public void constantEventBuilderTest() {

		LUBMFlowRateProfiler eb = new ConstantFlowRateProfiler(parser, 1, 0, 100);

		eb.setEncoder(new Encoder() {

			@Override
			public Stimulus[] encode(HeavenInput e) {
				return null;
			}
		});

		String tc1 = "<http://www.Department1.University1.edu/AssociateProfessor2/Publication9> "
				+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> "
				+ "<http://www.Department1.University1.edu/AssociateProfessor2>";

		String tc2 = "<http://www.Department1.University1.edu/AssociateProfessor2/Publication8> "
				+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> "
				+ "<http://www.Department1.University1.edu/AssociateProfessor2>";

		assertEquals(false, eb.isReady()); // The first RSPEvent

		eb.append(tc1);

		assertEquals(true, eb.isReady());

		HeavenInput event = eb.build();

		assertEquals(1, event.getLines().size());

		assertEquals(true, eb.isReady());

		eb.append(tc1);
		eb.append(tc2);

		assertEquals(true, eb.isReady());

		event = eb.build();
		assertEquals(1, event.getLines().size());

	}

	@Test
	public void constantEventBuilderRealCaseTest() {
		LUBMFlowRateProfiler eb = new ConstantFlowRateProfiler(parser, 1, 0, 100);

		eb.setEncoder(new Encoder() {

			@Override
			public Stimulus[] encode(HeavenInput e) {
				return null;
			}
		});

		assertEquals(false, eb.isReady());

		for (int i = 0; i < 10; i++) {

			eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication" + i + ">"
					+ " <http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> "
					+ "<http://www.Department1.University1.edu/AssociateProfessor2>");
			assertEquals(1, eb.build().size());
			assertEquals(true, eb.isReady());

		}

	}

	@Test
	public void constantEventBuilderTestSingleAdd() {
		LUBMFlowRateProfiler eb = new ConstantFlowRateProfiler(parser, 1, 0, 100);
		eb.setEncoder(new Encoder() {

			@Override
			public Stimulus[] encode(HeavenInput e) {
				return null;
			}
		});
		String tc1 = "<http://www.Department1.University1.edu/AssociateProfessor2/Publication9> "
				+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> "
				+ "<http://www.Department1.University1.edu/AssociateProfessor2>";

		String tc2 = "<http://www.Department1.University1.edu/AssociateProfessor2/Publication8> "
				+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> <http://www.Department1.University1.edu/AssociateProfessor2>";

		assertEquals(false, eb.isReady()); // The first RSPEvent

		eb.append(tc1);

		assertEquals(true, eb.isReady());

		HeavenInput event = eb.build();

		assertEquals(1, event.getLines().size());

		assertEquals(true, eb.isReady());

		eb.append(tc2);

		assertEquals(true, eb.isReady());

		event = eb.build();
		assertEquals(1, event.getLines().size());

	}

	@Test
	public void constantEventBuilderRealCaseTestSingleAdd() {
		LUBMFlowRateProfiler eb = new ConstantFlowRateProfiler(parser, 1, 0, 100);
		eb.setEncoder(new Encoder() {

			@Override
			public Stimulus[] encode(HeavenInput e) {
				return null;
			}
		});
		assertEquals(false, eb.isReady());

		for (int i = 0; i < 10; i++) {

			eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication" + i + "> "
					+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> "
					+ "<http://www.Department1.University1.edu/AssociateProfessor2>");
			assertEquals(1, eb.build().size());
			assertEquals(true, eb.isReady());

		}

	}

}
