package search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.function.BiFunction;

import org.junit.Before;
import org.junit.Test;

import junit4.Equality;
import search.Binary.IndicesAndRelativePosition;

public class TestBinary {

	int nDuplicates;
	int nLevels;
	double key1;

	double[] duplicatedArray;

	double[] array1a, array1b, array1c, array1d, array2;
	double keyMid;
	double val = -9999;
	int idx = -1;
	Double key = -1.5;

	double tol = 0.0000001;
	double testIncrement = 0.0001;
	int last1a, last1b; 
	int[] repeatedEndpoints1b;
	int[] repeatedCounts1b;
	int repeatedCountsEndpoints;
	
	IndicesAndRelativePosition expectedIRP = null, observedIRP = null;

	BiFunction<double[], Double, Integer> 
	fLessThanKey = Binary::indexOfLessThanKey, 
	fGreaterThanKey = Binary::indexOfGreaterThanKey, 
	fEqualGreaterThanKey = Binary::indexOfGreaterOrEqualToKey, 
	fEqualLessThanKey = Binary::indexOfLessThanOrEqualToKey;

	double[] array3;
	private int end;
	private double interval; 

	/** Force fGreaterThanKey to be initialized in each test method: */
	@Before public void setup() 
	{ 
		fLessThanKey = Binary::indexOfLessThanKey;
		fGreaterThanKey = Binary::indexOfGreaterThanKey; 
		fEqualGreaterThanKey = Binary::indexOfGreaterOrEqualToKey; 
		fEqualLessThanKey = Binary::indexOfLessThanOrEqualToKey;

		end = 99;
		interval = 0.77;
		testIncrement = 0.01;

		/* Array with no repeats. */
		array1a = new double[end + 1];
		array1b = new double[end + 1];
		array1c = new double[end + 1];
		array1d = new double[end + 1];

		double sum = -5.0;
		for (int i = 0; i <= end; i++)
		{
			array1a[i] = sum;
			array1b[i] = sum;
			array1c[i] = sum;
			array1d[i] = sum;
			sum += interval;
		}

		/* Array with only interior repeats */
		repeatedEndpoints1b = new int[] {20, 45, 70};
		repeatedCounts1b = new int[] {2, 3, 11};

		/* Array with repeated first values; */
		repeatedCountsEndpoints = 11;

		/* Add duplicates */
		for (int i = 0; i < repeatedEndpoints1b.length; i++)
		{
			for (int j = repeatedEndpoints1b[i]; j < repeatedEndpoints1b[i] + repeatedCounts1b[i]; j++)
				array1b[j] = array1b[repeatedEndpoints1b[i]];
		}


		double endVal = array1d[end - repeatedCountsEndpoints];
		double startVal = array1d[0];
		for (int j = 0; j < repeatedCountsEndpoints; j++) 
		{
			array1c[j] = startVal;
			array1d[end - j] = endVal;
		}

		//		fGreaterThanKey = null;
		key = Double.MIN_VALUE; 
		idx = Integer.MIN_VALUE;

		array2 = new double[] {-10.0, 1.0 };
		keyMid = 0.9999;
		array3 = new double [] { keyMid };
	}

	void setupDuplicates(int nDup, int nLev, double interval)
	{
		nDuplicates = nDup;
		nLevels = nLev;
		this.interval = interval;

		duplicatedArray = new double[nDuplicates * nLevels];
		for (int i = 0; i < nLevels; i++)
			for (int j = 0; j < nDuplicates; j++)
				duplicatedArray[i * nDuplicates + j] = i * interval;
	}

	private int assertException(double[] array, double key, BiFunction<double[], Double, Integer> f)
	{
		int out = Integer.MIN_VALUE;
		try { f.apply(array, key); fail();} catch(Exception ex) { assertTrue(ex instanceof IllegalArgumentException); }
		return out;
	}
	private int assertNoException(double[] array, double key, BiFunction<double[], Double, Integer> f)
	{
		int out = Integer.MIN_VALUE;
		try { out = f.apply(array, key); assertTrue(true); } catch(Exception ex) { fail(); }
		return out;
	}

	public void testIndex(double key, int expectedIndex, double[] array, BiFunction<double[], Double, Integer> f) { assertEquals(expectedIndex, assertNoException(array, key, f)); }
	public void failSearch(double key, double[] array, BiFunction<double[], Double, Integer> f) { assertException(array, key, f); }

