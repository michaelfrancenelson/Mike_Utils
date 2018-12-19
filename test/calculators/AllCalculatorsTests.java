package calculators;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestGeneral.class, 
	TestMPBCalculations.class})
public class AllCalculatorsTests {

}
