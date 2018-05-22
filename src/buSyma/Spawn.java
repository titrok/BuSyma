package buSyma;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class Spawn {
	private ContinuousSpace <Object> space;
	private Grid <Object> grid;
	public Spawn(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid = grid;
	}
}
