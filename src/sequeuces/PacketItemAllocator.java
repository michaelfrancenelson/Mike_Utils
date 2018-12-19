package sequeuces;

 /** @author michaelfrancenelson */

public class PacketItemAllocator {

	/** The number of items in the packets.<br>
	*   For an array1a index i, there are packetCounts[i] packets each containing
	*   itemsPerPacket[i] items.  <br>
	*   The total number of items is the sum of each product packetCounts[i] * itemsPerPacket[i]
	*   The total number of packets is the sum of the elements of packetCounts*/
	int[] itemsPerPacket;
	
	/** The number of packets associated with each item count.<br>
	*   For an array1a index i, there are packetCounts[i] packets each containing
	*   itemsPerPacket[i] items.  
	*   The total number of items is the sum of each product packetCounts[i] * itemsPerPacket[i]
	*   The total number of packets is the sum of the elements of packetCounts*/
	int[] packetCounts;
	
	/** The total number of packets, i.e. the sum of the elements of packetCounts. */
	int totalPacketCount;

	public PacketItemAllocator(){};
	
	/** Constructor for bins with uniformly distributed item counts. <br>
	 * Possible cases:<ol> 
	 * <li> items == packets: items evenly divided by packets. </li>
	 * <li> items >= packets: items not evenly divided. </li>
	 * <li> items <  packets: some packets contain zero items. </li></ol> */
	public PacketItemAllocator(int totalItems, int totalPackets){

		totalPacketCount = totalPackets;
		/* If there are more items to allocate than there are packets, allocate
		 * multiple items to some of the packets. 
		 * This will probably be the most common situation*/
		if((totalItems >= totalPackets && totalPackets > 0)){
			
			/* How many main bins should there be? */
			int remainder = (int)(totalItems % totalPackets);
			
			/* What is the number of beetles per remainder packet?
			 * The number of beetles per remainder packet, if applicable, 
			 * will be is one greater than this.
			 * NOTE:  division of ints truncates down, this is the desired behavior. */
			int beetlesPerPacket = totalItems / totalPackets;
			
			/* If the remainder is zero, the beetles are divided evenly. */
			if(remainder == 0){
				packetCounts = new int[]{totalPackets};
				itemsPerPacket = new int[]{beetlesPerPacket};
			} else
			/* Otherwise two sizes of packets are needed */
			{
				packetCounts = new int[] {remainder, totalPackets - remainder};
				itemsPerPacket = new int[] {beetlesPerPacket + 1, beetlesPerPacket};
			}
		} else
		if(totalItems < totalPackets)
		{
			packetCounts = new int[] {(int)totalItems, totalPackets - (int)totalItems};
			itemsPerPacket = new int[]{1, 0};
		}
	}
	
	/** Get the number of items in the input packet number. */
	public int retrieve(int packet){
		
		if(packet > totalPacketCount){
			throw new IllegalArgumentException(
				"Requested index (" + packet + ") greater than the number of packets (" +
				totalPacketCount + ") in the container.");
		}
		
		int cumulativePacketCount = 0;
		int cumulativePacketCountNext;
		int itemCount = itemsPerPacket[0];
		
		for(int i = 0; i < packetCounts.length - 1; i++){

			/* If the requested packet number is greater than the cumulative sum of the packet counts but
			 * less than the cumulative sum including the next group of packets of the current group of packets,
			 * return the number of items in the current group's packets.	 */
			cumulativePacketCountNext = cumulativePacketCount + packetCounts[i];
			if(packet < cumulativePacketCountNext){
				itemCount = itemsPerPacket[i];
				break;
			}
			cumulativePacketCount = cumulativePacketCountNext;
			itemCount = itemsPerPacket[i + 1];
		}
		return itemCount;
	}
	
	public int getTotalPacketCount() {return totalPacketCount;}
	public void setTotalPacketCount(int nBins) {this.totalPacketCount = nBins;}
	public int[] getnItems() {return itemsPerPacket;}
	public void setnItems(int[] nItems) {this.itemsPerPacket = nItems;}
	public int[] getPacketCounts() {return packetCounts;}
	public void setPacketCounts(int[] nPackets) {this.packetCounts = nPackets;}
}
