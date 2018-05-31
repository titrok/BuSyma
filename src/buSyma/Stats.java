package buSyma;

import repast.simphony.engine.schedule.ScheduledMethod;

public class Stats {

	private int nbTicks = 0;
	public static int nbHumans = 0;
	
	public Stats() {
	}
	
	@ScheduledMethod(start=1, interval=1)
	public void percentage() {
		nbTicks += 1;
		if (nbTicks % 1000 == 0)
			System.out.println("Number of Humans arrived on tick : " + nbTicks + " = " + nbHumans);
	}
}
