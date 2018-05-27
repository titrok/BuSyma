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
	public void despawn() {
		context.remove(this);
		BuSymaBuilder.addBus(context, space, grid, map);
	}
	
	public boolean shouldStop() {
		GridPoint pt = grid.getLocation(this);
		GridCellNgh<BusShelter> nghShelterCreator = new GridCellNgh<BusShelter>(grid, pt, BusShelter.class, 1, 1);
		List<GridCell<BusShelter>> shelterGridCells = nghShelterCreator.getNeighborhood(false);
		for (GridCell<BusShelter> shelter : shelterGridCells) {
			if (!shelter.items().iterator().hasNext())
				return false;
			GridPoint s = shelter.getPoint();
			if (Math.abs(s.getX() - pt.getX()) + Math.abs(s.getY() - pt.getY()) == 1)
				return true;
		}
		return false;
	}
	
	@ScheduledMethod(start=1, interval=1)
	public void moveTowardsDestination() {
		if (super.computeNextGoal())
			return;
		Object o = grid.getObjectAt(currentGoal.getX(), currentGoal.getY());
		if (o instanceof TrafficLight && !canCross((TrafficLight)o))
			return;
		if (!shouldStop())
			super.moveTowardsDestination();
	}
}
