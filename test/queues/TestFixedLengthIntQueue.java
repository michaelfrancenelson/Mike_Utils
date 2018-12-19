package queues;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestFixedLengthIntQueue {

	FixedLengthIntQueue queue;
	int length = 10;
	int[] testQueue;
	
	@Before
	public void setup()
	{
		queue = new FixedLengthIntQueue(length);
		testQueue = new int[length];
	}

	@Test
	public void test2()
	{
		int sizeDiff = 3;
		int toAdd = 2;
		int length2 = length - sizeDiff;
		int[] input = new int[length2];
		for(int i = 0; i < length2; i++) input[i] = i + toAdd;

		queue.enqueue(input);
//		printQueue();
		
		for(int i = length - length2; i < length; i++) testQueue[i] = i + toAdd - sizeDiff;
		for(int i = 0; i < length; i++) { Assert.assertEquals(queue.dequeue(), testQueue[i]); }
		
//		printQueue();
	}
	

	@Test
	public void test1()
	{
		for(int i = 0; i < length; i++)
		{
			testQueue[i] = i;
			queue.dequeue(); queue.enqueue(i);
			printQueue();
		}
		
//		printQueue();
		for(int i = 0; i < length; i++) { Assert.assertEquals(queue.dequeue(), testQueue[i]); }

//		printQueue();
		for(int i = 0; i < length; i++)	{ Assert.assertEquals(queue.dequeue(), 0); }
	}

	/** Test that overlapping calls to enqueue are summed. */
	@Test
	public void test3() {
		
		int sizeDiff = 3;
		int toAdd = 1;
		int times = 56;

		int length2 = length - sizeDiff;
		int[] input = new int[length2];
		for (int i = 0; i < length2; i++) input[i] = i + toAdd;

		for (int i = 0; i < times; i++) queue.enqueue(input);
//		printQueue();
		
		for (int i = length - length2; i < length; i++) testQueue[i] = times * (i + toAdd - sizeDiff);
		for (int i = 0; i < length; i++) { Assert.assertEquals(queue.dequeue(), testQueue[i]); }
		
//		printQueue();
		
		for (int i = 0; i < times; i++) queue.enqueue(1);

//		printQueue();

		for(int i = 0; i < length - 1; i++) queue.dequeue();
		Assert.assertEquals(queue.dequeue(), times);
//		printQueue();
		
	}

	private void printQueue() { System.out.println(queue.toString());	}
}
