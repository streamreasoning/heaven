package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics;

public class Queries {

	private static final String EXTERNAL = "it.polimi.processing.rspengine.esper.plain.Ontology";

	public static final String INPUT_RDFS6 = "on TEvent "
			+ "insert into RDFS3Input select s as s, o as o, p as p, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel "
			+ "insert into RDFS9Input select s as s, o as o, p as p, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel "
			+ "insert into QueryOut select s as s, o as o, p as p, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel "
			+ "insert into RDFS3Input select s as s, o as o, as p, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel "
			+ EXTERNAL + ".subPropertyOf(p) as p, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel "
			+ "insert into RDFS9Input select s as s, o as o," + EXTERNAL
			+ ".subPropertyOf(p) as p, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel "
			+ "insert into QueryOut select s as s, o as o," + EXTERNAL
			+ ".subPropertyOf(p) as p, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel " + "output all ";

	public static final String INPUT = "on TEvent as e "
			+ "insert into RDFS3Input select e.ss as s, e.os as o, e.ps as p, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel "
			+ "insert into RDFS6Input select e.ss as s, e.os as o, e.ps as p, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel "
			+ "insert into RDFS9Input select e.ss as s, e.os as o, e.ps as p, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel "
			+ "insert into QueryOut   select e.ss as s, e.os as o, e.ps as p, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel "
			+ "output all ";

	public static final String RDFS23 = "on RDFS3Input "

	+ "insert into QueryOut select o as s, " + EXTERNAL + ".type()" + " as p, " + EXTERNAL
			+ ".range(p) as o, timestamp as timestamp , app_timestamp as app_timestamp, channel || 'RDSF3' as channel "

			+ "insert into RDFS9Input select o as s, " + EXTERNAL + ".type()" + " as p, " + EXTERNAL
			+ ".range(p) as o, timestamp as timestamp, app_timestamp as app_timestamp , channel || 'RDSF3' as channel "

			+ "insert into QueryOut select s as s, " + EXTERNAL + ".type()" + " as p, " + EXTERNAL
			+ ".domain(p) as o, timestamp as timestamp , app_timestamp as app_timestamp, channel || 'RDSF3' as channel "

			+ "insert into RDFS9Input select s as s, " + EXTERNAL + ".type()" + " as p, " + EXTERNAL
			+ ".domain(p) as o, timestamp as timestamp , app_timestamp as app_timestamp, channel || 'RDSF3' as channel " + "output all";

	public static final String RDFS6 = "on RDFS6Input "

	+ "insert into QueryOut select s as s, o as o, " + EXTERNAL
			+ ".subPropertyOf(p) as p, timestamp as timestamp, app_timestamp as app_timestamp, channel || 'RDSF6' as channel "

			+ "insert into RDFS3Input select s as s, o as o, " + EXTERNAL
			+ ".subPropertyOf(p) as p, timestamp as timestamp, app_timestamp as app_timestamp, channel || 'RDSF6' as channel " + "output all";

	public static final String RDFS9 = "on RDFS9Input   "

	+ "insert into QueryOut select s as s, p, " + EXTERNAL
			+ ".subClassOf(o)  as o, timestamp as timestamp, app_timestamp as app_timestamp , channel || 'RDSF9' as channel ";

	public static final String queryOut = "insert into Out "
			+ "select  s as s, p as p, o as o, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel from QueryOut.win:time_batch(1000 msec) ";

	public static final String INPUT_NOGENERICS = "on RDFSInput " + "insert into RDFS3Input select s as s, o as o, p as p, channel as channel "
			+ "insert into RDFS9Input select s as s, o as o, p as p, channel as channel "
			+ "insert into QueryOut select s as s, o as o, p as p, channel as channel " + "output all ";

	public static final String RDFS3_NOGENERICS = "on RDFS3Input(p!=typeOf) "
			+ "insert into QueryOut select o as s, typeOf as p, p.range as o, channel || 'RDSF3' as channel "
			+ "insert into RDFS9Input select o as s, typeOf as p, p.range as o, channel || 'RDSF3' as channel "
			+ "insert into QueryOut select s as s, typeOf as p, p.domain as o, channel || 'RDSF3' as channel "
			+ "insert into RDFS9Input select s as s, typeOf as p, p.domain as o, channel || 'RDSF3' as channel " + "output all";

	public static final String RDFS9_NOGENERICS = "on RDFS9Input(p=typeOf) "
			+ "insert into QueryOut select s as s, p, o.super as o , channel || 'RDSF9' as channel where p=typeOf "
			+ "insert into QueryOut select s as s, p, o as o , channel || 'RDSF9' as channel where p!=typeOf output all";
	public static final String queryOut_nogenerics = "insert into OutEvent " + "select current_timestamp, * from QueryOut.win:time_batch(1000 msec)";
	public static final String queryOut1_nogenerics = "insert into OutEvent "
			+ "select * from QueryOut(instanceof(s, rdf.museo.ihneritance.nogenerics.RDFSUtils.Person) ).win:time_batch(1000 msec)";

}