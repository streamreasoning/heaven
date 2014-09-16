package commons;

public class Test {

	public static void main(String[] args) throws Exception {
		final Streamer s = new Streamer();
		Thread t = new Thread(new Runnable() {

			public void run() {
				try {
					s.stream();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		t.start();
		int i = 1000000000;
		while (i > 0)
			i--;

		while (true) {
			if (s.hasNext()) {
				String[] event = Streamer.getEvent();
				for (String string : event) {
					System.out.println(string);
					
				}
				Thread.sleep(1000);
			}
		}

	}
}
