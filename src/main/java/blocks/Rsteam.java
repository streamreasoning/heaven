package blocks;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.ConfigurationMethodRef;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.time.CurrentTimeEvent;
import com.espertech.esper.core.service.EPServiceProviderSPI;
import commons.LoggingListener;

//description: i want to define when the rain starts and stop 
public class Rsteam {
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
		cepConfig.addMethodRef(Rsteam.class, ref);

		cepConfig.addEventType("MyEvent", MyEvent.class.getName());
		cepConfig.getEngineDefaults().getViewResources().setShareViews(false);
		cepConfig.getEngineDefaults().getThreading()
				.setInternalTimerEnabled(false);

		cep = (EPServiceProviderSPI) EPServiceProviderManager.getProvider(
				Rsteam.class.getName(), cepConfig);
		// We register an EPL statement
		cepAdm = cep.getEPAdministrator();
		cepRT = cep.getEPRuntime();

		cepRT.sendEvent(new CurrentTimeEvent(1000));

		EPStatement init = cepAdm
				.createEPL(
						"@Name('INITStmt') "
								+ "insert into INIT "
								+ "select  current_timestamp() as timestamp, count(*) as counter from MyEvent.win:time_batch(500 msec) "
						// + "output snapshot every 500 msec"
						, null, null); // snapshot
										// per
										// gli
										// zeri
		EPStatement a = cepAdm
				.createEPL(
						"@Name('AStmt') "
								+ "insert into A "
								+ "select current_timestamp as a_timestamp, blocks.Rsteam.factorial(counter) as a_res, timestamp as ts from INIT.win:time_batch(500 msec)",
						null, null);
		EPStatement b = cepAdm
				.createEPL(
						"@Name('BStmt') "
								+ "insert into B "
								+ "select current_timestamp as b_timestamp, counter-1 as b_res, timestamp as ts from INIT.win:time_batch(500 msec)",
						null, null);

		EPStatement res = cepAdm
				.createEPL(
						"@Name('RES') "
								+ "select current_timestamp, a.a_res=blocks.Rsteam.factorial(b.b_res+1) as result, b.b_timestamp, a.a_timestamp, a.ts, b.ts "
								+ "from A.win:time_batch(500 msec) as a, B.win:time_batch(500 msec) as b ",
						null, null);

		init.addListener(new LoggingListener(false, cepConfig,
				(EPServiceProviderSPI) cep, (String[]) null));
		res.addListener(new LoggingListener(false, cepConfig,
				(EPServiceProviderSPI) cep, (String[]) null));
		a.addListener(new LoggingListener(false, cepConfig,
				(EPServiceProviderSPI) cep, (String[]) null));
		b.addListener(new LoggingListener(false, cepConfig,
				(EPServiceProviderSPI) cep, (String[]) null));

		// after statements
		for (int i = 0; i < 15; i++) {
			cepRT.sendEvent(new MyEvent("MyEvent_" + cepRT.getCurrentTime(), i,
					cepRT.getCurrentTime()));
		}
		cepRT.sendEvent(new CurrentTimeEvent(1499));
		System.out.println("1499");
		cepRT.sendEvent(new CurrentTimeEvent(1500));
		System.out.println("1500");
		cepRT.sendEvent(new CurrentTimeEvent(1501));
		System.out.println("1501");
		cepRT.sendEvent(new CurrentTimeEvent(1502));
		System.out.println("1502");
		cepRT.sendEvent(new CurrentTimeEvent(1503));
		System.out.println("1503");
		cepRT.sendEvent(new CurrentTimeEvent(2000));
		System.out.println("2000");
		cepRT.sendEvent(new MyEvent("MyEvent_" + cepRT.getCurrentTime(), 20,
				cepRT.getCurrentTime()));
		cepRT.sendEvent(new CurrentTimeEvent(2500));
		System.out.println("2500");
		cepRT.sendEvent(new MyEvent("MyEvent_" + cepRT.getCurrentTime(), 20,
				cepRT.getCurrentTime()));
		cepRT.sendEvent(new CurrentTimeEvent(3000));
		cepRT.sendEvent(new CurrentTimeEvent(3500));
		while (true)
			;

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

	public static long factorial(long i) throws InterruptedException {
		long res = i;
		for (int j = 1; j < i; j++) {
			res *= i - j;
		}
		// Thread.sleep(10000);
		return Math.abs(res);

	}
}