	@Test
	public void testEndpoints()
	{
		double[] array;

		nLevels = 100;
		for (int nDup = 1; nDup < 10; nDup++)
		{
			setupDuplicates(nDup, nLevels, interval);
			/* Should throw an exception if key is less than or equal to first element. */
			array = duplicatedArray;

			/* Test just the lowest and highest levels: */
			for (int i : new int[] {0, nLevels - 1}) for (int j = 0; j < nDuplicates; j++)
			{
				int start = 0;
				end = array.length - 1;
				key = duplicatedArray[i * nDuplicates];
				int lowestRepeatedIndex = i * nDuplicates;
				int highestRepeatedIndex = i * nDuplicates + nDuplicates - 1;

				/* Endpoint exclusive searches should fail at opposite ends and return (adjusted) indices at same ends: */
				/* Searches with equality should return the (adjusted) index and fail outside the range of the array*/ 
				if (key == array[end])
				{
					failSearch(key, array, fGreaterThanKey);

					testIndex(key, lowestRepeatedIndex - 1, array, fLessThanKey);
					testIndex(key + tol, end, array, fLessThanKey);

					failSearch(key + tol, array, fEqualLessThanKey);
					failSearch(key + tol, array, fEqualGreaterThanKey);

					testIndex(key, end, array, fEqualGreaterThanKey);
				}
				if (key == array[start])
				{
					failSearch(key, array, fLessThanKey);

					testIndex(key, highestRepeatedIndex + 1, array, fGreaterThanKey);
					testIndex(key - tol, start, array, fGreaterThanKey);

					failSearch(key - tol, array, fEqualLessThanKey);
					failSearch(key - tol, array, fEqualGreaterThanKey);

					testIndex(key, start, array, fEqualLessThanKey);
				}
			}
		}
	}

	@Test 
	public void testInteriorElements()
	{
		for (int nDup = 1; nDup < 11; nDup ++) for (int nLev = 1; nLev < 90; nLev++)
		{
			setupDuplicates(nDup, nLev, interval);
			for (int i = 0; i < nLevels; i++) for (int j = 0; j < nDuplicates; j++)
			{
				key = duplicatedArray[i * nDuplicates + j];
				int lowestRepeatedIndex = i * nDuplicates;
				int highestRepeatedIndex = i * nDuplicates + nDuplicates - 1;

				/* Less than or equal should return lowest index of matching duplicates. */
				testIndex(key, lowestRepeatedIndex, duplicatedArray, fEqualLessThanKey);

				/* Greater than or equal should return highest index of matching duplicates. */
				testIndex(key, highestRepeatedIndex, duplicatedArray, fEqualGreaterThanKey);

				/* Strictly less should throw an exception if key equal to first element. */
				if (key <= duplicatedArray[0]) 
					failSearch(key, duplicatedArray, fLessThanKey);
				/* Otherwise, strictly less should return one less than the lowest repeated element's index */
				else testIndex(key, lowestRepeatedIndex - 1, duplicatedArray, fLessThanKey);

				/* Strictly greater should throw an exception if key equal to highest element. */
				if (key >= duplicatedArray[duplicatedArray.length - 1])
					failSearch(key, duplicatedArray, fGreaterThanKey);
				/* Strictly greater should return one more than the highest repeated element's index */
				else testIndex(key, highestRepeatedIndex + 1, duplicatedArray, fGreaterThanKey);

				/* Nonmatching keys: */
				key1 = key + tol;
				if (key1 <= duplicatedArray[duplicatedArray.length - 1]) {

					testIndex(key1, highestRepeatedIndex + 1, duplicatedArray, fGreaterThanKey);
					testIndex(key1, highestRepeatedIndex + 1, duplicatedArray, fEqualGreaterThanKey);

					testIndex(key1, highestRepeatedIndex, duplicatedArray, fLessThanKey);
					testIndex(key1, highestRepeatedIndex, duplicatedArray, fEqualLessThanKey);
				}
				key1 = key - tol;
				if (key1 >= duplicatedArray[0]) {

					testIndex(key1, lowestRepeatedIndex, duplicatedArray, fGreaterThanKey);
					testIndex(key1, lowestRepeatedIndex, duplicatedArray, fEqualGreaterThanKey);

					testIndex(key1, lowestRepeatedIndex - 1, duplicatedArray, fLessThanKey);
					testIndex(key1, lowestRepeatedIndex - 1, duplicatedArray, fEqualLessThanKey);
				}
			}
		}
	}

