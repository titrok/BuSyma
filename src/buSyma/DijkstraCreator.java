package buSyma;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class DijkstraCreator {
	public GridPoint source;
	private GridPoint[][] predecessors;
	private int[][] distance;
	private int width, height;
	private ArrayList<String> map;
	private Grid<Object> grid;
	private Stack<GridPoint> way = null;
	private Set<GridPoint> seen = new HashSet<GridPoint>(); 
	private Object object;
	public DijkstraCreator(GridPoint source , ArrayList<String> map, Grid<Object> grid, Object o) {
		this.source = source;
		this.map = map;
		this.height = map.size();
		this.width = map.get(0).length();
		this.grid = grid;
		this.object = o;
		initialize();
	}
	
	private void initialize() {
		predecessors = new GridPoint[width][height];
		distance = new int[width][height];
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
			{
				distance[i][j] = Integer.MAX_VALUE;
				predecessors[i][j] = null;
			}
		distance[source.getX()][source.getY()] = 0;
	}
	
	private boolean isInRange(GridPoint g) {
		return g.getX() < width && g.getX() >= 0 && g.getY() < height && g.getY() >= 0;
	}
	
	private boolean isPracticable(GridPoint g) {
		char c = getChar(g);
		if (this.object instanceof Bus)
			return c != 'S' && c != 'X';
		return c != 'R' && c != 'H' && c != 'V';
	}
	
	private char getChar(GridPoint ngh) {
		
		char c = map.get(ngh.getY()).charAt(ngh.getX());
		return c;
	}
	
	private void update(GridPoint g, GridPoint ngh) {
		if (isInRange(ngh) && isPracticable(ngh)) {
			int dist = Math.abs(ngh.getX() - g.getX()) + Math.abs(ngh.getY() - g.getY());
				if (distance[ngh.getX()][ngh.getY()] > distance[g.getX()][g.getY()] + dist) {
					distance[ngh.getX()][ngh.getY()] = distance[g.getX()][g.getY()] + dist;
					predecessors[ngh.getX()][ngh.getY()] = g;
			}
		}
	}
	
	private boolean parcours(GridPoint g, GridPoint destination) {
		seen.add(g);
		GridCellNgh<Object> nghCreator = new GridCellNgh<Object>(grid, g, Object.class, 1, 1);
		List<GridCell<Object>> gridCells = nghCreator.getNeighborhood(true);
		for (GridCell<Object> cell : gridCells) {
			update(g, cell.getPoint());
		}
		int minDist = Integer.MAX_VALUE;
		GridPoint next = null;
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++) {
				if (distance[i][j] < minDist && !seen.contains(grid.getLocation(grid.getObjectAt(i,j))) &&  isPracticable(grid.getLocation(grid.getObjectAt(i,j)))) {
					minDist = distance[i][j];
					next = grid.getLocation(grid.getObjectAt(i,j));
				}
			}
		if (next == null)
			return true;
		return (parcours(next, destination));
	}
	
	public GridPoint compute(GridPoint destination) {
		if (way == null) {
			way = findWay(destination);
		}
		return way.pop();
	}
	
	public boolean arrived() {
		return way != null && way.isEmpty();
	}
	
	private Stack<GridPoint> findWay(GridPoint destination) {
		Stack<GridPoint> s = new Stack<GridPoint>();
		if (!parcours(source, destination))
		{
			System.out.println("No way was found from source to destination");
			return s;
		}
		s.push(destination);
		GridPoint prev = predecessors[destination.getX()][destination.getY()];
		while (prev != source && prev != null) {
			s.push(prev);
			prev = predecessors[prev.getX()][prev.getY()];
		}	
		return s;
	}
}
