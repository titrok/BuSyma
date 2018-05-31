package buSyma;

import java.util.HashSet;
import java.util.Set;

import repast.simphony.engine.schedule.ScheduledMethod;

public class TrafficLight {
	public boolean red;
	public Set<Human> pedestrians = new HashSet<Human>();
	public Set<Bus> busses = new HashSet<Bus>() ;
	private int timer = 0;
	private boolean methodA = true;
	public TrafficLight(boolean methodA) {
		this.red = true;
		this.methodA = methodA;
	}
	
	public boolean shouldLight() {
		int passengers = 0;
		for (Bus b : busses) {
			passengers += b.passengers;
		}
		int ped = pedestrians.size();
		return passengers >= ped;
	}
	
	public void light() {
		red = !red;
		timer = 0;
	}
	
	public void methodB() {
		timer++;
		if (red && shouldLight()) {
			light();
		}
		if (!red && !shouldLight()) {
			light();
		}
		if (timer > 5)
			light();
	}
	
	public void methodA() {
		timer++;
		if (timer > 5)
			light();
	}
	
	@ScheduledMethod(start=1, interval=1)
	public void increment() {
		if (methodA)
			methodA();
		else
			methodB();
	}
}
