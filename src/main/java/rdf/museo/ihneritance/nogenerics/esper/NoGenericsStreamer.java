package rdf.museo.ihneritance.nogenerics.esper;

import commons.Streamer;

import rdf.museo.ihneritance.generics.noconstrains.esper.events.RDFSInput;

public class NoGenericsStreamer {

	public static RDFSInput stream(){
		String[] triple = Streamer.getEvent();
		
		return new RDFSInput(null, null, null, 0);
	}
	
}
