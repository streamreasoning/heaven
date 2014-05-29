package commons;

import org.apache.log4j.Logger;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.core.service.EPServiceProviderSPI;

public class LoggingListener implements UpdateListener {

	Configuration config;
	EPServiceProviderSPI spi;
	boolean debug;
	String[] proStrings = null;

	public LoggingListener(boolean debug, Configuration config,
			EPServiceProviderSPI spi, String... properties) {
		this.config = config;
		this.spi = spi;
		this.debug = debug;
		if (properties != null)
			this.proStrings = properties;
	}

	public void update(EventBean[] arg0, EventBean[] arg1) {
		// Logger.getRootLogger().info(arg0.length);
		for (EventBean e : arg0) {
			if (debug) {
				Logger.getRootLogger().info(
						config.getEngineDefaults().getThreading()
								.isInternalTimerEnabled());
				Logger.getRootLogger().info(
						spi.getSchedulingService().getTime());//
			}
			if (proStrings != null) {
				for (String s : proStrings) {
					System.out.println(e.get(s));
				}
			} else {
				System.out.println(e.getUnderlying());
			}

		}
		System.out.println("--");
	}
}
