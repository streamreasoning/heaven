package it.polimi.heaven.core.teststand.streamer.flowrateprofiler.profiles;

/**
 * @author Riccardo
 *         CONSTANT(0)
 *         LINEAR(1)
 *         STEP(2)
 *         EXP(3)
 *         RANDOM(4)
 *         STEP_FACTOR(5)
 *         CUSTOM_STEP(6);
 */
public enum FlowRateProfile {
	CONSTANT(0), LINEAR(1), STEP(2), EXP(3), RANDOM(4), STEP_FACTOR(5), CUSTOM_STEP(6);

	private int id;

	private FlowRateProfile(final int i) {
		id = i;
	}

	public static FlowRateProfile getById(int id) {
		for (FlowRateProfile e : FlowRateProfile.values()) {
			if (e.id == id) {
				return e;
			}
		}

		throw new IllegalArgumentException("Not found");
	}
}
