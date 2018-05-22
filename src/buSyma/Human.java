package buSyma;

import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.SimUtilities;
public class Human {
	private ContinuousSpace <Object> space;
	private Grid <Object> grid;
	private GridPoint destination;
	private Context<Object> context;
	public Human(ContinuousSpace<Object> space, Grid<Object> grid, GridPoint dest, Context<Object> context) {
		this.space = space;
		this.grid = grid;
		this.destination = dest;
		this.context = context;
	}
	
	
	@ScheduledMethod(start=1, interval=1)
	public void moveTowardsDestination() {
		//System.out.println("Current location : " + grid.getLocation(this) + "\nDestination is : " + destination);
		//System.out.println(arrived);
		if (grid.getLocation(this).getX() != destination.getX() && grid.getLocation(this).getY() != destination.getY()) {
			NdPoint myPoint = space.getLocation(this);
			NdPoint otherPoint = new NdPoint(destination.getX() + 0.5f, destination.getY() + 0.5f);
			double angle = SpatialMath.calcAngleFor2DMovement(space, myPoint, otherPoint);
			space.moveByVector(this, 0.5f, angle, 0);
			myPoint = space.getLocation(this);
			grid.moveTo(this, (int)myPoint.getX(), (int)myPoint.getY());
		}
		else {
			context.remove(this);
			BuSymaBuilder.addHuman(context, space, grid);
		}
	}
}
