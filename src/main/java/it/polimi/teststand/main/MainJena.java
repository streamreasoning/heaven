package it.polimi.teststand.main;

import it.polimi.output.filesystem.FileManagerImpl;
import it.polimi.output.result.ResultCollector;
import it.polimi.output.sqllite.DatabaseManagerImpl;
import it.polimi.streamer.Streamer;
import it.polimi.teststand.core.TestStand;
import it.polimi.teststand.engine.RSPEngine;
import it.polimi.teststand.engine.jena.JenaEngine;
import it.polimi.teststand.events.TestExperimentResultEvent;
import it.polimi.teststand.events.TestResultEvent;
import it.polimi.teststand.output.ResultCollectorTestStandImpl;

import java.sql.SQLException;

public class MainJena {

	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {

		String[] files = new String[] { "file1.txt", "file2.txt", "file3.txt" };
		
		ResultCollector<TestResultEvent, TestExperimentResultEvent> resultCollector = new ResultCollectorTestStandImpl(
				new FileManagerImpl(), new DatabaseManagerImpl());
		
		JenaEngine engine = new JenaEngine(resultCollector);

		TestStand testStand = new TestStand(resultCollector, engine,
				new Streamer<RSPEngine>(engine));

		testStand.turnOn();
		try {
			for (String f : files) {
				testStand.run(f);
			}
		} catch (Exception e) {
			testStand.stop();
			e.printStackTrace();
		}
		testStand.turnOff();
	}
}
