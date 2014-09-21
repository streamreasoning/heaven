package commons;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.ConfigurationMethodRef;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.time.CurrentTimeEvent;

public abstract class EsperModel {

	protected static Configuration cepConfig;
	protected static ConsoleAppender appender;
	protected static EPServiceProvider cep;
	protected static EPRuntime cepRT;
	protected static EPAdministrator cepAdm;
	protected static ConfigurationMethodRef ref;
	protected static int time = 0;
	protected static Boolean BUSY = false;

	public abstract void sendEvent(String[] eventTriple);

	protected abstract void initQueries();

	public boolean isBusy() {
		return BUSY;
	}

	protected void moveTime() {
		cepRT.sendEvent(new CurrentTimeEvent(time));
		time += 1000;
	}

	protected static void initLogger() {
		PatternLayout sl = new PatternLayout(
				"%d{HH:mm:ss.SS} - %t-%x-%-5p-%-10c:%m%n");
		appender = new ConsoleAppender(sl);
		Logger.getRootLogger().addAppender(appender);
		Logger.getRootLogger().setLevel((Level) Level.INFO);
	}
}
