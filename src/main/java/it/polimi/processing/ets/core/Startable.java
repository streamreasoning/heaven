package it.polimi.processing.ets.core;

public interface Startable<E> {

	public E init();

	public E close();

}
