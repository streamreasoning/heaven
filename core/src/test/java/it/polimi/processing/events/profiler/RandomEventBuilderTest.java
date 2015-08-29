package it.polimi.processing.events.profiler;

import it.polimi.processing.events.CTEvent;
import it.polimi.processing.events.TripleContainer;
import it.polimi.processing.events.profiler.ConstantRandomFlowRateProfiler;
import it.polimi.processing.events.profiler.RandomFlowRateProfiler;
import it.polimi.processing.events.profiler.abstracts.FlowRateProfiler;
import lombok.extern.log4j.Log4j;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

@Log4j
public class RandomEventBuilderTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void randomSizeTest() {

		int yMax = 30;
		int initSize = 5;

		CTEvent event;

		int experimentNumber = 0;
		FlowRateProfiler<CTEvent> eb = new RandomFlowRateProfiler(yMax, initSize, experimentNumber);

		int i = 0;
		while (!eb.isReady()) {
			eb.append(new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication" + i + "" + i,
					"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor",
					"http://www.Department1.University1.edu/AssociateProfessor2" + i }));
			i++;
		}
		event = eb.getEvent();
		log.info("Event [" + 0 + "] Size [" + event.size() + "]");

		for (int eventNumber = 1; eventNumber < 100; eventNumber++) {
			eb.append(new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication" + i + "" + i,
					"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor",
					"http://www.Department1.University1.edu/AssociateProfessor2" + i }));
			while (!eb.isReady()) {
				i++;
				eb.append(new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication" + i + "" + i,
						"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor",
						"http://www.Department1.University1.edu/AssociateProfessor2" + i }));

			}
			event = eb.getEvent();
			log.info("Event [" + eventNumber + "] Size [" + event.size() + "]");

		}

	}

	public void costantRandomSizeTest() {

		int yMax = 30;
		int xMax = 10;

		int initSize = 5;

		CTEvent event;

		int experimentNumber = 0;
		FlowRateProfiler<CTEvent> eb = new ConstantRandomFlowRateProfiler(xMax, yMax, initSize, experimentNumber);

		int i = 0;
		while (!eb.isReady()) {
			eb.append(new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication" + i + "" + i,
					"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor",
					"http://www.Department1.University1.edu/AssociateProfessor2" + i }));
			i++;
		}
		event = eb.getEvent();
		log.info("Event [" + 0 + "] Size [" + event.size() + "]");

		for (int eventNumber = 1; eventNumber < 1000; eventNumber++) {
			eb.append(new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication" + i + "" + i,
					"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor",
					"http://www.Department1.University1.edu/AssociateProfessor2" + i }));
			while (!eb.isReady()) {
				i++;
				eb.append(new TripleContainer(new String[] { "http://www.Department1.University1.edu/AssociateProfessor2/Publication" + i + "" + i,
						"http://swat.cse.lehigh.edu/onto/univ-bench.owl#publicationAuthor",
						"http://www.Department1.University1.edu/AssociateProfessor2" + i }));

			}
			event = eb.getEvent();
			log.info("Event [" + eventNumber + "] Size [" + event.size() + "]");

		}

	}

}
