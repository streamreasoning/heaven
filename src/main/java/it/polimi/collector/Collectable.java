package it.polimi.collector;

import java.util.Set;

public interface Collectable {

	public String getName();

	public Set<String[]> getTriples();

	public String getTrig();

	public String getCSV();

	public String getSQL();

	byte[] getBytes();

}
