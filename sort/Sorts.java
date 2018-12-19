package sort;

import java.util.stream.IntStream;

public class Sorts {

	/** Provides the indices to which the input array elements would have to be
	 *   moved to put them in descending order. <br>
	 *   Like order() in R.<br>
	 *   Solution from Pratap Koritala:<br>
	 *   https://stackoverflow.com/questions/4859261/get-the-indices-of-an-array-after-sorting<br>
	 *   TODO:  I don't know about the Stream API, need to learn it to understand this code...*/
	public static int[] sortedIndicesDescending(Comparable[] inputArray)
	{
		return IntStream.range(0, inputArray.length)
				.boxed()
				.sorted((i, j) -> inputArray[j].compareTo(inputArray[i]))
				.mapToInt(ele -> ele).toArray();
	}

	/** Provides the indices to which the input array elements would have to be
	 *   moved to put them in descending order. <br>
	 *   Like order() in R.<br>
	 *   Solution from Pratap Koritala:<br>
	 *   https://stackoverflow.com/questions/4859261/get-the-indices-of-an-array-after-sorting<br>
	 *   TODO:  I don't know about the Stream API, need to learn it to understand this code...*/
	public static int[] sortedIndicesAscending(Comparable[] inputArray)
	{
		return IntStream.range(0, inputArray.length)
				.boxed()
				.sorted((i, j) -> inputArray[i].compareTo(inputArray[j]))
				.mapToInt(ele -> ele).toArray();
	}



}
