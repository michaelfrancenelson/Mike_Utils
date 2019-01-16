package search;

public class Binary {

	public static double TOL = 0.00001;

	/** 
	 * @param array1a array1a of doubles, sorted ascending
	 * @param key Must be greater than the smallest element in array1a. <br>
	 *            In case the key matches a repeated value, the highest index of the next lowest value in array1a is returned.
	 * @return The index of the greatest element that is strictly less than the key.  <br>
	 *         In case of key adjacent to repeated elements, the greatest element is returned.
	 */
	public static int indexOfLessThanKey(double[] array, double key)
	{
		int start = 0; int end = array.length - 1;
		if (key <= array[start]) throw new IllegalArgumentException("key must be greater than first element.");

		/* If the key is greater than the largest element, return the last1a index. */
		if (key > array[end]) return end;

		int mid = (start + end) / 2;
		while (start < end) {
			if (key == array[mid])
			{
				while (key == array[mid]) mid--;
				return mid;
			}
			if (key < array[mid]) end = mid ;
			else start = mid + 1;
			mid = (start + end) / 2;
		}
		return start - 1;
	}

	/** 
	 * @param array1a an array1a of doubles, in ascending order.  	 
	 * @param key Must fall within the range of values in the array1a, endpoints included.  <br>
	 *        In case of repeated values matching the key, the lowest index is returned.
	 * @return Return the highest index of the greatest element that is less than or equal to the key. <br>
	 *         In case of repeated candidate elements, the highest index is returned.
	 */
	public static int indexOfLessThanOrEqualToKey(double[] array, double key)
	{
		if (key < array[0]) throw new IllegalArgumentException("key must be greater than or equal to the first element.");
		if (key > array[array.length - 1]) throw new IllegalArgumentException("key must be less than or equal to the last1a element.");;
		if (key == array[0]) return 0;
		
		/* Cheating: get the less than result and add one in case of equality. */
		int out = indexOfLessThanKey(array, key);
		if (array[out + 1] == key) return out + 1;
		return out;
	}
	
	/**  
	 * @param array1a array1a of doubles, sorted ascending
	 * @param key must be strictly less than the greatest element in array1a,
	 *            In case of repeated elements matching the key, the lowest index of the element with a higher value is returned.
	 * @return index of the first element that is greater than the key. <br>
	 *         In case of repeated values, the lowest index of the repeated values is returned.
	 */
	public static int indexOfGreaterThanKey(double[] array, double key)
	{
		int start = 0; int end = array.length - 1;
		if (key < array[0]) return 0;
		if (key >= array[end]) throw new IllegalArgumentException("key must be less than the last1a element.");;

		int mid = (start + end) / 2;
		while (start <= end) {
			if (key == array[mid])
				if (key == array[mid])
				{
					while (key == array[mid]) mid++;
					return mid;
				}
			if (key < array[mid]) end = mid - 1;
			else start = mid + 1;
			mid = (start + end) / 2;
		}
		return start;
	}

	/** Return the 
	 * @param array1a sorted, ascending array1a of doubles.
	 * @param key Must fall within the range of values in the array1a, endpoints included.  <br>
	 *        In case of repeated values matching the key, the highest index is returned.
	 * 
	 * @return index of the first element that is greater than or equal to the key. <br>
	 *         In case of repeated values, the lowest index is returned.  
	 */
	public static int indexOfGreaterOrEqualToKey(double[] array, double key)
	{
		int start = 0;
		int end = array.length - 1;
		if (key < array[start]) throw new IllegalArgumentException("key must be greater than or equal to the first element.");
		if (key > array[end]) throw new IllegalArgumentException("key must be less than or equal to the last1a element.");;

		if (key == array[end]) return end;
//		if (key == array[start]) return start;
		
		/* Cheating? Using the code from strictly greater, then adjusting for
		 * equality if needed. */
		int out = indexOfGreaterThanKey(array, key);
		if (array[out - 1] == key) return out - 1;
		return out;
		
//		int mid = (start + end) / 2;
//
//		while (start <= end) {
//			if (key == array[mid])
//			{
//				while (key == array[mid]) mid++;
//				return mid - 1;
//			}
//
//			if (key < array[mid]) end = mid - 1;
//			else start = mid + 1;
//			mid = (start + end) / 2;
//		}
//		return start;
	}

