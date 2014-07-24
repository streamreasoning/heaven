package rdf.museo.inheritanceOnEvents.activeEvents.esper.events;

public class WrapperEvent<T> {

	protected T e;

	public WrapperEvent(T e) {
		this.e = e;
	}

	public T getE() {
		return e;
	}

	public void setE(T e) {
		this.e = e;
	}

	@Override
	public String toString() {
		return "WrapperEvent [e=" + e + "]";
	}

}
