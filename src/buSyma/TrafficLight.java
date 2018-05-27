package buSyma;

import repast.simphony.engine.schedule.ScheduledMethod;

public class TrafficLight {
	public boolean red;
	public int timer = 0;
	public TrafficLight() {
		this.red = true;
	}
	
	@ScheduledMethod(start=7, interval=7)
	public void light() {
		red = !red;
		timer = 0;
	}
	
	@ScheduledMethod(start=0, interval=1)
	private void increment() {
		timer++;
	}
}
