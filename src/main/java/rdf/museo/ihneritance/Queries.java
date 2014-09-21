package rdf.museo.ihneritance;

public class Queries {


	public static final String input_nogenerics = "on RDFSInput "
			+ "insert into RDFS3Input select s as s, o as o, p as p, channel as channel "
			+ "insert into RDFS9Input select s as s, o as o, p as p, channel as channel "
			+ "insert into QueryOut select s as s, o as o, p as p, channel as channel "
			+ "output all ";

	public static final String rdfs3_nogenerics = "on RDFS3Input(p!=typeof) "
			+ "insert into QueryOut select o as s, typeof as p, p.range as o, channel || 'RDSF3' as channel "
			+ "insert into RDFS9Input select o as s,  typeof as p, p.range as o, channel || 'RDSF3' as channel "
			+ "insert into QueryOut select s as s, typeof as p, p.domain as o, channel || 'RDSF3' as channel "
			+ "insert into RDFS9Input select s as s, typeof as p, p.domain as o, channel || 'RDSF3' as channel "
			+ "output all";

	public static final String rdfs9_nogenerics = "on RDFS9Input(p=typeof) "
			+ "insert into QueryOut select s as s, p, o.super as o , channel || 'RDSF9' as channel where p=typeof "
			+ "insert into QueryOut select s as s, p, o as o , channel || 'RDSF9' as channel where p!=typeof "
			+ "output all";
	public static final String queryOut_nogenerics = "insert into OutEvent "
			+ "select current_timestamp, * from QueryOut.win:time_batch(1000 msec)";
	public static final String queryOut1_nogenerics = "insert into OutEvent "
			+ "select * from QueryOut(instanceof(s, rdf.museo.ihneritance.nogenerics.ontology.Person) ).win:time_batch(1000 msec)";

}
