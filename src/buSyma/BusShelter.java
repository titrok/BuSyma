package buSyma;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class BusShelter {
	private ContinuousSpace <Object> space;
	private Grid <Object> grid;
	public BusShelter(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.grid = grid;
		this.space = space;
	}
}
