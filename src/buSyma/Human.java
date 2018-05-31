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
public class Human extends Moving{
	private boolean isCrossing = false;
	public int timer = 0;
	public boolean dead = false;
	public Human(ContinuousSpace<Object> space, Grid<Object> grid, GridPoint dest, Context<Object> context, ArrayList<String> map) {
		this.space = space;
		this.grid = grid;
		this.destination = dest;
		this.context = context;
		this.map = map;
		this.speed = 0.5f;
	}	

	public void despawn() {
		if (grid.getObjectAt(destination.getX(), destination.getY()) instanceof BusShelter) {
			GridCellNgh<Bus> nghBusCreator = new GridCellNgh<Bus>(grid, currentGoal, Bus.class, 1, 1);
			List<GridCell<Bus>> busGridCells = nghBusCreator.getNeighborhood(true);
			int count = 0;
			for (GridCell<Bus> bus : busGridCells) {
				if (bus.items().iterator().hasNext()) {
					count++;
					Bus b = bus.items().iterator().next();
					if (b.shouldMove)
						return;
					else
						b.passengers += 1;
				}
			}
			if (count == 0)
				return;
		}
		Stats.nbHumans = Stats.nbHumans + 1;
		context.remove(this);
		BuSymaBuilder.addHuman(context, space, grid, map);
	}
	
	public boolean canCross(TrafficLight t) {
		t.pedestrians.add(this);
		return (t.red);
	}
	
	public boolean canCross(Crossing c) {
		if (isCrossing) {
			return true;
		}
		GridCellNgh<TrafficLight> nghLightCreator = new GridCellNgh<TrafficLight>(grid, currentGoal, TrafficLight.class, 1, 1);
		List<GridCell<TrafficLight>> lightGridCells = nghLightCreator.getNeighborhood(true);
		for (GridCell<TrafficLight> light : lightGridCells)
			if (light.items().iterator().hasNext() && !canCross(light.items().iterator().next()))
				return false;
		isCrossing = true;
		return true;
	}

	@ScheduledMethod(start=1, interval=1)
	public void moveTowardsDestination() {
		timer++;
		GridPoint pt = grid.getLocation(this);
		Object t = grid.getObjectAt(pt.getX(), pt.getY());
		if (t instanceof TrafficLight && ((TrafficLight) t).pedestrians.contains(this))
			((TrafficLight)t).pedestrians.remove(this);
		if (super.computeNextGoal())
			return;
		Object o = grid.getObjectAt(currentGoal.getX(), currentGoal.getY());
		if (o instanceof Crossing && !canCross((Crossing)o))
			return;
		isCrossing = o instanceof Crossing;
		if (o instanceof TrafficLight && !canCross((TrafficLight)o))
			return;
		super.moveTowardsDestination();
	}
}
