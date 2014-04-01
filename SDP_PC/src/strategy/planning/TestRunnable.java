package strategy.planning;

public class TestRunnable implements Runnable{

	public TestRunnable() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestRunnable t1 = new TestRunnable();
		System.out.println("are we winning?");
		Thread t = new Thread(t1);
		t.start();
		System.out.println("Success!");
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("asd123");
		while(true){}
	}

}
