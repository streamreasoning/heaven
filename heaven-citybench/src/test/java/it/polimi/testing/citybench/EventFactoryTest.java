package it.polimi.testing.citybench;

import static org.junit.Assert.assertEquals;
import it.polimi.heaven.core.tsimpl.streamer.rel2rdfstream.citybench.EventFactory;
import it.polimi.heaven.core.tsimpl.streamer.rel2rdfstream.citybench.ssnobservations.AarhusParkingObservation;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class EventFactoryTest {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");

	@Test
	public void createAarhusParkingObservationrTest() throws ParseException, NumberFormatException, IOException {

		Map<String, String> obsMap = new HashMap<String, String>();

		obsMap.put("name", "AarhusParkingData.stream");
		obsMap.put("type", "parking");
		obsMap.put("timestamp", "2014-11-03 16:18:44");
		obsMap.put("vehiclecount", "0");
		obsMap.put("updatetime", "2014-05-22 09:09:04.145");
		obsMap.put("totalspaces", "65");
		obsMap.put("garagecode", "NORREPORT");
		obsMap.put("_id", "1");
		obsMap.put("streamID", "");

		int vehiclecount = Integer.parseInt("0");
		int totalspaces = Integer.parseInt("65");
		String obId = "AarhusParkingObservation_" + Integer.parseInt("1");
		String garageCode = "NORREPORT";
		Date obTimeStamp = sdf.parse("2014-11-03 16:18:44");
		String streamID = "";

		AarhusParkingObservation aarhusParkingObservation = new AarhusParkingObservation(totalspaces, vehiclecount, garageCode, "", obTimeStamp,
				obId, streamID);
		assertEquals(aarhusParkingObservation, EventFactory.createAarhusParkingObservation(obsMap));
	}
}
