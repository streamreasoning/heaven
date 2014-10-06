package it.polimi.teststand.main;

import it.polimi.teststand.core.TestStand;
import it.polimi.teststand.engine.jena.JenaEngine;

import java.sql.SQLException;

public class MainJena {

	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {

		String[] files = new String[] { "file1.txt", "file2.txt", "file3.txt" };

		TestStand<JenaEngine> testStand = new TestStand<JenaEngine>(files,
				new JenaEngine(null));

		testStand.turnOn();
		testStand.run();
		testStand.turnOff();
	}
}
