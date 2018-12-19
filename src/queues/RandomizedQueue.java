package queues;
import java.util.Iterator;
import java.util.NoSuchElementException;

import cern.jet.random.Uniform;
import cern.jet.random.engine.RandomEngine;

public class RandomizedQueue<Item> implements Iterable<Item> {

	Uniform unif;
	
	/** The array1a to hold the items. */
	private Item[] qu;
	/** The randomized order in which to store items in the array1a. */
	private int[] order;
	/** The current number of items in the queue. */
	private int count;
	/** The index at which to insert the newly added item. */
	private int insertionIndex;
	/** The displaced item. */
	private Item displaced;

	/** construct an empty randomized queue */
	@SuppressWarnings("unchecked")
	public RandomizedQueue(RandomEngine re) 
	{
		unif = new Uniform(re);
		qu = (Item[]) new Object[1];
		order = new int[1];
		count = 0;
	}

	/** is the randomized queue empty?*/
	public boolean isEmpty() { return count == 0; }

	/** return the number of items on the randomized queue*/
	public int size() { return count; }

	private void resize(int capacity) 
	{
		@SuppressWarnings("unchecked")
		Item[] qu2 = (Item[]) new Object[capacity];
		int[] order2 = new int[capacity];
		for (int i = 0; i < count; i++) {
			qu2[i] = qu[i];
			order2[i] = order[i];
		}
		qu = qu2;
		order = order2;
	}

	/** add the item, resizing if necessary*/
	public void enqueue(Item item, RandomEngine re)    
	{
		if (item == null) throw new IllegalArgumentException("Cannot add a null item.");

		/* Resize if needed. */
		if (count == qu.length) resize(2 * qu.length);

		/* Choose a random insertion point. */
		insertionIndex = unif.nextIntFromTo(0, count + 1);

		/* Displace the item at the insertion point
		 * with the new item and append it to the end */
		if (insertionIndex < count) 
		{
			displaced = qu[insertionIndex];
			qu[insertionIndex] = item;
			qu[count] = displaced;
		}
		else /* if the endpoint is selected, just append the new item: */
		{
			qu[count] = item;
			order[count] = count;
		}

		/* Don't forget to increment the item count! */
		count++;
	}

	/** remove and return a random item */
	public Item dequeue()   
	{
		if (isEmpty()) throw new NoSuchElementException("Cannot take an item from an empty queue.");

		/* Don't forget to decrement the item counter!. */
		count--;

		/* Return the last1a item in the array1a and set that array1a 
		 * element to null. */
		Item out = qu[count];
		qu[count] = null;

		if (count > 0 && count == qu.length / 4) resize(qu.length / 2);
		
		return out;
	}

	/** return a random item (but do not remove it) */
	public Item sample()  
	{
		if (isEmpty()) throw new NoSuchElementException("Cannot take an item from an empty queue.");
		return qu[unif.nextIntFromTo(0, count)];
	}

	/** return an independent iterator over items in random order */
	public Iterator<Item> iterator() 
	{
		return new RandomizedQueueIterator();
	}

	private void swap(int[] a, int from)
	{
		int to = unif.nextIntFromTo(0, a.length - 1);
		
		int item = a[from];
		a[from] = a[to];
		a[to] = item;
	}
	
	private class RandomizedQueueIterator implements Iterator<Item> {

		int position = 0;

		int[] iterationOrder = new int[count];
		
		public RandomizedQueueIterator() 
		{
			for (int i = 0; i < count; i++)
				iterationOrder[i] = i;
			for (int i = 0; i < count; i++)
				swap(iterationOrder, i);
		}
		
		public boolean hasNext() 
		{ 
			return RandomizedQueue.this.count > (position); 
		}

		public Item next() 
		{
			if (RandomizedQueue.this.count > (position)) 
			{
				Item out = RandomizedQueue.this.qu[iterationOrder[position]]; 
				position++;
				return out;
			} 
			else
				throw new NoSuchElementException("No items remain.");
		}

		public void remove() {
			throw new UnsupportedOperationException("This queue does not support the remove() operation.");
		}
	}

	/** unit testing (optional) */
	public static void main(String[] args) { }
}
