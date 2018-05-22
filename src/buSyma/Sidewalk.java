/**
 * 
 */
package buSyma;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

/**
 * @author titrok
 *
 */
public class Sidewalk {
	private ContinuousSpace <Object> space;
	private Grid <Object> grid;
	public Sidewalk(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid = grid;
	}
}
