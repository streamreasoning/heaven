package it.polimi.processing.enums;

public enum BuildingStrategy {
	CONSTANT(0), LINEAR(1), STEP(2), EXP(3);

	private int id;

	private BuildingStrategy(final int i) {
		id = i;
	}

	public static BuildingStrategy getById(int id) {
		for (BuildingStrategy e : BuildingStrategy.values()) {
			if (e.id == id) {
				return e;
			}
		}

		throw new IllegalArgumentException("Not found");
	}
}
