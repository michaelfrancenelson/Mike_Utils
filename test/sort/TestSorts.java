package sort;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

public class TestSorts {


	@Test
	public void testIndexSort() {

		Double[] treeHeights = new Double[] {1.0, 0.5, 0.1, 10.0, 11.0, 9.0, 9.0};
		Double[] treeHeightsAsc = new Double[] {1.0, 0.5, 0.1, 10.0, 11.0, 9.0, 9.0};
		Double[] treeHeightsDsc = new Double[] {1.0, 0.5, 0.1, 10.0, 11.0, 9.0, 9.0};
		int[] expectedAsc = new int[] {2, 1, 0, 5, 6, 3, 4};
		int[] expectedDesc = new int[expectedAsc.length];
		for(int i = 0; i < expectedAsc.length; i++){
			expectedDesc[expectedAsc.length - i - 1] = expectedAsc[i];
		}
		
		/* Order of indices for tied elements is tricky... */
		expectedDesc[2] = 5;
		expectedDesc[3] = 6;
		
		
		int[] sortedIndices = Sorts.sortedIndicesAscending(treeHeights);
		int[] sortedIndicesDesc = Sorts.sortedIndicesDescending(treeHeights);
		
		Arrays.sort(treeHeightsDsc);
		ArrayUtils.reverse(treeHeightsDsc);
		Arrays.sort(treeHeightsAsc);
		
		for(int i = 0; i < expectedAsc.length; i++)
		{
			assertEquals(expectedAsc[i], sortedIndices[i]);
			assertEquals(expectedDesc[i], sortedIndicesDesc[i]);
			assertEquals(treeHeightsAsc[i], treeHeights[sortedIndices[i]]);
			assertEquals(treeHeightsDsc[i], treeHeights[sortedIndicesDesc[i]]);
		}
	}

}
