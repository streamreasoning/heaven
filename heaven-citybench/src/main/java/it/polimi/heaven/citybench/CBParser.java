package it.polimi.heaven.citybench;

import it.polimi.heaven.citybench.ssnobservations.AarhusParkingObservation;
import it.polimi.heaven.citybench.ssnobservations.AarhusTrafficObservation;
import it.polimi.heaven.citybench.ssnobservations.PollutionObservation;
import it.polimi.heaven.citybench.ssnobservations.SensorObservation;
import it.polimi.heaven.citybench.ssnobservations.WeatherObservation;
import it.polimi.heaven.core.teststand.streamer.ParsingTemplate;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

import org.insight_centre.aceis.eventmodel.EventDeclaration;
import org.insight_centre.aceis.eventmodel.TrafficReportService;

@Log4j
@AllArgsConstructor
public class CBParser implements ParsingTemplate {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
	private Map<String, EventDeclaration> event_declarations;

	public SensorObservation createAarhusParkingObservation(Map<String, String> streamData) throws NumberFormatException, IOException, ParseException {
		int vehiclecount = Integer.parseInt(streamData.get("vehiclecount"));
		int totalspaces = Integer.parseInt(streamData.get("totalspaces"));
		String obId = "AarhusParkingObservation_" + Integer.parseInt(streamData.get("_id"));
		String garageCode = streamData.get("garagecode");
		Date obTimeStamp = sdf.parse(streamData.get("timestamp"));
		String streamID = streamData.get("streamID");

		EventDeclaration ed = event_declarations.get(streamID);
		String payload = ed.getPayloads().get(0).split("\\|")[2];
		return new AarhusParkingObservation(totalspaces, vehiclecount, garageCode, payload, obTimeStamp.getTime(), obId, streamID, ed.getServiceId());
	}

	public SensorObservation createAarhusTrafficObservation(Map<String, String> streamData) throws NumberFormatException, IOException, ParseException {

		String obId = "AarhusTrafficObservation_" + streamData.get("_id");
		int reportID = streamData.containsKey("REPORT_ID") ? Integer.parseInt(streamData.get("REPORT_ID")) : -1;
		double avgSpeed = Double.parseDouble(streamData.get("avgSpeed"));
		double avgMeasuredTime = Double.parseDouble(streamData.get("avgMeasuredTime"));
		double medianMeasuredTime = Double.parseDouble(streamData.get("medianMeasuredTime"));
		int vehicleCount = Integer.parseInt(streamData.get("vehicleCount"));
		Date obTimeStamp = sdf.parse(streamData.get("timestamp"));
		String status = streamData.get("status");
		int extID = Integer.parseInt(streamData.get("extID"));
		String streamID = streamData.get("streamID");

		EventDeclaration ed = event_declarations.get(streamID);
		TrafficReportService trafficReportService = (TrafficReportService) ed;
		double distance = Double.parseDouble(trafficReportService.getDistance() + "");
		List<String> payloads = ed.getPayloads();
		String payload = payloads.get(0).split("\\|")[2];
		double congestion_level = distance != 0 ? (vehicleCount / distance) : -1D;
		double estimatedTime = avgSpeed != 0 ? (distance / avgSpeed) : -1D;
		return new AarhusTrafficObservation(obId, streamID, obTimeStamp.getTime(), status, avgMeasuredTime, avgSpeed, medianMeasuredTime,
				vehicleCount, extID, reportID, congestion_level, estimatedTime, payloads, ed.getServiceId());
	}

	public SensorObservation createWeatherObservation(Map<String, String> streamData) throws NumberFormatException, IOException, ParseException {

		String obId = "WeatherObservation" + streamData.get("_id");
		Date obTimeStamp = sdf.parse(streamData.get("TIMESTAMP"));
		double humidity = Double.parseDouble(streamData.get("humidity"));
		double windSpeed = Double.parseDouble(streamData.get("windSpeed"));
		double temperature = Double.parseDouble(streamData.get("temperature"));
		String streamID = streamData.get("streamID");
		EventDeclaration ed = event_declarations.get(streamID);
		String payload = ed.getPayloads().get(0).split("\\|")[2];
		return new WeatherObservation(obId, streamID, obTimeStamp.getTime(), humidity, windSpeed, temperature, payload, ed.getServiceId());
	}

	public SensorObservation createPollutionObservation(Map<String, String> streamData) throws NumberFormatException, IOException, ParseException {

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
		EventDeclaration ed = event_declarations.get(streamID);
		String payload = ed.getPayloads().get(0).split("\\|")[2];
		return new PollutionObservation(obId, streamID, obTimeStamp.getTime(), ozone, particullate_matter, carbon_monoxide, sulfure_dioxide,
				nitrogen_dioxide, lon, lat, payload, ed.getServiceId());

	}

	public SensorObservation parse(String line) {

		Map<String, String> obsMap = new HashMap<String, String>();
		String[] fields = line.split(",");
		int fieldsLength = fields.length;
		String streamID = fields[fieldsLength - 3];
		String typeString = fields[fieldsLength - 2];
		String timestamp = fields[fieldsLength - 1];
		CityBenchStreamTypes type = CityBenchStreamTypes.valueOf(typeString.toUpperCase());
		String[] headers = CityBenchUtils.getHeader(type);

		obsMap.put("streamID", streamID.split("\\/")[1].split("\\.")[0]);
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
