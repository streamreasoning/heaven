package it.polimi.processing.collector.saver.data;

public interface CollectableData {

	public String getData();

	public CollectableData append(String c);
}
