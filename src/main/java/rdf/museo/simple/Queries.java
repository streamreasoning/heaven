package rdf.museo.simple;

public class Queries {

	public static final String input = "on TEvent "
			+ "insert into RDFS3Input select s as s, o as o, p as p, timestamp as timestamp, channel as channel "
			+ "insert into RDFS9Input select s as s, o as o, p as p, timestamp as timestamp, channel as channel "
			+ "insert into QueryOut select s as s, o as o, p as p, timestamp as timestamp, channel as channel "
			+ "output all ";

	public static final String rdfs3 = "on RDFS3Input(p!='typeOf' and p!='type') "
			+ "insert into QueryOut select o as s, 'typeOf' as p, rdf.museo.simple.Ontology.range(p) as o, timestamp as timestamp , channel || 'RDSF3' as channel "
			+ "insert into RDFS9Input select o as s, 'typeOf' as p, rdf.museo.simple.Ontology.range(p) as o, timestamp as timestamp , channel || 'RDSF3' as channel "
			+ "insert into QueryOut select s as s, 'typeOf' as p, rdf.museo.simple.Ontology.domain(p) as o, timestamp as timestamp , channel || 'RDSF3' as channel "
			+ "insert into RDFS9Input select s as s, 'typeOf' as p, rdf.museo.simple.Ontology.domain(p) as o, timestamp as timestamp , channel || 'RDSF3' as channel "
			+ "output all";

	public static final String rdfs9 = "on RDFS9Input(p='typeOf' or p='type') "
			+ "insert into QueryOut select s as s, p, rdf.museo.simple.Ontology.subClassOf(o)  as o, timestamp as timestamp , channel || 'RDSF9' as channel ";

	public static final String queryOut = "insert into Out "
			+ "select  timestamp, s, p, o, channel from QueryOut.win:time_batch(1000 msec) ";

}
