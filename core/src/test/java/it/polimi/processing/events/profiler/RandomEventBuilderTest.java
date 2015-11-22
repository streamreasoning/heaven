package it.polimi.processing.events.profiler;

import it.polimi.heaven.core.ts.events.Stimulus;
import it.polimi.heaven.core.ts.events.TripleContainer;
import it.polimi.heaven.core.ts.streamer.flowrateprofiler.FlowRateProfiler;
import it.polimi.heaven.core.tsimpl.streamer.rdf2rdfstream.flowrateprofiler.ConstantRandomFlowRateProfiler;
import it.polimi.heaven.core.tsimpl.streamer.rdf2rdfstream.flowrateprofiler.RandomFlowRateProfiler;
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

		Stimulus event;

		int experimentNumber = 0;
		FlowRateProfiler<Stimulus> eb = new RandomFlowRateProfiler(yMax, initSize, experimentNumber);

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

		Stimulus event;

		int experimentNumber = 0;
		FlowRateProfiler<Stimulus> eb = new ConstantRandomFlowRateProfiler(xMax, yMax, initSize, experimentNumber);

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
