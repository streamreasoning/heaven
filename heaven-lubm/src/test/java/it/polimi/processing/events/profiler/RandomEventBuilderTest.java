package it.polimi.processing.events.profiler;

import it.polimi.heaven.core.teststand.events.HeavenInput;
import it.polimi.heaven.core.teststand.rspengine.events.Stimulus;
import it.polimi.heaven.core.teststand.streamer.Encoder;
import it.polimi.heaven.core.teststand.streamer.flowrateprofiler.FlowRateProfiler;
import it.polimi.heaven.core.teststand.streamer.flowrateprofiler.profiles.ConstantRandomFlowRateProfiler;
import it.polimi.heaven.core.teststand.streamer.flowrateprofiler.profiles.RandomFlowRateProfiler;
import it.polimi.heaven.core.teststand.streamer.lubm.LUBMParser;
import lombok.extern.log4j.Log4j;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

@Log4j
public class RandomEventBuilderTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();
	private final LUBMParser parser = new LUBMParser();

	@Test
	public void randomSizeTest() {

		int yMax = 30;
		int initSize = 5;

		HeavenInput event;

		int experimentNumber = 0;
		FlowRateProfiler eb = new RandomFlowRateProfiler(parser, yMax, initSize, experimentNumber, 100);
		eb.setEncoder(new Encoder() {

			@Override
			public Stimulus[] encode(HeavenInput e) {
				return null;
			}
		});
		int i = 0;
		while (!eb.isReady()) {
			eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication" + i + "" + i + "> "
					+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> "
					+ "<http://www.Department1.University1.edu/AssociateProfessor2" + i + ">");
			i++;
		}
		event = eb.build();
		log.info("Event [" + 0 + "] Size [" + event.size() + "]");

		for (int eventNumber = 1; eventNumber < 100; eventNumber++) {
			eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication" + i + "" + i + "> "
					+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> "
					+ "<http://www.Department1.University1.edu/AssociateProfessor2" + i + ">");
			while (!eb.isReady()) {
				i++;
				eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication" + i + "" + i + "> "
						+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> "
						+ "<http://www.Department1.University1.edu/AssociateProfessor2" + i + ">");

			}
			event = eb.build();
			log.info("Event [" + eventNumber + "] Size [" + event.size() + "]");

		}

	}

	public void costantRandomSizeTest() {

		int yMax = 30;
		int xMax = 10;

		int initSize = 5;

		HeavenInput event;

		int experimentNumber = 0;
		FlowRateProfiler eb = new ConstantRandomFlowRateProfiler(parser, xMax, yMax, initSize, experimentNumber, 100);
		eb.setEncoder(new Encoder() {

			@Override
			public Stimulus[] encode(HeavenInput e) {
				return null;
			}
		});
		int i = 0;
		while (!eb.isReady()) {
			eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication" + i + "" + i + "> "
					+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> "
					+ "<http://www.Department1.University1.edu/AssociateProfessor2" + i + ">");
			i++;
		}
		event = eb.build();
		log.info("Event [" + 0 + "] Size [" + event.size() + "]");

		for (int eventNumber = 1; eventNumber < 1000; eventNumber++) {
			eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication" + i + "" + i + "> "
					+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> "
					+ "<http://www.Department1.University1.edu/AssociateProfessor2" + i + ">");
			while (!eb.isReady()) {
				i++;
				eb.append("<http://www.Department1.University1.edu/AssociateProfessor2/Publication" + i + "" + i + "> "
						+ "<http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor> "
						+ "<http://www.Department1.University1.edu/AssociateProfessor2" + i + ">");

			}
			event = eb.build();
			log.info("Event [" + eventNumber + "] Size [" + event.size() + "]");

		}

	}
}
