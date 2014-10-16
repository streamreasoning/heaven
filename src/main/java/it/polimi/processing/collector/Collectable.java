package it.polimi.processing.collector;

import java.util.Set;

//TODO it must be splitted in proprerly cases

/**
 * 
 * this interface reporst all methods used to translate an event into the
 * corresponding saver format
 * 
 * @author Riccardo
 * 
 */
public interface Collectable {

	public String getName();

	public Set<String[]> getTriples();

	public String getTrig();

	public String getCSV();

	public String getSQL();

	byte[] getBytes();

}
