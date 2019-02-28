package allTests;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import calculators.AllCalculatorsTests;
import coordinates.AllCoordinateTests;
import discrete.AllDiscreteTests;
import interpolations.AllInterpolationsTests;
import queues.AllQueuesTests;
import sampling.AllTestsSampling;
import search.AllSearchTests;
import sequences.AllSequencesTests;
import sort.AllTestsSorts;
import vectors.AllTestsVectors;

@RunWith(Suite.class)

@SuiteClasses({
	AllCalculatorsTests.class,
	AllCoordinateTests.class,
	AllDiscreteTests.class,
	AllInterpolationsTests.class,
	AllQueuesTests.class,
	AllSearchTests.class,
	AllSequencesTests.class,
	AllTestsVectors.class,
	AllTestsSampling.class,
	AllTestsSorts.class})

public class AllTests {

}
