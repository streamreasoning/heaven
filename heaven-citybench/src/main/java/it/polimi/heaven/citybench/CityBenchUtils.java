package it.polimi.heaven.citybench;

public class CityBenchUtils {
	public static final String defaultPrefix = "http://www.insight-centre.org/dataset/SampleEventService#",
			ssnPrefix = "http://purl.oclc.org/NET/ssnx/ssn#", owlsPrefix = "http://www.daml.org/services/owl-s/1.2/Service.owl#",
			owlsspPrefix = "http://www.daml.org/services/owl-s/1.2/ServiceParameter.owl#",
			owlsscPrefix = "http://www.daml.org/services/owl-s/1.2/ServiceCategory.owl#", emvoPrefix = "http://sense.deri.ie/vocab/emvo#",
			muoPrefix = "http://purl.oclc.org/NET/muo/ucum/", ctPrefix = "http://www.insight-centre.org/citytraffic#",
			drPrefix = "http://www.insight-centre.org/datarequest#", qPrefix = "http://www.ict-citypulse.eu/ontologies/streamQoI/Quality",
			saoPrefix = "http://purl.oclc.org/NET/sao/", upPrefix = "http://www.ict-citypulse.eu/ontologies/userprofile#",
			cesPrefix = "http://www.insight-centre.org/ces#", osmPrefix = "http://www.insight-centre.org/ontologies/osm#";

	public static final String ssnObservedProperty = ssnPrefix + "observedProperty";
	public static final String saoHasValue = saoPrefix + "hasValue";

	public static final String[] parkingHeader = new String[] { "vehiclecount", "updatetime", "_id", "totalspaces", "garagecode", "streamtime" };
	public static final String[] trafficHeader = new String[] { "status", "avgMeasuredTime", "avgSpeed", "extID", "medianMeasuredTime", "TIMESTAMP",
			"vehicleCount", "_id", "REPORT_ID" };
	public static final String[] userLocationHeader = new String[] {};
	public static final String[] weatherHeader = new String[] { "hum", "tempm", "wspdm", "TIMESTAMP" };
	public static final String[] pollutionHeader = new String[] { "ozone", "particullate_matter", "carbon_monoxide", "sulfure_dioxide",
			"nitrogen_dioxide", "longitude", "latitude", "timestamp" };

	public static String[] getHeader(CityBenchStreamTypes type) {
		switch (type) {
		case PARKING:
			return parkingHeader;
		case POLLUTION:
			return pollutionHeader;
		case TRAFFIC:
			return trafficHeader;
		case WEATHER:
			return weatherHeader;
		default:
			return userLocationHeader;
		}
	}
}
