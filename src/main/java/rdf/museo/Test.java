package rdf.museo;

public class Test {

	public static void main(String[] args) {
		Prova p = new Prova();
		EstendeProva ep = new EstendeProva();
		Class<Prova> clazzProva = Prova.class;
		Class<EstendeProva> clazzEP = EstendeProva.class;

		System.out.println(p instanceof Prova);
		System.out.println(p instanceof EstendeProva);
		System.out.println(ep instanceof Prova);
		System.out.println(ep instanceof EstendeProva);
		System.out.println(Prova.class instanceof Class<?>);
		System.out.println(EstendeProva.class instanceof Class<?>);

		System.out.println(clazzProva.isInstance(p));
		System.out.println(clazzEP.isInstance(p));
		System.out.println(clazzProva.isInstance(ep));
		System.out.println(clazzEP.isInstance(ep));

	}

	private static class Prova {
		String s;
	}

	private static class EstendeProva extends Prova {
		String s;
	}

}
