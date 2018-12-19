package sequences;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomEngine;
import sequeuces.PacketItemAllocator;

 /** @author michaelfrancenelson */

public class TestPacketItemAllocator {

	RandomEngine re = new MersenneTwister(12345);
	Uniform unif = new Uniform(re);
	
	@Test
	public void testLightweightAllocateItemsUniform() {
		
		int totalPackets;
		int totalItems;
		
		PacketItemAllocator output = null;

		for(totalPackets = 7; totalPackets < 8; totalPackets++){
			for(totalItems = 1; totalItems < 3001; totalItems++){
				
				output = new PacketItemAllocator(totalItems, totalPackets);

				
				/* If items are evenly distributed to bins, 
				 * there should only be one entry in the container: */
				
				if(totalItems > totalPackets)
				{
					
					if(totalItems % totalPackets == 0)
					{
						assertEquals(1, output.getPacketCounts().length);
						assertEquals(1, output.getnItems().length);
						assertEquals(totalItems, output.getPacketCounts()[0] * output.getnItems()[0]);

					} else
					{
					/* The sum of the counts of packets * number of items per packet should
					 * be equal to the input countOfItems.
					 */
					
						assertEquals(totalItems, output.getPacketCounts()[0] * 
							output.getnItems()[0] + output.getPacketCounts()[1] * output.getnItems()[1]);
					}
				} else
				/* If there are fewer items than bins, the output number of bins should
				 * be equal to the number of items, and the items per bin should be 1. */
				 if(totalItems < totalPackets)
				 {
					assertEquals(2, output.getPacketCounts().length);
					assertEquals(2, output.getnItems().length);
					assertEquals(totalItems, (long)output.getPacketCounts()[0]);
					assertEquals(totalItems, output.getPacketCounts()[0] * output.getnItems()[0]);
				 }	else
				/* If there are zero, or fewer, items and/or packets
				 * requested an empty container should be returned */
				if(totalItems < 1 || totalPackets < 1)
				{
					assertEquals(1, output.getPacketCounts().length);
					assertEquals(1, output.getnItems().length);
					assertEquals(0, output.getTotalPacketCount());
				} 				
			}
		}		
	}
	
	/** Test that the correct group corresponding to the input index is retrieved. */
	@Test
	public void testRetrieve(){
		
		int nGroups = 100;
		for(int nTests = 0; nTests < 10; nTests++){
			
			int[] groupCounts = new int[nGroups];
			int sumOfGroupCounts = 1;
			groupCounts[0] = 1;
			for(int i = 1; i < nGroups; i++){
				groupCounts[i] = unif.nextIntFromTo(0, 9999);
				sumOfGroupCounts += groupCounts[i];
			}
			
			PacketItemAllocator testCounts = new PacketItemAllocator();
			
			testCounts.setnItems(new int[nGroups]);
			testCounts.setPacketCounts(new int[nGroups]);
			
			for(int i = 0; i < nGroups; i++){
				testCounts.getnItems()[i] = i + 100;
				testCounts.getPacketCounts()[i] = groupCounts[i];
				testCounts.setTotalPacketCount(sumOfGroupCounts);
			}
			
			int packetTracker = 0;
			for(int i = 0; i < nGroups; i++){
				for(int j = 0; j < groupCounts[i]; j++){
					
					assertEquals(testCounts.retrieve(packetTracker), i + 100);
					packetTracker++;
					
				}
			}
		}	
	}
}
