package it.polimi.heaven.citybench.encoders;

import it.polimi.heaven.baselines.jena.events.stimuli.GraphStimulus;
import it.polimi.heaven.baselines.jena.events.stimuli.StatementStimulus;
import it.polimi.heaven.citybench.ssnobservations.SensorObservation;
import it.polimi.heaven.core.teststand.data.Line;
import it.polimi.heaven.core.teststand.events.HeavenInput;
import it.polimi.heaven.core.teststand.rspengine.events.Stimulus;
import it.polimi.heaven.core.teststand.streamer.Encoder;

import java.util.HashMap;
import java.util.Map;

import com.hp.hpl.jena.rdf.model.Model;

public class CB2GraphStimulusEncoder implements Encoder {

	private Stimulus[] stimulis;
	private Map<String, Model> streams_models;
	private SensorObservation so;
	private String streamID;

	@Override
	public Stimulus[] encode(HeavenInput e) {
		streams_models = new HashMap<String, Model>();
		for (Line tc : e.getLines()) {
			so = (SensorObservation) tc;
			streamID = so.getStreamID();
			if (streams_models.containsKey(streamID)) {
				streams_models.put(streamID, streams_models.get(streamID).add(so.toModel()));
			} else {
				streams_models.put(streamID, so.toModel());

			}

		}

		stimulis = new StatementStimulus[streams_models.size()];
		int i = 0;

		for (String streams : streams_models.keySet()) {
			stimulis[i] = new GraphStimulus(e.getStimuli_application_timestamp(), System.currentTimeMillis(), streams_models.get(streams).getGraph(),
					streams);
		}

		return stimulis;
	}
}
