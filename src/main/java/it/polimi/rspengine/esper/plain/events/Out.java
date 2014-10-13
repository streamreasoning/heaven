package it.polimi.rspengine.esper.plain.events;

import it.polimi.collector.Collectable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Out implements Collectable {
	private String[] o, s;
	private String p, channel;
	private long timestamp, app_timestamp;

	public Out() {

	}

	public Out(String[] s, String p, String[] o, String ch, long timestamp,
			long app_timestamp) {
		this.s = s;
		this.p = p;
		this.o = o;
		this.channel = ch;
		this.app_timestamp = app_timestamp;
		this.timestamp = timestamp;
	}

	public String[] getS() {
		return s;
	}

	public void setS(String[] s) {
		this.s = s;
	}

	public String getP() {
		return p;
	}

	public void setP(String p) {
		this.p = p;
	}

	public String[] getO() {
		return o;
	}

	public void setO(String[] c) {
		this.o = c;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	@Override
	public String toString() {
		return "Out [o=" + Arrays.toString(o) + ", s=" + Arrays.toString(s)
				+ ", p=" + p + ", timestamp=" + timestamp + ", app_timestamp="
				+ app_timestamp + ", channel=" + channel + "]";
	}


	@Override
	public Set<String[]> getTriples() {
		Set<String[]> triples = new HashSet<String[]>();
		List<String> list;

		for (String subj : s) {
			list = new ArrayList<String>();
			for (String obj : o) {
				list.add(subj + "," + p + "," + obj);
			}

			for (String string : list) {
				triples.add(string.split(","));
			}
		}

		return triples;
	}

	public long getApp_timestamp() {
		return app_timestamp;
	}

	public void setApp_timestamp(long app_timestamp) {
		this.app_timestamp = app_timestamp;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTrig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCSV() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSQL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getBytes() {
		// TODO Auto-generated method stub
		return null;
	}

}