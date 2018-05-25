package buSyma;

import java.util.ArrayList;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public abstract class Moving {
	protected ContinuousSpace <Object> space;
	protected Grid <Object> grid;
	protected GridPoint destination;
	protected DijkstraCreator way;
	protected GridPoint currentGoal;
	protected Context<Object> context;
	protected ArrayList<String> map;
	protected float speed;
	public void initDijkstra() {
		this.way = new DijkstraCreator(grid.getLocation(this), map, grid, this);
		currentGoal = grid.getLocation(this);
	}
	
	@ScheduledMethod(start=1, interval=1)
	public void moveTowardsDestination() {
		if (grid.getLocation(this).getX() == currentGoal.getX() && grid.getLocation(this).getY() == currentGoal.getY()) {
			if (way.arrived()) {
				context.remove(this);
				BuSymaBuilder.addHuman(context, space, grid, map);
				return;
			} else {
				currentGoal = way.compute(destination);
			}
		}
		NdPoint myPoint = space.getLocation(this);
		NdPoint otherPoint = new NdPoint(currentGoal.getX() + 0.5f, currentGoal.getY() + 0.5f);
		double angle = SpatialMath.calcAngleFor2DMovement(space, myPoint, otherPoint);
		space.moveByVector(this, this.speed, angle, 0);
		myPoint = space.getLocation(this);
		grid.moveTo(this, (int)myPoint.getX(), (int)myPoint.getY());
	}
}
