package buSyma;

import java.util.ArrayList;

import repast.simphony.context.Context;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class Bus extends Moving{
	public Bus(ContinuousSpace<Object> space, Grid<Object> grid, GridPoint dest, Context<Object> context, ArrayList<String> map) {
		this.space = space;
		this.grid = grid;
		this.destination = dest;
		this.context = context;
		this.map = map;
		this.speed = 1f;
	}

	public boolean canCross(TrafficLight t) {
		return (!t.red && t.timer < 5);
	}
	
	public boolean canCross(Crossing c) {
		return true;
	}
	public void despawn() {
		context.remove(this);
		BuSymaBuilder.addBus(context, space, grid, map);
	}
}
