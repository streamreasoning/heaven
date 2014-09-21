package rdf.museo.ihneritance.nogenerics;

import rdf.museo.ihneritance.nogenerics.esper.NoGenericsPropertySubclass;
import commons.Streamer;


public class ModelNoGenerics {

	public static void main(String[] args) throws Exception {
		
		//TODO faccio partire lo streamer
		//TODO faccio partire il modello
		while(true){
			Streamer.stream(new NoGenericsPropertySubclass());
		}

	}

}
