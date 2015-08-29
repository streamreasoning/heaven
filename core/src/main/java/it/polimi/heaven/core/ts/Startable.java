package it.polimi.heaven.core.ts;

/**
 * 
 * @author Riccardo
 *         Interface that uniforms the initialization and finalization of the classes that
 *         implements
 *         it.
 * @param <E>
 *            Define the return type for those methods
 */
public interface Startable<E> {

	public E init();

	public E close();

}
