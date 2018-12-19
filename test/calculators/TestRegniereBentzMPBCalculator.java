package calculators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

 /** @author michaelfrancenelson */

public class TestRegniereBentzMPBCalculator {

	double coldHardening, gain, loss, survival;
	double[] state;
	
	RegniereBentzMPBCalculator calc;
	@Before
	public void setup() throws IOException, ParseException {
		state = new double[4];
	}
	
	/** Tests for equation 7 */
	@Test
	public void testColdHardening(){
		
		state[0] = 0.49;
		state[1] = 0.01;
		state[2] = 0.01;
		
		gain = 0;
		loss = 0;
		coldHardening = 0.51;
		
		/* Cold hardening should keep increasing as long as tepmperatures are cold enough. */
		/* Test at a mean temperature of -15 with a range of 8 */
		double tau = -25d;
		double range = 8d;
		
		for(int i = 0; i < 100; i++){
			gain = RegniereBentzMPBCalculator.gain(range, tau, 
					RegniereBentzMPBCalculator.supercoolingTempGain(coldHardening));
			loss = RegniereBentzMPBCalculator.loss(range, tau, 
					RegniereBentzMPBCalculator.supercoolingTempLoss(coldHardening));
			double old = coldHardening;
			coldHardening = RegniereBentzMPBCalculator.currentColdHardening(gain, loss, coldHardening, false);
			assertTrue(old < coldHardening);
		}

		/* Cold hardening should decrease when temperatures are warmer. */
		tau = 25d;
		for(int i = 0; i < 100; i++){
			gain = RegniereBentzMPBCalculator.gain(range, tau, 
					RegniereBentzMPBCalculator.supercoolingTempGain(coldHardening));
			loss = RegniereBentzMPBCalculator.loss(range, tau, 
					RegniereBentzMPBCalculator.supercoolingTempLoss(coldHardening));
			double old = coldHardening;
			coldHardening = RegniereBentzMPBCalculator.currentColdHardening(gain, loss, coldHardening, false);
			assertTrue(old > coldHardening);
		}
	}
	
	
	/** Tests for equations 8 and 9 */
	@Test
	public void testProportions(){
		
		
		/* All individuals should be in summer state when cold hardening state = 0. 
		 * All individuals should be in summer state when cold hardening state <= lambda0.
		 * No individuals should be in summer state when cold hardening state = 0.5.
		 * No individuals should be in summer state when cold hardening state > 0.5*/
		assertEquals(1d, RegniereBentzMPBCalculator.proportion1(0d), 0.0001);
		assertEquals(0d, RegniereBentzMPBCalculator.proportion1(0.5), 0.0001);
		assertEquals(0d, RegniereBentzMPBCalculator.proportion1(0.5001), 0.0001);
		assertEquals(0d, RegniereBentzMPBCalculator.proportion1(1d), 0.0001);
		
		/* All individuals should be in winter state when cold hardening state = 1. 
		 * All individuals should be in winter state when cold hardening state >= lambda01
		 * No individuals should be in winter state when cold hardening state = 0.5.
		 * No individuals should be in winter state when cold hardening state < 0.5*/
		assertEquals(1d, RegniereBentzMPBCalculator.proportion3(1d), 0.0001);
		assertEquals(0d, RegniereBentzMPBCalculator.proportion3(0.5), 0.0001);
		assertEquals(0d, RegniereBentzMPBCalculator.proportion3(0.4999), 0.0001);
		assertEquals(0d, RegniereBentzMPBCalculator.proportion3(0d), 0.0001);
		
		/* All individuals should be in spring/fall state when cold hardening state = 0.5 */
		assertEquals(1d, RegniereBentzMPBCalculator.proportion2(0.5), 0.0001);
		
		/* Median SCP should be:
		 * 	alpha1 in state 1
		 * 	alpha2 in state 2
		 * 	alpha3 in state 3 */
		
		assertEquals(RegniereBentzMPBCalculator.ModelParameters.meanSCPAlpha1, RegniereBentzMPBCalculator.medianLethalTemperature(0d), 0.0001);
		assertEquals(RegniereBentzMPBCalculator.ModelParameters.meanSCPAlpha2, RegniereBentzMPBCalculator.medianLethalTemperature(0.5), 0.0001);
		assertEquals(RegniereBentzMPBCalculator.ModelParameters.meanSCPAlpha3, RegniereBentzMPBCalculator.medianLethalTemperature(1d), 0.0001);
	}

	
	/** Tests for equation 10*/
	@Test
	public void testSurvival(){
		
		double[] temps;
		
		LocalDate today = LocalDate.of(2000, 1, 1);
		
		/*Survival probability should be higher if temperature decreases gradually. */
		state[3] = 1d;
		state[0] = 0d;
		temps  = new double[] {20d, 30d};
		RegniereBentzMPBCalculator.updateState(temps, today, state);
		

		state[3] = 1d;
		state[0] = 0d;
		/* Abrupt change */
		temps  = new double[] {-20d, -10d};
		RegniereBentzMPBCalculator.updateState(temps, today, state);
		double survivalAbrupt = state[3];
		
		/* Gradual change */
		state[3] = 1d;
		state[0] = 0d;
		
		temps  = new double[] {20d, 30d};
		RegniereBentzMPBCalculator.updateState(temps, today, state);
		for(int i = 0; i < 40; i++){
			temps[0]--;
			temps[1]--;
			RegniereBentzMPBCalculator.updateState(temps, today, state);		}
		double survivalGradual = state[3];
		
		assertTrue(survivalGradual > survivalAbrupt);
		
		/* Survival probability should continue to decrease if temperature is low. */
		/* Start with no cold hardening. */
		/* Set survival to 1 on August 1st. */
		state[3] = 1d;
		state[0] = 0d;

		temps = new double[] {20d, 30d};
		for(int i = 0; i < 20; i++){
			temps[0]--;
			temps[1]--;
			double oldSurvival = state[3];
			RegniereBentzMPBCalculator.updateState(temps, today, state);
			assertTrue(oldSurvival >= state[3]);
		}

		for(int i = 0; i < 40; i++){
			temps[0]--;
			temps[1]--;
			double oldSurvival = state[3];
			RegniereBentzMPBCalculator.updateState(temps, today, state);
			assertTrue(oldSurvival >= state[3]);
		}
	}
	
}
