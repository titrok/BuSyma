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
	abstract void despawn();
	abstract boolean canCross(TrafficLight t);
	abstract boolean canCross(Crossing c);
	
	@ScheduledMethod(start=1, interval=1)
	public void moveTowardsDestination() {
		if (grid.getLocation(this).getX() == currentGoal.getX() && grid.getLocation(this).getY() == currentGoal.getY()) {
			if (way.arrived()) {
				despawn();
				return;
			} else {
				currentGoal = way.compute(destination);
			}
		}
		NdPoint myPoint = space.getLocation(this);
		Object o = grid.getObjectAt(currentGoal.getX(), currentGoal.getY());
		if (o instanceof Crossing && !canCross((Crossing)o))
			return;
		if (o instanceof TrafficLight && !canCross((TrafficLight)o))
			return;
		NdPoint otherPoint = new NdPoint(currentGoal.getX() + 0.5f, currentGoal.getY() + 0.5f);
		double angle = SpatialMath.calcAngleFor2DMovement(space, myPoint, otherPoint);
		space.moveByVector(this, this.speed, angle, 0);
		myPoint = space.getLocation(this);
		grid.moveTo(this, (int)myPoint.getX(), (int)myPoint.getY());
	}
}
