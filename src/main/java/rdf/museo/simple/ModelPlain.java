package rdf.museo.simple;

import commons.Streamer;


public class ModelPlain {

	public static void main(String[] args) throws Exception {
		
		//TODO faccio partire lo streamer
		//TODO faccio partire il modello
		while(true){
			Streamer.stream(new PlainMultipleInheritance());
		}

	}

}
