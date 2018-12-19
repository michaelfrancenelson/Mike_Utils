package queues;

public class StackOfInts {
	private int[] s;
	private int n = 0;
	
	public StackOfInts(int initialSize) { s = new int[initialSize]; }
	
	public boolean isEmpty(){ return n == 0; }
	
	private void resize(int capacity){
		int[] s2 = new int[capacity];
		for(int i = 0; i < n; i++) s2[i] = s[i];
		s = s2;
	}
	
	public void push (int item) { 
		if (n == s.length) resize(2 * s.length);
		s[n++] = item; }
	
	public int pop(){
		n--;
		int item = s[n];
		if(n > 0 && n == s.length / 4){ 
			resize(s.length / 2);
		}
		return item;
	}
	
	public int size() { return n; }
}
