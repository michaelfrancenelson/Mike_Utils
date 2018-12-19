package queues;

import cern.jet.random.Uniform;
import cern.jet.random.engine.RandomEngine;

public class FixedLengthIntQueue {

	private int[] queue;
	private int head;

	public FixedLengthIntQueue(int length)
	{
		queue = new int[length];
		head = 0;
	}
	
	public FixedLengthIntQueue(int[] elements)
	{
		queue = elements;
		head = 0;
	}

	public int getQueueSize() { return queue.length; }

	/** Retrieve and remove the item at the head of the queue and advance the queue. */
	public int dequeue()
	{
		int out = queue[head];
		queue[head] = 0;
		advance();
		return out;
	}

	/** Return the element at the head of the queue, but do not advance the queue. */
	public int peek() { return queue[head]; }

	public void enqueueRandomPosition(int item, RandomEngine re)
	{
		int index = (new Uniform(re)).nextIntFromTo(0, queue.length - 1);
		queue[index] += item;
	}
	

	/** Add one element to the last1a position in the queue.
	 *  If the last1a position is not 0, then the new number is added to the
	 *  quantity already present.  enqueue() does not advance the head position. */
	public void enqueue(int item)
	{
		int index = head - 1;
		if (index == -1) index = queue.length - 1;
		queue[index] += item;
	}

	/** Add multiple items to the queue.  The elements toward the front of the input array1a
	 * are placed closer to the front of the queue, and thus are dequeued sooner.  
	 * Items added to non-empty spots in the queue are added to the existing total at that index.
	 * @param items */
	public void enqueue(int[] items)
	{
		for (int i = 0; i < items.length; i++)
		{
			int index = head - (items.length - i);
			if (index < 0) index += queue.length;
			queue[index] += items[i];
		}
	}
	
	public void advance()
	{
		head++;
		if (head >= queue.length) head = 0;
	}

	public String toString()
	{
		String out = "";
		for (int i = 0; i < queue.length; i++)
		{
			out += String.format("%-4d", queue[(head + i) % queue.length]);
//			out += queue[i] + " ";
		}
		return out;
	}

}
