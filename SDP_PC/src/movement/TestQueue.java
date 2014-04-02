package movement;

public class TestQueue {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("TEST");
		Test test = new Test("attack");
/*		add(test);*/
		Test test2 = new Test("defence");

		System.out.println("TEST2 HAS " + test2.numQueuedJobs());
		add(test2);
		add(test);

		System.out.println("TEST HAS " + test.numQueuedJobs());

		//test.interrupt();
		//Test test = new Test("defence");
		System.out.println("NUMBER OF JOBS: " + test.numQueuedJobs());
		test.forward();

	}
	
	public static void add(Test test) throws InterruptedException {
			test.forward();
			test.backward();
			test.start();
			

			System.out.println("TEST HAS " + test.numQueuedJobs());
			
			Thread.sleep(5000);

	}
	
	
}
