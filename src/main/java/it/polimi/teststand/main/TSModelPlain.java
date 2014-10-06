package it.polimi.teststand.main;

import it.polimi.teststand.core.TestStand;
import it.polimi.teststand.engine.esper.plain.PlainMultipleInheritance;

public class TSModelPlain {

	public static void main(String[] args) throws Exception {

		
		String[] files = new String[] { "file1.txt" };

		TestStand<PlainMultipleInheritance> testStand = new TestStand<PlainMultipleInheritance>(
				files, new PlainMultipleInheritance());

		testStand.turnOn();
		testStand.run();
		testStand.turnOff();

	}

}
