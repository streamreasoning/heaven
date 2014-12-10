package it.polimi.processing.rspengine.windowed;

import it.polimi.processing.rspengine.windowed.esper.plain.events.TEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;

public class MySubscriber {

	List<TEvent> list = new ArrayList<TEvent>();
	EPStatement out;

	public MySubscriber(EPStatement out) {
		this.out = out;
	}

	public void update(TEvent e) {
		list.add(e);
	}

	public void updateEnd() {

		System.out.println("-----");

		for (TEvent e : list) {
			System.err.println(e);
		}
		list = new ArrayList<TEvent>();

		Iterator<EventBean> iterator = out.iterator();

		while (iterator.hasNext()) {
			System.out.println(iterator.next().getUnderlying());
		}

		System.out.println("-----");
	}
}
