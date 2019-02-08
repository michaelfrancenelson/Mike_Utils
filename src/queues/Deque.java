package queues;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

	protected Node<Item> first, last;
	protected int n;

	protected class Node<T> {
		public T item;
		public Node<T> ahead;
		public Node<T> behind;
	}

	/** Construct a new empty Deque. */
	public Deque() { first = null; last = null; n = 0; }

	/**  is the deque empty? */
	public boolean isEmpty() { return n == 0; }

	/** return the number of items on the deque */
	public int size() {	return n; }

	/** add the item to the front */
	public void addFirst(Item item) {
		if (item == null) throw new IllegalArgumentException("Trying to add a null element.");
		if (!isEmpty())
		{
			Node<Item> oldFirst = first;
			first = new Node<>();
			first.item = item;
			first.behind = oldFirst;
			oldFirst.ahead = first;
			if (n == 1) last = oldFirst;
			n++;
		} 
		else addToEmpty(item);
	}

	/** Add the first element to an empty deque. */
	private void addToEmpty(Item item) {
		first = new Node<Item>();
		first.item = item;
		last = first;
		n = 1;
	}

	/** add the item to the end */
	public void addLast(Item item)  {
		if (item == null) throw new IllegalArgumentException("Trying to add a null element.");
		if (!isEmpty())
		{
			if (n > 1) {
				Node<Item> oldLast = last;
				last = new Node<Item>();
				last.item = item;
				last.ahead = oldLast;
				oldLast.behind = last;
			} else
			{
				last = new Node<Item>();
				last.item = item;
				last.ahead = first;
				first.behind = last;
			}
			n++;
		}
		else addToEmpty(item);
	}

	/** remove and return the item from the front */
	public Item removeFirst()
	{
		if (isEmpty()) throw new NoSuchElementException("Trying to remove an item from an empty deque...");
		if (n > 1)
		{
			Item item = first.item;
			first = first.behind;
			/* Stage the removed node for garbage collection: */
			first.ahead = null;
			n--;
			if (isEmpty()) last = null;
			return item;
		}
		else return clear();
	}

	/** remove and return the item from the end */
	public Item removeLast()
	{
		if (isEmpty()) throw new NoSuchElementException("Trying to remove an item from an empty deque...");	
		if (n > 1)
		{
			Item item = last.item;
			last = last.ahead;
			/* Stage the removed node for garbage collection: */
			last.behind = null;
			n--;
			if (isEmpty()) last = null;
			return item;
		}
		else return clear();
	} 

	/** Remove the final element in the queue. */
	private Item clear() 
	{
		Item item = first.item;
		first = null;
		last = null;
		n = 0;
		return item;
	}

	/** return an iterator over items in order from front to end */
	public Iterator<Item> iterator() {
		return new DequeIterator();
	}
	
	protected class DequeIterator implements Iterator<Item> 
	{

		private boolean firstNode = true;
		private Node<Item> current;

		public boolean hasNext()  {
			if (n < 1) return false;
			if (!firstNode)
				return current.behind != null;
			else return true;
		}

		public Item next()  {
			if (!hasNext()) throw new NoSuchElementException("Trying to call next() after the iterator is exhausted.");
			if (!firstNode)
			{
				Item item = current.behind.item;
				current = current.behind;
				return item;
			}
			else
			{
				current = first;
				firstNode = false;
				return current.item;
			}
		}

		public void remove() {
			throw new UnsupportedOperationException("Use of remove() is unsupported.");
		}
	}
}
