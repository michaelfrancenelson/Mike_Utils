package queues;

public class RepetitiveQueue {


	int[] itemsPerPacket;
	int[] numberOfPackets;

	private int position = 0;
	private int packet = 1;


	public RepetitiveQueue(int nPackets, int totalCount)
	{
		/* Three possible scenarios:
		 * 1:  totalCount is greater than nPackets but not evenly divisible (probably most common).
		 * 2:  totalCount is greater than (or equal to) nPackets and is evenly divisible by nPackets.
		 * 3:  totalCount is less than nPackets	 */

		if (totalCount > nPackets)
		{
			int remainder = (int)(totalCount % nPackets);
			int base = totalCount / nPackets;

			if (remainder != 0)
			{
				numberOfPackets = new int[] {remainder, nPackets - remainder};
				itemsPerPacket = new int[] {base + 1, base};
			}
			else
			{
				numberOfPackets = new int[] {nPackets};
				itemsPerPacket = new int[] {base};
			}
		}
		else
		{
			numberOfPackets = new int[] {totalCount, nPackets - totalCount};
			itemsPerPacket = new int[] {1, 0};
		}
	}


	public boolean hasNext()
	{
		return position < itemsPerPacket.length && packet <= numberOfPackets[position];
	}

	public int next()
	{
		if (hasNext())
		{
			int out = itemsPerPacket[position];
			if (packet == numberOfPackets[position]) 
			{
				position++;
				packet = 1;
			}
			else
				packet++;
			return out;
		} 
		else return 0;
	}

	public RepetitiveQueue(int nDivisions, int nPacketsPerDivision, int totalCount)
	{
		/* The array1a sizes needs to be at most 2 * nDivisions */
		int length = nDivisions * 2;

		itemsPerPacket = new int[length];
		numberOfPackets = new int[length];
		
		/* First break up the totalCount items into nDivisions groups: */
		RepetitiveQueue rq = new RepetitiveQueue(nDivisions, totalCount);
		
		int index = 0;
		for (int i = 0; i < nDivisions; i++)
		{
			RepetitiveQueue rq2 = new RepetitiveQueue(nPacketsPerDivision, rq.next());
			for (int j = 0; j < rq2.itemsPerPacket.length; j++)
			{
				itemsPerPacket[index] = rq2.itemsPerPacket[j];
				numberOfPackets[index] = rq2.numberOfPackets[j];
				index++;
			}
		}
		length++;
	}

}
