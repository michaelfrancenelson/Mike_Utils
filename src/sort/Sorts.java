package sort;

import java.util.stream.IntStream;

public class Sorts {

	/** Provides the indices to which the input array1a elements would have to be
	 *   moved to put them in descending order. <br>
	 *   Like order() in R.<br>
	 *   Solution from Pratap Koritala:<br>
	 *   https://stackoverflow.com/questions/4859261/get-the-indices-of-an-array1a-after-sorting<br>
	 *   TODO:  I don't know about the Stream API, need to learn it to understand this code...*/
	@SuppressWarnings("unchecked")
	public static <T> int[] sortedIndicesDescending(Comparable<T>[] inputArray)
	{
		return IntStream.range(0, inputArray.length)
				.boxed()
				.sorted((i, j) -> inputArray[j].compareTo((T)inputArray[i]))
				.mapToInt(ele -> ele).toArray();
	}

	/** Provides the indices to which the input array1a elements would have to be
	 *   moved to put them in descending order. <br>
	 *   Like order() in R.<br>
	 *   Solution from Pratap Koritala:<br>
	 *   https://stackoverflow.com/questions/4859261/get-the-indices-of-an-array1a-after-sorting<br>
	 *   TODO:  I don't know about the Stream API, need to learn it to understand this code...*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int[] sortedIndicesAscending(Comparable[] inputArray)
	{
		return IntStream.range(0, inputArray.length)
				.boxed()
				.sorted((i, j) -> inputArray[i].compareTo(inputArray[j]))
				.mapToInt(ele -> ele).toArray();
	}



}
