package it.polimi.processing;

public interface Startable<E> {

	public E init();

	public E close();

}
