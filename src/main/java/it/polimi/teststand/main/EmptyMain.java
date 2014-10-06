package it.polimi.teststand.main;

import it.polimi.teststand.core.TestStand;
import it.polimi.teststand.engine.identity.IdentityModel;

import java.sql.SQLException;

public class EmptyMain {

	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {

		String[] files = new String[] { "file1.txt", "file2.txt", "file3.txt" };

		TestStand<IdentityModel> testStand = new TestStand<IdentityModel>(files,
				new IdentityModel(null));

		testStand.turnOn();
		testStand.run();
		testStand.turnOff();
	}
}
