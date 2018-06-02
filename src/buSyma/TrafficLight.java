package buSyma;

import java.util.HashSet;
import java.util.Set;

import repast.simphony.engine.schedule.ScheduledMethod;

public class TrafficLight {
	public boolean red;
	public Set<Human> pedestrians = new HashSet<Human>();
	public Set<Bus> busses = new HashSet<Bus>() ;
	private int timer = 0;
	private char method = 'A';
	public TrafficLight(char method) {
		this.red = true;
		this.method = method;
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
	public void methodC() {
		if (busses.size() > 0)
			red = false;
		else
			red = true;
	}
	
	public void methodA() {
		timer++;
		if (timer > 5)
			light();
	}
	
	@ScheduledMethod(start=1, interval=1)
	public void increment() {
		if (method == 'A')
			methodA();
		else if (method == 'B')
			methodB();
		else
			methodC();
	}
}
