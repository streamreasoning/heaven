package it.polimi.export.processing.rspengine.windowed.esper.plain.utils;

import it.polimi.processing.rspengine.jena.WindowUtils;
import it.polimi.processing.services.system.GetPropertyValues;

public class Queries {

	private static final String EXTERNAL = "it.polimi.processing.rspengine.esper.plain." + GetPropertyValues.getProperty("ontology_class");
	public static final long window = WindowUtils.omega;

	public static final String INPUT = "insert into InputEvent select * from  TEvent";

	public static final String INPUT_TE = "on InputEvent as e "
			+ "insert into RDFS23Input select e.ss as s, e.os as o, e.ps as p, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel where not "
			+ EXTERNAL
			+ ".containsType(e.ps) "
			+ "insert into RDFS6Input select e.ss as s, e.os as o, e.ps as p, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel where not "
			+ EXTERNAL
			+ ".containsType(e.ps) "
			+ "insert into RDFS9Input select e.ss as s, e.os as o, e.ps as p, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel where  "
			+ EXTERNAL
			+ ".containsType(e.ps) "
			+ "insert into QueryOut select e.ss as s, e.os as o, e.ps as p, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel "
			+ "output all ";

	public static final String RDFS23 = "on RDFS23Input as e "

	+ "insert into QueryOut select o as s, " + EXTERNAL + ".type()" + " as p, " + EXTERNAL
			+ ".range(p) as o, timestamp as timestamp , app_timestamp as app_timestamp, channel || 'RDSF23' as channel where not " + EXTERNAL
			+ ".containsType(e.p) "

			+ "insert into RDFS9Input select o as s, " + EXTERNAL + ".type()" + " as p, " + EXTERNAL
			+ ".range(p) as o, timestamp as timestamp, app_timestamp as app_timestamp , channel || 'RDSF23' as channel where not " + EXTERNAL
			+ ".containsType(e.p) "

			+ "insert into QueryOut select s as s, " + EXTERNAL + ".type()" + " as p, " + EXTERNAL
			+ ".domain(p) as o, timestamp as timestamp , app_timestamp as app_timestamp, channel || 'RDSF23' as channel where not " + EXTERNAL
			+ ".containsType(e.p) "

			+ "insert into RDFS9Input select s as s, " + EXTERNAL + ".type()" + " as p, " + EXTERNAL
			+ ".domain(p) as o, timestamp as timestamp , app_timestamp as app_timestamp, channel || 'RDSF23' as channel where not " + EXTERNAL
			+ ".containsType(e.p) "

			+ "output all";

	public static final String RDFS6 = "on RDFS6Input as e "

	+ "insert into QueryOut select s as s, o as o, " + EXTERNAL
			+ ".subPropertyOf(p) as p, timestamp as timestamp, app_timestamp as app_timestamp, channel || 'RDSF6' as channel "

			+ "insert into RDFS9Input select s as s, o as o, " + EXTERNAL
			+ ".subPropertyOf(p) as p, timestamp as timestamp, app_timestamp as app_timestamp, channel || 'RDSF6' as channel "

			+ "insert into RDFS3Input select s as s, o as o, " + EXTERNAL
			+ ".subPropertyOf(p) as p, timestamp as timestamp, app_timestamp as app_timestamp, channel || 'RDSF6' as channel where not " + EXTERNAL
			+ ".containsType(e.p) "

			+ "output all";

	public static final String RDFS9 = "on RDFS9Input as e "

	+ "insert into QueryOut select s as s, p, " + EXTERNAL
			+ ".subClassOf(o)  as o, timestamp as timestamp, app_timestamp as app_timestamp , channel || 'RDSF9' as channel where  " + EXTERNAL
			+ ".containsType(e.p) ";

	public static final String QUERYOUT = "insert into Out "
			+ "select  s as s, p as p, o as o, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel from QueryOut.win:time_batch(1000 msec) ";

	// REVERSE

	// TODO verificare se la distinct lavora anche sullo stream in cui inserisco per evitare
	// duplicati
	public static final String OUTPUTREV1 = "insert into QueryOut select e.ss as s, e.ps as p, e.os as o, timestamp as timestamp, app_timestamp as app_timestamp, channel || 'OUT' as channel  from TEvent.win:time(1000 msec) as e ";
	public static final String OUTPUTREV2 = "insert into QueryOut select * from RDFS3Input ";
	public static final String OUTPUTREV3 = "insert into QueryOut select * from RDFS6Input ";
	public static final String OUTPUTREV4 = "insert into QueryOut select * from RDFS9Input ";

	public static final String RDFS9REV1 = " insert into RDFS9Input select e.ss as s, e.ps as p, "
			+ EXTERNAL
			+ ".subClassOf(e.os)  as o, timestamp as timestamp, app_timestamp as app_timestamp , channel || 'RDSF9' as channel from TEvent as e where "
			+ EXTERNAL + ".containsType(e.ps) ";
	public static final String RDFS9REV2 = "insert into RDFS9Input select  s as s, p as p, " + EXTERNAL
			+ ".subClassOf(o)  as o, timestamp as timestamp, app_timestamp as app_timestamp , channel || 'RDSF9' as channel from RDFS6Input where  "
			+ EXTERNAL + ".containsType(p)  ";
	public static final String RDFS9REV3 = "insert into RDFS9Input select  s as s, p as p, " + EXTERNAL
			+ ".subClassOf(o)  as o, timestamp as timestamp, app_timestamp as app_timestamp , channel || 'RDSF9' as channel from RDFS3Input where  "
			+ EXTERNAL + ".containsType(p)  ";

	public static final String RDFS6REV = " insert into RDFS6Input select e.ss as s, e.os as o, "
			+ EXTERNAL
			+ ".subPropertyOf(e.ps) as p, timestamp as timestamp, app_timestamp as app_timestamp, channel || 'RDSF6' as channel from TEvent as e where not "
			+ EXTERNAL + ".containsType(e.ps)  ";

	public static final String RDFS23REV1 = "insert into RDFS3Input select e.os as s, " + EXTERNAL + ".type()" + " as p, " + EXTERNAL
			+ ".range(e.ps) as o, timestamp as timestamp , app_timestamp as app_timestamp, channel || 'RDSF23' as channel "
			+ "from TEvent as e where not " + EXTERNAL + ".containsType(e.ps) ";
	public static final String RDFS23REV2 = "insert into RDFS3Input select e.os as s, " + EXTERNAL + ".type()" + " as p, " + EXTERNAL
			+ ".domain(e.ps) as o, timestamp as timestamp , app_timestamp as app_timestamp, channel || 'RDSF23' as channel "
			+ "from TEvent as e where not " + EXTERNAL + ".containsType(e.ps) ";
	public static final String RDFS23REV3 = "insert into RDFS3Input select o as s, " + EXTERNAL + ".type()" + " as p, " + EXTERNAL
			+ ".range(p) as o, timestamp as timestamp , app_timestamp as app_timestamp, channel || 'RDSF23' as channel "
			+ "from RDFS6Input where not " + EXTERNAL + ".containsType(p) ";
	public static final String RDFS23REV4 = "insert into RDFS3Input select o as s, " + EXTERNAL + ".type()" + " as p, " + EXTERNAL
			+ ".domain(p) as o, timestamp as timestamp , app_timestamp as app_timestamp, channel || 'RDSF23' as channel "
			+ "from RDFS6Input where not " + EXTERNAL + ".containsType(p) ";

}
