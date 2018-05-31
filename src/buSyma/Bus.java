package buSyma;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class Bus extends Moving{
	public boolean shouldMove = true;
	private int timeStopped = 0;
	private GridPoint spawn;
	public int passengers = 0;
	public Bus(ContinuousSpace<Object> space, Grid<Object> grid, GridPoint dest, Context<Object> context, ArrayList<String> map, GridPoint spawn) {
		this.space = space;
		this.grid = grid;
		this.destination = dest;
		this.context = context;
		this.map = map;
		this.speed = 1f;
		this.spawn = spawn;
	}

	public boolean canCross(TrafficLight t) {
		t.busses.add(this);
		return (!t.red);
	}
	public void despawn() {
		BuSymaBuilder.addBus(context, space, grid, map, spawn, destination);
		context.remove(this);
	}
	
	public boolean shouldStop() {
		if (!shouldMove) {
			timeStopped++;
			if (timeStopped > 3) {
				shouldMove = true;
				timeStopped = 0;
				return false;
			}
			return true;
		}
		GridPoint pt = grid.getLocation(this);
		GridCellNgh<BusShelter> nghShelterCreator = new GridCellNgh<BusShelter>(grid, pt, BusShelter.class, 1, 1);
		List<GridCell<BusShelter>> shelterGridCells = nghShelterCreator.getNeighborhood(false);
		for (GridCell<BusShelter> shelter : shelterGridCells) {
			if (shelter.items().iterator().hasNext()) {
				BusShelter s = shelter.items().iterator().next();
				GridPoint ps = grid.getLocation(s);
				if (Math.abs(ps.getX() - pt.getX()) + Math.abs(ps.getY() - pt.getY()) == 1) {
					shouldMove = false;
					return true;
				}
			}
		}
		return false;
	}
	
	@ScheduledMethod(start=1, interval=1)
	public void moveTowardsDestination() {
		GridPoint pt = grid.getLocation(this);
		Object t = grid.getObjectAt(pt.getX(), pt.getY());
		if (t instanceof TrafficLight && ((TrafficLight)t).busses.contains(this))
			((TrafficLight)t).busses.remove(this);
		if (super.computeNextGoal())
			return;
		Object o = grid.getObjectAt(currentGoal.getX(), currentGoal.getY());
		if (o instanceof TrafficLight && !canCross((TrafficLight)o))
			return;
		if (!shouldStop())
			super.moveTowardsDestination();
	}
}
