package buSyma;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
public class Human extends Moving{
	public Human(ContinuousSpace<Object> space, Grid<Object> grid, GridPoint dest, Context<Object> context, ArrayList<String> map) {
		this.space = space;
		this.grid = grid;
		this.destination = dest;
		this.context = context;
		this.map = map;
		this.speed = 0.5f;
	}	
	
	public void despawn() {
		context.remove(this);
		BuSymaBuilder.addHuman(context, space, grid, map);
	}
	
	public boolean canCross(TrafficLight t) {
		return (t.red && t.timer < 5);
	}
	
	public boolean canCross(Crossing c) {
		GridCellNgh<TrafficLight> nghSpawnCreator = new GridCellNgh<TrafficLight>(grid, currentGoal, TrafficLight.class, 1, 1);
		List<GridCell<TrafficLight>> lightGridCells = nghSpawnCreator.getNeighborhood(true);
		for (GridCell<TrafficLight> light : lightGridCells)
			if (light.items().iterator().hasNext() && !canCross(light.items().iterator().next()))
				return false;
		return true;
	}
}
