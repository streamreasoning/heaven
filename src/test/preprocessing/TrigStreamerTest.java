package preprocessing;

import static org.junit.Assert.assertEquals;
import it.polimi.processing.EventProcessor;
import it.polimi.processing.events.TestStandEvent;
import it.polimi.processing.teststand.streamer.TriGStreamer;
import it.polimi.utils.FileUtils;
import it.polimi.utils.TripleGraphTypes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

public class TrigStreamerTest {

	TestStandEvent e;

	@Test
	public void simpleTrigTest() {

		TriGStreamer<TestStandEvent> streamer = new TriGStreamer<TestStandEvent>(new EventProcessor<TestStandEvent>() {

			@Override
			public boolean sendEvent(TestStandEvent e) {

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

		TriGStreamer<TestStandEvent> streamer = new TriGStreamer<TestStandEvent>(new EventProcessor<TestStandEvent>() {
			int numEvents = 0;

			@Override
			public boolean sendEvent(TestStandEvent e) {

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
