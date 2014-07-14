package rdf.museo.ihneritance.interfaces.esper;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import rdf.museo.ihneritance.nogenerics.esper.events.RDFS3;
import rdf.museo.ihneritance.nogenerics.esper.events.RDFS9;
import rdf.museo.ihneritance.nogenerics.esper.events.RDFSInput;
import rdf.museo.ihneritance.nogenerics.esper.events.RDFSOut;
import rdf.museo.ihneritance.nogenerics.ontology.Artist;
import rdf.museo.ihneritance.nogenerics.ontology.Paint;
import rdf.museo.ihneritance.nogenerics.ontology.Painter;
import rdf.museo.ihneritance.nogenerics.ontology.Piece;
import rdf.museo.ihneritance.nogenerics.ontology.Sculpt;
import rdf.museo.ihneritance.nogenerics.ontology.Sculptor;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFClass;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFProperty;
import rdf.museo.ihneritance.nogenerics.rdfs.RDFResource;

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

public class Interfaces {
	protected static Configuration config;
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
		config = new Configuration();
		config.addMethodRef(Interfaces.class, ref);

		// eventi in classi diverse perche' altrimenti non vengono distinti a
		// livello di ELP, indagare
		config.addEventType("RDFSInput", RDFSInput.class.getName());
		config.addEventType("RDFS3Input", RDFS3.class.getName());
		config.addEventType("RDFS9Input", RDFS9.class.getName());
		config.addEventType("QueryOut", RDFSOut.class.getName());

		RDFProperty creates = new RDFProperty(Artist.class, Piece.class,
				"creates");
		RDFProperty sculpts = new RDFProperty(Sculptor.class, Sculpt.class,
				"sculpts");
		RDFProperty paints = new RDFProperty(Painter.class, Paint.class,
				"paints");
		RDFProperty typeof = new RDFProperty(RDFResource.class, RDFClass.class,
				"typeof");

		config.addVariable("typeof", RDFProperty.class, typeof, true);
		config.addVariable("creates", RDFProperty.class, creates, true);
		config.addVariable("sculpts", RDFProperty.class, sculpts, true);
		config.addVariable("paints", RDFProperty.class, paints, true);

		config.getEngineDefaults().getViewResources().setShareViews(false);
		config.getEngineDefaults().getThreading()
				.setInternalTimerEnabled(false);

		cep = (EPServiceProviderSPI) EPServiceProviderManager.getProvider(
				Interfaces.class.getName(), config);
		// We register an EPL statement
		cepAdm = cep.getEPAdministrator();
		cepRT = cep.getEPRuntime();

		cepRT.sendEvent(new CurrentTimeEvent(0));

		String input = "on RDFSInput "
				+ "insert into RDFS3Input select s as s, o as o, p as p, channel as channel "
				+ "insert into RDFS9Input select s as s, o as o, p as p, channel as channel "
				+ "insert into QueryOut select s as s, o as o, p as p, channel as channel "
				+ "output all ";

		String rdfs3 = "on RDFS3Input(p!=typeof) "
				+ "insert into QueryOut select o as s, typeof as p, p.range as o, channel || 'RDSF3' as channel "
				+ "insert into RDFS9Input select o as s,  typeof as p, p.range as o, channel || 'RDSF3' as channel "
				+ "insert into QueryOut select s as s, typeof as p, p.domain as o, channel || 'RDSF3' as channel "
				+ "insert into RDFS9Input select s as s, typeof as p, p.domain as o, channel || 'RDSF3' as channel "
				+ "output all";

		String rdfs9 = "on RDFS9Input(p=typeof) "
				+ "insert into QueryOut select s as s, p, o.super as o , channel || 'RDSF9' as channel where p=typeof "
				+ "insert into QueryOut select s as s, p, o as o , channel || 'RDSF9' as channel where p!=typeof "
				+ "output all";
		String queryOut = "insert into OutEvent "
				+ "select current_timestamp, * from QueryOut.win:time_batch(1000 msec)";
		String queryOut1 = "insert into OutEvent "
				+ "select * from QueryOut(instanceof(s, rdf.museo.ihneritance.nogenerics.ontology.Artist) ).win:time_batch(1000 msec)";

		cepAdm.createEPL(input);
		cepAdm.createEPL(rdfs3);
		cepAdm.createEPL(rdfs9);
		cepAdm.createEPL(queryOut).addListener(
				new LoggingListener("", false, false, false, config,
						(EPServiceProviderSPI) cep, (String[]) null));

		// after statements
		// esempio 1

		/*
		 * cepRT.sendEvent(new RDFSInput(new RDFClass(Painter.class), typeof,
		 * new RDFClass(RDFClass.class), cepRT.getCurrentTime()));
		 */

		cepRT.sendEvent(new RDFSInput(new Painter("Leonardo"), paints,
				new Paint("Gioconda"), cepRT.getCurrentTime()));

		// esempio 2
		/*
		 * cepRT.sendEvent(new RDFSInput(new Artist("Leonardo"), typeof, new
		 * Painter(""), cepRT.getCurrentTime()));
		 */
		cepRT.sendEvent(new CurrentTimeEvent(1000));
	}
}
