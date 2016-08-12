package it.polimi.heaven.citybench;

public enum CityBenchStreamTypes {
	POLLUTION("pollution"), PARKING("parking"), TRAFFIC("traffic"), USER_LOCATION("userlocation"), WEATHER("weather");

	private final String name;

	private CityBenchStreamTypes(String s) {
		name = s;
	}

	public static CityBenchStreamTypes getAssociatedType(String s) {
		CityBenchStreamTypes[] values = CityBenchStreamTypes.values();
		for (CityBenchStreamTypes cityBenchStreamTypes : values) {
			if (s.toLowerCase().contains(cityBenchStreamTypes.name)) {
				return cityBenchStreamTypes;
			}
		}
		return null;
	}

	public String toString() {
		return this.name;
	}

}
