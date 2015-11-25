package it.polimi.heaven.core.tsimpl.streamer.rel2rdfstream.citybench;

import it.polimi.heaven.core.tsimpl.streamer.rel2rdfstream.citybench.ssnobservations.AarhusParkingObservation;
import it.polimi.heaven.core.tsimpl.streamer.rel2rdfstream.citybench.ssnobservations.AarhusTrafficObservation;
import it.polimi.heaven.core.tsimpl.streamer.rel2rdfstream.citybench.ssnobservations.PollutionObservation;
import it.polimi.heaven.core.tsimpl.streamer.rel2rdfstream.citybench.ssnobservations.SensorObservation;
import it.polimi.heaven.core.tsimpl.streamer.rel2rdfstream.citybench.ssnobservations.WeatherObservation;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EventFactory {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");

	public static SensorObservation createAarhusParkingObservation(Map<String, String> streamData) throws NumberFormatException, IOException,
			ParseException {
		int vehiclecount = Integer.parseInt(streamData.get("vehiclecount"));
		int totalspaces = Integer.parseInt(streamData.get("totalspaces"));
		String obId = "AarhusParkingObservation_" + Integer.parseInt(streamData.get("_id"));
		String garageCode = streamData.get("garagecode");
		Date obTimeStamp = sdf.parse(streamData.get("timestamp"));
		String streamID = streamData.get("streamID");
		return new AarhusParkingObservation(totalspaces, vehiclecount, garageCode, "", obTimeStamp, obId, streamID);
	}

	public static SensorObservation createAarhusTrafficObservation(Map<String, String> streamData) throws NumberFormatException, IOException,
			ParseException {

		String obId = "AarhusTrafficObservation_" + streamData.get("_id");
		int reportID = Integer.parseInt(streamData.get("REPORT_ID"));
		double avgSpeed = Double.parseDouble(streamData.get("avgSpeed"));
		double avgMeasuredTime = Double.parseDouble(streamData.get("avgMeasuredTime"));
		double medianMeasuredTime = Double.parseDouble(streamData.get("medianMeasuredTime"));
		int vehicleCount = Integer.parseInt(streamData.get("vehicleCount"));
		Date obTimeStamp = sdf.parse(streamData.get("TIMESTAMP"));
		String status = streamData.get("status");
		int extID = Integer.parseInt(streamData.get("extID"));
		String streamID = streamData.get("streamID");
		return new AarhusTrafficObservation(obId, streamID, obTimeStamp, status, avgMeasuredTime, avgSpeed, medianMeasuredTime, vehicleCount, extID,
				reportID);
	}

	public static SensorObservation createWeatherObservation(Map<String, String> streamData) throws NumberFormatException, IOException,
			ParseException {

		String obId = "WeatherObservation" + streamData.get("_id");
		Date obTimeStamp = sdf.parse(streamData.get("TIMESTAMP"));
		double humidity = Double.parseDouble(streamData.get("humidity"));
		double windSpeed = Double.parseDouble(streamData.get("windSpeed"));
		double temperature = Double.parseDouble(streamData.get("temperature"));
		String streamID = streamData.get("streamID");
		return new WeatherObservation(obId, streamID, obTimeStamp, humidity, windSpeed, temperature);
	}

	public static SensorObservation createPollutionObservation(Map<String, String> streamData) throws NumberFormatException, IOException,
			ParseException {

		String obId = "PollutionObservation" + (int) Math.random() * 10000;
		Date obTimeStamp = sdf.parse(streamData.get("timestamp"));
		double ozone = Double.parseDouble(streamData.get("ozone"));
		double particullate_matter = Double.parseDouble(streamData.get("particullate_matter"));
		double carbon_monoxide = Double.parseDouble(streamData.get("carbon_monoxide"));
		double sulfure_dioxide = Double.parseDouble(streamData.get("sulfure_dioxide"));
		double nitrogen_dioxide = Double.parseDouble(streamData.get("nitrogen_dioxide"));
		double lat = Double.parseDouble(streamData.get("lat"));
		double lon = Double.parseDouble(streamData.get("lon"));
		String streamID = streamData.get("streamID");
		return new PollutionObservation(obId, streamID, obTimeStamp, ozone, particullate_matter, carbon_monoxide, sulfure_dioxide, nitrogen_dioxide,
				lon, lat);

	}

	public static SensorObservation createObservation(String line) {

		Map<String, String> obsMap = new HashMap<String, String>();
		String[] fields = line.split(",");

		int fieldsLength = fields.length;
		String streamID = fields[fieldsLength - 3];
		String typeString = fields[fieldsLength - 2];
		String timestamp = fields[fieldsLength - 1];
		CityBenchStreamTypes type = CityBenchStreamTypes.valueOf(typeString);
		String[] headers = CityBenchUtils.getHeader(type);

		obsMap.put("streamID", streamID);
		obsMap.put("type", typeString);
		obsMap.put("timestamp", timestamp);

		for (int i = 0; i < headers.length && i < fieldsLength - 3; i++) {
			if (fields[i].equals("-"))
				continue;
			obsMap.put(headers[i], fields[i]);
		}

		try {
			switch (type) {
			case PARKING:
				return createAarhusParkingObservation(obsMap);
			case POLLUTION:
				return createPollutionObservation(obsMap);
			case TRAFFIC:
				return createAarhusTrafficObservation(obsMap);
			case WEATHER:
				return createWeatherObservation(obsMap);
			default:
				return null;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

	}
}
