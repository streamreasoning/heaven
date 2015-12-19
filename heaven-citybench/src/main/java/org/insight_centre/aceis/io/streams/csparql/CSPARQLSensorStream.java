package org.insight_centre.aceis.io.streams.csparql;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.insight_centre.aceis.observations.SensorObservation;

import com.hp.hpl.jena.rdf.model.Statement;

import eu.larkc.csparql.cep.api.RdfStream;

public abstract class CSPARQLSensorStream extends RdfStream implements Runnable {

	public CSPARQLSensorStream(String iri) {
		super(iri);

	}

	protected int sleep = 1000;
	protected boolean stop = false;
	protected SensorObservation currentObservation;
	protected List<String> requestedProperties = new ArrayList<String>();
	protected Double rate = 1.0;

	// private List<String> subscribers = new ArrayList<String>();

	public List<String> getRequestedProperties() {
		return requestedProperties;
	}

	public void setRequestedProperties(List<String> requestedProperties) {
		this.requestedProperties = requestedProperties;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public double getRate() {
		return rate;
	}

	public void setFreq(Double freq) {
		sleep = (int) (sleep / freq);
	}

	public void stop() {
		if (!stop) {
			stop = true;
		}
		// ACEISEngine.getSubscriptionManager().getStreamMap().remove(this.getURI());
		// SubscriptionManager.
	}

	protected abstract List<Statement> getStatements(SensorObservation so) throws NumberFormatException, IOException;

	protected abstract SensorObservation createObservation(Object data);

	public SensorObservation getCurrentObservation() {
		return this.currentObservation;
	}

	// public abstract void addSubscriber(String subscriber);

}
