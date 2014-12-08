package processing.timestrategy;

import it.polimi.main.strategies.AggregationStrategy;
import it.polimi.processing.workbench.core.RSPTestStand;
import it.polimi.processing.workbench.core.TimeStrategy;

public class AggregationStrategyTest {

	public void simpleTest() {

		TimeStrategy strategy = new AggregationStrategy(5);

		RSPTestStand teststand = new RSPTestStand(null);
	}
}
