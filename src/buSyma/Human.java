package buSyma;

import java.util.ArrayList;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
public class Human {
	private ContinuousSpace <Object> space;
	private Grid <Object> grid;
	private GridPoint destination;
	private Context<Object> context;
	private DijkstraCreator way;
	private ArrayList<String> map;
	private GridPoint currentGoal;
	public Human(ContinuousSpace<Object> space, Grid<Object> grid, GridPoint dest, Context<Object> context, ArrayList<String> map) {
		this.space = space;
		this.grid = grid;
		this.destination = dest;
		this.context = context;
		this.map = map;
	}
	
	public void initDijkstra() {
		this.way = new DijkstraCreator(grid.getLocation(this), map, grid);
		currentGoal = grid.getLocation(this);
	}
	
	@ScheduledMethod(start=1, interval=1)
	public void moveTowardsDestination() {
		//System.out.println("Current location : " + grid.getLocation(this) + "\nDestination is : " + destination);
		//System.out.println(arrived);
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
		space.moveByVector(this, 0.5f, angle, 0);
		myPoint = space.getLocation(this);
		grid.moveTo(this, (int)myPoint.getX(), (int)myPoint.getY());
	}
}