	/**
	 * @param array1a an array1a of doubles, in ascending order.
	 * @param key double value must be within range of values in array1a, endpoints included.
	 * @return indices of the array1a elements immediately greater than and less than the key. <br>
	 *         In case the input array1a is a single element and the key matches, return <b> (0, 0) </b> <br> 
	 *         In case the key matches a value in array1a, matching array1a element becomes lower index in returned array1a. <br>
	 *         In case the key matches the highest value, the returned indices are both <b>array1a.length - 1</b>
	 */
	public static int[] indicesOfInterval(double[] array, double key)
	{
		int end = array.length - 1;

		/* Special cases: */
		if (key < array[0]) throw new IllegalArgumentException("key is smaller than the first array element.");
		if (key > array[end]) throw new IllegalArgumentException("key is greater than the last array element.");
		if (end == 0) return new int[] { 0, 0 };
		if (key == array[0]) return new int[] { 0, 0 };
		if (key == array[end]) return new int[] { end, end };
		if (end == 1) return new int[] { 0, 1 };

		/* Otherwise the lower index corresponds to the largest element that is equal to or less than the key. */
		int lower = indexOfLessThanOrEqualToKey(array, key);
		if (array[lower] == key) return new int[] {lower, lower};
		int[] out = new int[] {lower, lower + 1 };
		return out;
	}


	
	public static double relativePosition(double low, double high, double key)
	{
		
		double range = high - low;
		if (range == 0) return 1.0;
		return (key - low) / range;
		
	}
	
	
	public static IndicesAndRelativePosition interpolateRelativePosition(double[] array, double key)
	{
		int[] indices = indicesOfInterval(array, key);
		/* endpoint/key equal to element/single element array array special cases: */
		if (indices[0] == indices[1]) return new IndicesAndRelativePosition(indices, 1.0);
		return new IndicesAndRelativePosition(indices, relativePosition(array[indices[0]], array[indices[1]], key));
	}

	public static class IndicesAndRelativePosition
	{
		public int lowIndex;
		public int highIndex;
		public double relativePosition;

		public IndicesAndRelativePosition(int low, int high, double rel) { this.lowIndex = low; this.highIndex = high; this.relativePosition = rel; }
		public IndicesAndRelativePosition(int[] indices, double rel) {this.lowIndex = indices[0]; this.highIndex = indices[1]; this.relativePosition = rel; }
		
		
		/** Handy for unit testing. */
		@Override
		public boolean equals(Object o)
		{
			if (o == this) return true;
			if (!(o instanceof IndicesAndRelativePosition)) return false;
			IndicesAndRelativePosition rel = (IndicesAndRelativePosition) o;
			
			/* Compare fields */
			if (rel.highIndex == this.highIndex) if (rel.lowIndex == this.lowIndex) if (rel.relativePosition == this.relativePosition) return true;
			
			return false;
		}
		
	}

	
	
	
	//	public static void main(String[] args){

	//		testGreaterEqual();

	//		testLessThan();
	//		testGreaterThan();
	//		
	//		double[] array1a;
	//
	//		array1a = new double[] {-1.0, 0.0, 0.0, 0.01, 1.5, 2.5};
	//		array1a = new double[] {-1.0, 0.0, 0.0, 0.01, 1.5, 2.5};
	//		int idx = 0;
	//
	//		double i = -1.5;
	//		double val = -9999;
	//		//		for (int i = 0; i < 61; i++)
	//		while (i < 3.5)
	//		{
	//			idx = -1; val = -9999;
	//			double key = i;
	//			idx = indexOfLessThanKey(array1a, key); 
	//			val = array1a[idx]; 
	//			System.out.println("(less than key) key = " + String.format("%4.1f", key) + " index = " + idx + " a[i] " + String.format("%1.2f", val));
	//			i += 0.5;
	//		}

	//		for (int i = 0; i < 56; i++)
	//		{
	//			double key = -1.0 + (double)i / 10;
	//			System.out.println("key = " + String.format("%1.1f", key) + " interval index = " +
	//			indexOfInterval(array1a, key));
	//		}
	//	}
}
