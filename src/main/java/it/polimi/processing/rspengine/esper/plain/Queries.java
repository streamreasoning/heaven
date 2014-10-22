package it.polimi.processing.rspengine.esper.plain;

import it.polimi.utils.RDFSUtils;

public class Queries {

	public static final String input = "on TEvent "
			+ "insert into RDFS3Input select s as s, o as o, p as p, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel "
			+ "insert into RDFS9Input select s as s, o as o, p as p, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel "
			+ "insert into QueryOut select s as s, o as o, p as p, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel "
			+ "output all ";

	public static final String rdfs3 = "on RDFS3Input(p!='"
			+ RDFSUtils.TYPE_PROPERTY
			+ "') "
			+ "insert into QueryOut select o as s, '"
			+ RDFSUtils.TYPE_PROPERTY
			+ "' as p, it.polimi.processing.rspengine.esper.plain.Ontology.range(p) as o, timestamp as timestamp , app_timestamp as app_timestamp, channel || 'RDSF3' as channel "
			+ "insert into RDFS9Input select o as s, '"
			+ RDFSUtils.TYPE_PROPERTY
			+ "' as p, it.polimi.processing.rspengine.esper.plain.Ontology.range(p) as o, timestamp as timestamp, app_timestamp as app_timestamp , channel || 'RDSF3' as channel "
			+ "insert into QueryOut select s as s, '"
			+ RDFSUtils.TYPE_PROPERTY
			+ "' as p, it.polimi.processing.rspengine.esper.plain.Ontology.domain(p) as o, timestamp as timestamp , app_timestamp as app_timestamp, channel || 'RDSF3' as channel "
			+ "insert into RDFS9Input select s as s, '"
			+ RDFSUtils.TYPE_PROPERTY
			+ "' as p, it.polimi.processing.rspengine.esper.plain.Ontology.domain(p) as o, timestamp as timestamp , app_timestamp as app_timestamp, channel || 'RDSF3' as channel "
			+ "output all";
	public static final String rdfs9 = "on RDFS9Input(p='"
			+ RDFSUtils.TYPE_PROPERTY
			+ "') "
			+ "insert into QueryOut select s as s, p, it.polimi.processing.rspengine.esper.plain.Ontology.subClassOf(o)  as o, timestamp as timestamp, app_timestamp as app_timestamp , channel || 'RDSF9' as channel ";

	public static final String queryOut = "insert into Out "
			+ "select  s as s, p as p, o as o, timestamp as timestamp, app_timestamp as app_timestamp, channel as channel from QueryOut.win:time_batch(1000 msec) ";

	public static final String input_nogenerics = "on RDFSInput "
			+ "insert into RDFS3Input select s as s, o as o, p as p, channel as channel "
			+ "insert into RDFS9Input select s as s, o as o, p as p, channel as channel "
			+ "insert into QueryOut select s as s, o as o, p as p, channel as channel "
			+ "output all ";

	public static final String rdfs3_nogenerics = "on RDFS3Input(p!=typeOf) "
			+ "insert into QueryOut select o as s, typeOf as p, p.range as o, channel || 'RDSF3' as channel "
			+ "insert into RDFS9Input select o as s, typeOf as p, p.range as o, channel || 'RDSF3' as channel "
			+ "insert into QueryOut select s as s, typeOf as p, p.domain as o, channel || 'RDSF3' as channel "
			+ "insert into RDFS9Input select s as s, typeOf as p, p.domain as o, channel || 'RDSF3' as channel "
			+ "output all";

	public static final String rdfs9_nogenerics = "on RDFS9Input(p=typeOf) "
			+ "insert into QueryOut select s as s, p, o.super as o , channel || 'RDSF9' as channel where p=typeOf "
			+ "insert into QueryOut select s as s, p, o as o , channel || 'RDSF9' as channel where p!=typeOf output all";
	public static final String queryOut_nogenerics = "insert into OutEvent "
			+ "select current_timestamp, * from QueryOut.win:time_batch(1000 msec)";
	public static final String queryOut1_nogenerics = "insert into OutEvent "
			+ "select * from QueryOut(instanceof(s, rdf.museo.ihneritance.nogenerics.RDFSUtils.Person) ).win:time_batch(1000 msec)";

}
