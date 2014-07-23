package rdf.museo.ihneritance.generics.noconstrains.esper;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import rdf.museo.ihneritance.generics.noconstrains.esper.events.RDFS3;
import rdf.museo.ihneritance.generics.noconstrains.esper.events.RDFS9;
import rdf.museo.ihneritance.generics.noconstrains.esper.events.RDFSInput;
import rdf.museo.ihneritance.generics.noconstrains.esper.events.RDFSOut;
import rdf.museo.ihneritance.generics.noconstrains.ontology.properties.Creates;
import rdf.museo.ihneritance.generics.noconstrains.ontology.properties.Paints;
import rdf.museo.ihneritance.generics.noconstrains.ontology.properties.Sculpts;
import rdf.museo.ihneritance.generics.ontology.Artist;
import rdf.museo.ihneritance.generics.ontology.Paint;
import rdf.museo.ihneritance.generics.ontology.Painter;
import rdf.museo.ihneritance.generics.ontology.Sculptor;
import rdf.museo.ihneritance.generics.ontology.TypeOf;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.ConfigurationMethodRef;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.time.CurrentTimeEvent;
import com.espertech.esper.core.service.EPServiceProviderSPI;

import commons.LoggingListener;

/**
 * In this example rdfs property of subclass of is exploited by external static
 * functions which can be called form EPL No data or time windows are considered
 * in event consuming, se the related example for that time is externally
 * controlled all event are sent in the samte time interval
 * 
 * the query doesn't include joins
 * 
 * events are pushed, on incoming events, in 3 differents queue which are pulled
 * by refering statements
 * 
 * 
 * 
 * 
 * **/

public class NoPropertyConstrains {
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
		cepConfig.addMethodRef(NoPropertyConstrains.class, ref);

		// eventi in classi diverse perche' altrimenti non vengono distinti a
		// livello di ELP, indagare
		cepConfig.addEventType("RDFSInput", RDFSInput.class.getName());
		cepConfig.addEventType("RDFS3Input", RDFS3.class.getName());
		cepConfig.addEventType("RDFS9Input", RDFS9.class.getName());
		cepConfig.addEventType("QueryOut", RDFSOut.class.getName());

		Creates creates = new Creates();
		Sculpts sculpts = new Sculpts();
		Paints paints = new Paints();
		TypeOf typeof = new TypeOf();
		cepConfig.addVariable("typeof", TypeOf.class, typeof, true);
		cepConfig.addVariable("creates", Creates.class, creates, true);
		cepConfig.addVariable("sculpts", Sculpts.class, sculpts, true);
		cepConfig.addVariable("paints", Paints.class, paints, true);

		cepConfig.addVariable("artist", Class.class, Artist.class, true);
		cepConfig.addVariable("sculptor", Class.class, Sculptor.class, true);
		cepConfig.addVariable("painter", Class.class, Painter.class, true);

		cepConfig.getEngineDefaults().getViewResources().setShareViews(false);
		cepConfig.getEngineDefaults().getThreading()
				.setInternalTimerEnabled(false);

		cep = (EPServiceProviderSPI) EPServiceProviderManager.getProvider(
				NoPropertyConstrains.class.getName(), cepConfig);
		// We register an EPL statement
		cepAdm = cep.getEPAdministrator();
		cepRT = cep.getEPRuntime();

		cepRT.sendEvent(new CurrentTimeEvent(0));

		String input = "on RDFSInput "
				+ "insert into RDFS3Input select s as s, o as o, p as p, channel as channel "
				+ "insert into RDFS9Input select s as s, o as o, p as p, channel as channel "
				+ "insert into QueryOut select s as s, o as o, p as p, channel as channel "
				+ "output all ";

		String rdfs3 = "on RDFS3Input "
				+ "insert into QueryOut select o as s, typeof as p, p.range as o, channel || 'RDSF3' as channel "
				+ "insert into RDFS9Input select o as s,  typeof as p, p.range as o, channel || 'RDSF3' as channel "
				+ "insert into QueryOut select s as s, typeof as p, p.domain as o, channel || 'RDSF3' as channel "
				+ "insert into RDFS9Input select s as s, typeof as p, p.domain as o, channel || 'RDSF3' as channel "
				+ "output all";

		String rdfs9 = "on RDFS9Input(p=typeof) "
				+ "insert into QueryOut select s as s, p, o.super as o , channel || 'RDSF9' as channel ";
		String queryOut = "insert into OutEvent "
				+ "select current_timestamp, * from QueryOut.win:time_batch(1000 msec)";
		String queryOut1 = "insert into OutEvent "
				+ "select * from QueryOut(instanceof(s, rdf.museo.ihneritance.generics.ontology.Painter) ).win:time_batch(1000 msec)";

		cepAdm.createEPL(input);
		cepAdm.createEPL(rdfs3);
		cepAdm.createEPL(rdfs9);
		cepAdm.createEPL(queryOut).addListener(
				new LoggingListener("", false, false, false, cepConfig,
						(EPServiceProviderSPI) cep, (String[]) null));

		// after statements
		// esempio 1
		cepRT.sendEvent(new RDFSInput(new Painter("Leonardo"), paints,
				new Paint("Gioconda"), cepRT.getCurrentTime()));

		// esempio 2
		/*
		 * cepRT.sendEvent(new RDFSInput(new Painter("Leonardo"), typeof, new
		 * Painter("Gioconda"), cepRT.getCurrentTime()));
		 */

		cepRT.sendEvent(new CurrentTimeEvent(1000));
	}
}
