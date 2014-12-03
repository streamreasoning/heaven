package it.polimi.processing.workbench.core;

public interface Startable<E> {

	public E init();

	public E close();

}
