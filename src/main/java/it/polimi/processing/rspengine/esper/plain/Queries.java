package it.polimi.processing.rspengine.esper.plain;

public class Queries {

	private static final String EXTERNAL = "it.polimi.processing.rspengine.esper.plain.Ontology";

	public static final String input_rdfs6 = "on TEvent "
			+ "insert into RDFS3Input select s as s, o as o, p as p, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel "
			+ "insert into RDFS9Input select s as s, o as o, p as p, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel "
			+ "insert into QueryOut select s as s, o as o, p as p, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel "
			+ "insert into RDFS3Input select s as s, o as o, as p, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel "
			+ EXTERNAL + ".subPropertyOf(p) as p, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel "
			+ "insert into RDFS9Input select s as s, o as o," + EXTERNAL
			+ ".subPropertyOf(p) as p, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel "
			+ "insert into QueryOut select s as s, o as o," + EXTERNAL
			+ ".subPropertyOf(p) as p, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel " + "output all ";

	public static final String input = "on TEvent as e "
			+ "insert into RDFS3Input select e.ss as s, e.os as o, e.ps as p, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel where not "
			+ EXTERNAL
			+ ".containsType(e.ps) "
			+ "insert into RDFS6Input select e.ss as s, e.os as o, e.ps as p, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel where not "
			+ EXTERNAL
			+ ".containsType(e.ps) "
			+ "insert into RDFS9Input select e.ss as s, e.os as o, e.ps as p, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel where  "
			+ EXTERNAL
			+ ".containsType(e.ps) "
			+ "insert into QueryOut   select e.ss as s, e.os as o, e.ps as p, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel "
			+ "output all ";

	public static final String rdfs3 = "on RDFS3Input as e "

	+ "insert into QueryOut select o as s, " + EXTERNAL + ".type()" + " as p, " + EXTERNAL
			+ ".range(p) as o, timestamp as timestamp , app_timestamp as app_timestamp, channel || 'RDSF3' as channel where not " + EXTERNAL
			+ ".containsType(e.p) "

			+ "insert into RDFS9Input select o as s, " + EXTERNAL + ".type()" + " as p, " + EXTERNAL
			+ ".range(p) as o, timestamp as timestamp, app_timestamp as app_timestamp , channel || 'RDSF3' as channel where not " + EXTERNAL
			+ ".containsType(e.p) "

			+ "insert into QueryOut select s as s, " + EXTERNAL + ".type()" + " as p, " + EXTERNAL
			+ ".domain(p) as o, timestamp as timestamp , app_timestamp as app_timestamp, channel || 'RDSF3' as channel where not " + EXTERNAL
			+ ".containsType(e.p) "

			+ "insert into RDFS9Input select s as s, " + EXTERNAL + ".type()" + " as p, " + EXTERNAL
			+ ".domain(p) as o, timestamp as timestamp , app_timestamp as app_timestamp, channel || 'RDSF3' as channel where not " + EXTERNAL
			+ ".containsType(e.p) "

			+ "output all";

	public static final String rdfs6 = "on RDFS6Input as e "

	+ "insert into QueryOut select s as s, o as o, " + EXTERNAL
			+ ".subPropertyOf(p) as p, timestamp as timestamp, app_timestamp as app_timestamp, channel || 'RDSF6' as channel "

			+ "insert into RDFS9Input select s as s, o as o, " + EXTERNAL
			+ ".subPropertyOf(p) as p, timestamp as timestamp, app_timestamp as app_timestamp, channel || 'RDSF6' as channel "

			+ "insert into RDFS3Input select s as s, o as o, " + EXTERNAL
			+ ".subPropertyOf(p) as p, timestamp as timestamp, app_timestamp as app_timestamp, channel || 'RDSF6' as channel where not " + EXTERNAL
			+ ".containsType(e.p) "

			+ "output all";

	public static final String rdfs9 = "on RDFS9Input as e "

	+ "insert into QueryOut select s as s, p, " + EXTERNAL
			+ ".subClassOf(o)  as o, timestamp as timestamp, app_timestamp as app_timestamp , channel || 'RDSF9' as channel where  " + EXTERNAL
			+ ".containsType(e.p) ";

	public static final String queryOut = "insert into Out "
			+ "select  s as s, p as p, o as o, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel from QueryOut.win:time_batch(1000 msec) ";

}
