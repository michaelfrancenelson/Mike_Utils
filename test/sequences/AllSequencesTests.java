package sequences;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	TestPacketItemAllocator.class, 
	TestSequences.class })
public class AllSequencesTests {

}
