package it.polimi.export.processing.rspengine.windowed.esper.plain.events;

import it.polimi.processing.rspengine.rspevents.SerializedTripleEvent;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class TEvent extends SerializedTripleEvent {

	private final String channel;

	public TEvent(String s, String p, String o, long timestamp, long app_timestamp, String channel) {
		super(s, p, o, timestamp, app_timestamp);
		this.channel = channel;
	}

	@Override
	public String toString() {
		return "TEvent [s=" + s + ", p=" + p + ", o=" + o + "ts=" + timestamp + "app_ts=" + app_timestamp + "]";
	}

}