package commons;

import org.apache.log4j.Logger;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.core.service.EPServiceProviderSPI;

public class LoggingListener implements UpdateListener {

	Configuration config;
	EPServiceProviderSPI spi;
	boolean debug, log, red;
	String[] proStrings = null;
	String name;

	public LoggingListener(boolean debug, Configuration config,
			EPServiceProviderSPI spi, String... properties) {
		this.log = false;
		this.red = false;
		this.name = "";
		this.config = config;
		this.spi = spi;
		this.debug = debug;
		if (properties != null)
			this.proStrings = properties;
	}

	public LoggingListener(String name, boolean debug, boolean log,
			boolean red, Configuration config, EPServiceProviderSPI spi,
			String... properties) {
		this.log = log;
		this.red = red;
		this.name = name;
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
					print(name + " " + e.get(s));
				}
			} else {
				print(name + " " + e.getUnderlying());
			}

		}
	}

	private void print(String msg) {
		if (log)
			Logger.getRootLogger().info(msg);
		if (red)
			System.err.println(msg);
		else
			System.out.println(msg);

	}
}
