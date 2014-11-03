package preprocessing;

import static org.junit.Assert.assertEquals;
import it.polimi.processing.EventProcessor;
import it.polimi.processing.events.StreamingEvent;
import it.polimi.processing.streamer.Parser;
import it.polimi.processing.teststand.streamer.TriGStreamer;
import it.polimi.utils.FileUtils;
import it.polimi.utils.TripleGraphTypes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

public class TrigStreamerTest {

	StreamingEvent e;

	@Test
	public void simpleTrigTest() {

		TriGStreamer<StreamingEvent> streamer = new TriGStreamer<StreamingEvent>(new EventProcessor<StreamingEvent>() {

			@Override
			public boolean sendEvent(StreamingEvent e) {

				String[] parseTriple1 = Parser
						.parseTriple("<http://www.Department0.University0.edu/UndergraduateStudent4> <http://swat.cse.lehigh.edu/onto/univ-bench.owl#advisor> <http://www.Department0.University0.edu/AssistantProfessor4> .");
				String[] parseTriple2 = Parser
						.parseTriple("<http://www.Department0.University0.edu/UndergraduateStudent4> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://swat.cse.lehigh.edu/onto/univ-bench.owl#UndergraduateStudent> .");
				String[] parseTriple3 = Parser
						.parseTriple("<http://www.Department0.University0.edu/AssistantProfessor4> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://swat.cse.lehigh.edu/onto/univ-bench.owl#AssistantProfessor> .");

				assertEquals(e.getId(), "<http://experiment.org/0>");

				return true;
			}

		});

		streamer.init();
		try {

			File file = new File(FileUtils.INPUT_FILE_PATH + "/test/teststreamer1.trig");
			BufferedReader br = new BufferedReader(new FileReader(file));

			streamer.stream(br, 0, "Test", TripleGraphTypes.UTRIPLES);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void twoGraphsTrigTest() {

		TriGStreamer<StreamingEvent> streamer = new TriGStreamer<StreamingEvent>(new EventProcessor<StreamingEvent>() {
			int numEvents = 0;

			@Override
			public boolean sendEvent(StreamingEvent e) {

				assertEquals(e.getId(), "<http://example.org/" + numEvents + ">");
				numEvents++;
				return true;
			}

		});

		streamer.init();
		try {

			File file = new File(FileUtils.INPUT_FILE_PATH + "/test/teststreamer2.trig");
			BufferedReader br = new BufferedReader(new FileReader(file));

			streamer.stream(br, 0, "Test", TripleGraphTypes.UTRIPLES);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
