package buSyma;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class Crossing {
	private ContinuousSpace <Object> space;
	private Grid <Object> grid;
	public Crossing(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.grid = grid;
		this.space = space;
	}
}
