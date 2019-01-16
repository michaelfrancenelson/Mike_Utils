package sequeuces;


/**
*  @author michaelfrancenelson
*
*/

public class AllocateItems {
	
	public static void main(String[] args) {
		int total;
		int maxBinSize;
		int[] seq;
		
		total = 50;
		maxBinSize = 13;
		seq = allocateCountToBins(total, maxBinSize);
		for (int i : seq) System.out.println(" " + i);

		maxBinSize = 10;
		seq = allocateCountToBins(total, maxBinSize);
		for (int i : seq) System.out.println(" " + i);
//			seq = allocateCounts(total, maxBinSize);
	}
	
	public static int[] allocateCountToBins(int total, int maxBinSize)
	{
		int[] out;
		int nBins;
		int remainder = total % maxBinSize;
		/* How many bins are needed. */
		if (remainder == 0)
		{
			nBins = total / maxBinSize;
			out = new int[nBins];
			for (int i = 0; i < out.length; i++) { out[i] = maxBinSize;	}
		}
		else
		{
			nBins = total / maxBinSize + 1;
			out = new int[nBins];
			for (int i = 0; i < out.length - 1; i++) {
				out[i] = maxBinSize;
			}
			out[out.length - 1] = remainder;
		}

		return out;
	}
}
