package it.polimi.processing.enums;

public enum EventBuilderMode {
	CONSTANT(0), LINEAR(1), STEP(2), EXP(3), RANDOM(4);

	private int id;

	private EventBuilderMode(final int i) {
		id = i;
	}

	public static EventBuilderMode getById(int id) {
		for (EventBuilderMode e : EventBuilderMode.values()) {
			if (e.id == id) {
				return e;
			}
		}

		throw new IllegalArgumentException("Not found");
	}
}
