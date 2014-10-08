package it.polimi.teststand.main;

import it.polimi.output.filesystem.FileManagerImpl;
import it.polimi.output.result.ResultCollector;
import it.polimi.output.sqllite.DatabaseManagerImpl;
import it.polimi.streamer.Streamer;
import it.polimi.teststand.core.TestStand;
import it.polimi.teststand.engine.RSPEngine;
import it.polimi.teststand.engine.esper.plain.PlainMultipleInheritance;
import it.polimi.teststand.events.TestExperimentResultEvent;
import it.polimi.teststand.events.TestResultEvent;
import it.polimi.teststand.output.ResultCollectorTestStandImpl;

import java.sql.SQLException;

public class TSModelPlain {

	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {

		String[] files = new String[] { "file1.txt" };

		ResultCollector<TestResultEvent, TestExperimentResultEvent> resultCollector = new ResultCollectorTestStandImpl(
				new FileManagerImpl(), new DatabaseManagerImpl());
		RSPEngine engine = new PlainMultipleInheritance(resultCollector);
		TestStand testStand = new TestStand(resultCollector, engine,
				new Streamer<RSPEngine>(engine));

		
		
		testStand.turnOn();
		try {
			for (String f : files) {
				testStand.run(f);
			}
		} catch (Exception e) {
			e.printStackTrace();
			testStand.stop();
		}
		testStand.turnOff();

	}

}
