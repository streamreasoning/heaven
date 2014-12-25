package it.polimi.processing.collector.saver.data;

public interface CollectableData {

	public String getData();

	public CollectableData append(String c);

	public boolean save(String where);
}
