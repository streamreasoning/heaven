package it.polimi;


public interface Startable<E> {
	
	public E init();
	public E close();

}
