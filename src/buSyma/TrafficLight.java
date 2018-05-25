package buSyma;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class TrafficLight {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	public boolean red;
	public int timer = 0;
	public TrafficLight(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid = grid;
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
