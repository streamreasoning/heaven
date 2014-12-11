package processing.timestrategy;

import it.polimi.processing.workbench.core.RSPTestStand;
import it.polimi.processing.workbench.timecontrol.AggregationStrategy;
import it.polimi.processing.workbench.timecontrol.TimeStrategy;

public class AggregationStrategyTest {

	public void simpleTest() {

		TimeStrategy strategy = new AggregationStrategy(5);

		RSPTestStand teststand = new RSPTestStand(null);
	}
}