	@Test
	public void testIntervalIndices()
	{
		double[] array;
		int[] expected;
		int[] observed;
		for (int nDup = 1; nDup < 11; nDup ++) for (int nLev = 1; nLev < 90; nLev++)
		{
			setupDuplicates(nDup, nLev, interval);
			end = duplicatedArray.length - 1;
			for (int i = 0; i < nLevels; i++) for (int j = 0; j < nDuplicates; j++)
			{
				key = duplicatedArray[i * nDuplicates + j];
				int lowestRepeatedIndex = i * nDuplicates;
				int highestRepeatedIndex = i * nDuplicates + nDuplicates - 1;

				array = duplicatedArray;

				for (double d : new double[] {0, tol})
				{
					key += d;
					if (key <= array[end])
					{

						/* If the key is equal to the flrst or last element, return only the first or last indices. */
						if (key == duplicatedArray[0]) expected = new int[] {0, 0};
						else if (key == duplicatedArray[end]) expected = new int[] {end, end};

						/* If the key is equal to a non duplicated element, return only the elemen't index. */
						else if (array[lowestRepeatedIndex] == key && highestRepeatedIndex == lowestRepeatedIndex) expected = new int[] {lowestRepeatedIndex, lowestRepeatedIndex};

						/* If the key is equal to a duplicated element, return only the lowest duplicated element's index. */
						else if (lowestRepeatedIndex < highestRepeatedIndex && array[lowestRepeatedIndex] == key) 
							expected = new int[] {lowestRepeatedIndex, lowestRepeatedIndex};

						else expected = new int[] {highestRepeatedIndex, highestRepeatedIndex + 1};

						observed = Binary.indicesOfInterval(array, key);

						/* In case the key matches an element: */
						Equality.assertEqualsArray(expected, observed);
					}
				}
			}
		}
	}

	@Test
	public void testRelativePosition()
	{
		double low = -1.0;
		double high = 1.0;
		double range = high - low;
		
		key = -1.0;
		
		int nIntervals = 1234;
		interval = range / nIntervals;
		
		int test = 0;
		while (key <= 1.0)
		{
			key = low + interval * test;
			assertEquals(interval * test, range * Binary.relativePosition(low, high, key), tol);
			test++;
		}
	}
	
	@Test
	public void testIndicesAndRelativePosition()
	{
		double[] array;

		for (int nDup = 1; nDup < 11; nDup ++) for (int nLev = 1; nLev < 90; nLev++)
		{
			setupDuplicates(nDup, nLev, interval);
			end = duplicatedArray.length - 1;
			for (int i = 0; i < nLevels; i++) for (int j = 0; j < nDuplicates; j++)
			{
				key = duplicatedArray[i * nDuplicates + j];
				int lowestRepeatedIndex = i * nDuplicates;
				int highestRepeatedIndex = i * nDuplicates + nDuplicates - 1;

				array = duplicatedArray;

				for (double d : new double[] {0, tol})
				{
					int expIdx1, expIdx2;
					double expRel;
					key += d;

					if (key <= array[end])
					{
						idx = Binary.indexOfLessThanOrEqualToKey(array, key);
						
						/* Equal to an endpoint: */
						if (key == array[0]) {expIdx1 = expIdx2 = 0; expRel = 1.0; }
						else if (key == array[end])	{expIdx1 = expIdx2 = end; expRel = 1.0; }

						/* If the key is equal to a non-duplicated interior element, return only the elemen't index. */
						else if (array[idx] == key & highestRepeatedIndex == lowestRepeatedIndex) {expIdx1 = expIdx2 = idx; expRel = 1.0; }

						/* If the key is equal to a duplicated element, return only the lowest duplicated element's index. */
						else if (array[idx] == key & highestRepeatedIndex != lowestRepeatedIndex) {expIdx1 = expIdx2 = lowestRepeatedIndex; expRel = 1.0; }
							
						else {expIdx1 = idx; expIdx2 = idx + 1; expRel = Binary.relativePosition(array[idx],  array[idx + 1], key); }

						setIRPs(expIdx1, expIdx2, expRel, array, key);
						assertEquals(observedIRP, expectedIRP);
					}
					
				}
			}
		}
	}
	
	private void setIRPs(int expIdx1, int expIdx2, double expRel, double[] array, double key)
	{
		expectedIRP = new IndicesAndRelativePosition(expIdx1, expIdx2, expRel);
		observedIRP = Binary.interpolateRelativePosition(array, key);
	}
}