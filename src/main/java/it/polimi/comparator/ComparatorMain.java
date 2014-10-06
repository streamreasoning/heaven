package it.polimi.comparator;

import it.polimi.comparator.core.CalibrationStand;
import it.polimi.comparator.engine.ComparatorImpl;
import it.polimi.comparator.engine.EngineComparator;

public class ComparatorMain {

	public static void main(String[] args) throws Exception {

		String[] comparing_files = new String[] { "jena/_Result_file1.trig",
				"plain/_Result_file1.trig" };
		String[] files = new String[] { "file1.txt" };

		CalibrationStand<EngineComparator> stand = new CalibrationStand<EngineComparator>(
				comparing_files, files, new ComparatorImpl(comparing_files));

		stand.turnOn();
		stand.run();
		stand.turnOff();

	}
}
