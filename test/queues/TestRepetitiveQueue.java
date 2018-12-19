package queues;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestRepetitiveQueue {

	RepetitiveQueue rq;
	int nPackets, totalCount, nDivisions, nPacketsPerDivision;


	@Test
	public void test() {



		nDivisions = 3;
		nPacketsPerDivision = 4;
		totalCount = 33;
		rq = new RepetitiveQueue(nDivisions, nPacketsPerDivision, totalCount);
		for(int j = 0; j < 3; j++)
		{
			for(int i = 0; i < 3; i++)
				assertEquals(3, rq.next());
			assertEquals(2, rq.next());
		}
		assertEquals(0, rq.next());

		/* Test without groups of groups of packets. */

		nPackets = 4; 
		totalCount = 20;

		rq = new RepetitiveQueue(nPackets, totalCount);
		for(int i = 0; i < nPackets; i++)
			assertEquals(5, rq.next());
		assertEquals(0, rq.next());		

		totalCount = 22;
		rq = new RepetitiveQueue(nPackets, totalCount);
		for(int i = 0; i < 2; i++)
			assertEquals(6, rq.next());
		for(int i = 0; i < 2; i++)
			assertEquals(5, rq.next());
		assertEquals(0, rq.next());	

		totalCount = 3;
		rq = new RepetitiveQueue(nPackets, totalCount);
		for(int i = 0; i < 3; i++)
			assertEquals(1, rq.next());
		for(int i = 0; i < 1; i++)
			assertEquals(0, rq.next());
		assertEquals(0, rq.next());	

	}

}
