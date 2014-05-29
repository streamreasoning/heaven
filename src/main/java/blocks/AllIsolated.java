package blocks;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.ConfigurationMethodRef;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPAdministratorIsolated;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPRuntimeIsolated;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderIsolated;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.time.CurrentTimeEvent;
import com.espertech.esper.core.service.EPServiceProviderSPI;
import commons.LoggingListener;

//description: i want to define when the rain starts and stop 
public class AllIsolated {
	protected static Configuration cepConfig;
	protected static ConsoleAppender appender;
	protected static EPServiceProvider cep;
	protected static EPRuntime cepRT;
	protected static EPAdministrator cepAdm;

	public static void main(String argv[]) throws InterruptedException {

		PatternLayout sl = new PatternLayout(
				"%d{HH:mm:ss.SS} - %t-%x-%-5p-%-10c:%m%n");
		appender = new ConsoleAppender(sl);
		Logger.getRootLogger().addAppender(appender);
		Logger.getRootLogger().setLevel((Level) Level.INFO);

		ConfigurationMethodRef ref = new ConfigurationMethodRef();
		cepConfig = new Configuration();
		cepConfig.addMethodRef(AllIsolated.class, ref);

		cepConfig.addEventType("MyEvent", MyEvent.class.getName());
		cepConfig.getEngineDefaults().getViewResources().setShareViews(false);
		cepConfig.getEngineDefaults().getThreading()
				.setInternalTimerEnabled(false);

		cep = (EPServiceProviderSPI) EPServiceProviderManager.getProvider(
				AllIsolated.class.getName(), cepConfig);
		// We register an EPL statement
		cepAdm = cep.getEPAdministrator();
		cepRT = cep.getEPRuntime();
		EPServiceProviderIsolated isolatedService = cep
				.getEPServiceIsolated("Isolator");
		EPRuntimeIsolated isoRuntime = isolatedService.getEPRuntime();
		EPAdministratorIsolated isolatedAdmin = isolatedService
				.getEPAdministrator();
		cepRT.sendEvent(new CurrentTimeEvent(0));

		isoRuntime.sendEvent(new CurrentTimeEvent(0));
		EPStatement init = isolatedAdmin
				.createEPL(
						"insert into INIT "
								+ "select distinct count(*) as counter, current_timestamp as timestamp, e.timestamp as init_timestamp "
								+ "from MyEvent.win:time_batch(1 sec) as e",
						null, null);
		init.addListener(new LoggingListener(false, cepConfig,
				(EPServiceProviderSPI) cep, (String[]) null));
		EPStatement isolated_A = isolatedAdmin.createEPL("insert into A "
				+ "select blocks.AllIsolated.factorial(e.counter) as a_res, "
				+ "current_timestamp as timestamp, "
				+ "e.timestamp as init_current_timestamp, "
				+ "e.init_timestamp as init_event_timestamp "
				+ "from INIT.win:length(1) as e ", null, null);
		isolated_A.addListener(new LoggingListener(false, cepConfig,
				(EPServiceProviderSPI) cep, (String[]) null));
		EPStatement isolated_B = isolatedAdmin.createEPL("insert into B "
				+ "select counter-1 as b_res,"
				+ "current_timestamp as timestamp, "
				+ "e.timestamp as init_current_timestamp, "
				+ "e.init_timestamp as init_event_timestamp "
				+ "from INIT.win:length(1) as e ", null, null);
		isolated_B.addListener(new LoggingListener(false, cepConfig,
				(EPServiceProviderSPI) cep, (String[]) null));
		EPStatement res = isolatedAdmin
				.createEPL(
						"insert into RES "
								+ "select a_res=blocks.AllIsolated.factorial(b_res+1) as res, current_timestamp as timestamp, "
								+ "a.timestamp, b.timestamp, a.init_current_timestamp,b.init_current_timestamp, "
								+ "a.init_event_timestamp,"
								+ "b.init_event_timestamp "
								+ "from A.win:length(1) as a, B.win:length(1) as b ",
						null, null);
		res.addListener(new LoggingListener(false, cepConfig,
				(EPServiceProviderSPI) cep, (String[]) null));

		// after statements

		for (int i = 0; i < 15; i++) {
			isoRuntime.sendEvent(new MyEvent("MyEvent_"
					+ cepRT.getCurrentTime(), i, cepRT.getCurrentTime()));
		}

		isolatedService.getEPRuntime().sendEvent(new CurrentTimeEvent(2000));
		cepRT.sendEvent(new CurrentTimeEvent(2000));

		for (int i = 0; i < 20; i++) {
			isoRuntime.sendEvent(new MyEvent("MyEvent_"
					+ cepRT.getCurrentTime(), i, cepRT.getCurrentTime()));
		}
		isolatedService.getEPRuntime().sendEvent(new CurrentTimeEvent(3000));
		cepRT.sendEvent(new CurrentTimeEvent(3000));
	}

	public static class MyEvent {
		String name;
		int nextParam;
		long timestamp;

		public MyEvent(String name, int nextParam, long timestamp) {
			super();
			this.name = name;
			this.nextParam = nextParam;
			this.timestamp = timestamp;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getNextParam() {
			return nextParam;
		}

		public void setNextParam(int nextParam) {
			this.nextParam = nextParam;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}

		@Override
		public String toString() {
			return "MyEvent [name=" + name + ", nextParam=" + nextParam
					+ ", timestamp=" + timestamp + "]";
		}

	}

	public static long factorial(long i) {
		long res = i;
		for (int j = 1; j < i; j++) {
			res *= i - j;
		}
		return res;
	}
}
