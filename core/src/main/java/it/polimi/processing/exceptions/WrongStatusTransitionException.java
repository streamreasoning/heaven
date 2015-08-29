package it.polimi.processing.exceptions;

public class WrongStatusTransitionException extends RuntimeException {

	private static final long serialVersionUID = -3801640653186329608L;

	public WrongStatusTransitionException(String string) {
		super(string);
	}

}
