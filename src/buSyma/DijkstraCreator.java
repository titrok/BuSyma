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
	
	public DijkstraCreator(GridPoint source , ArrayList<String> map, Grid<Object> grid) {
		this.source = source;
		this.map = map;
		this.height = map.size();
		this.width = map.get(0).length();
		this.grid = grid;
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
		System.out.println(g);
		char c = getChar(g);
		System.out.println(c);
		return c != 'R';
	}
	
	private char getChar(GridPoint ngh) {
		return map.get(height - ngh.getY() - 1).charAt(ngh.getX());
	}
	
	private boolean update(GridPoint g, GridPoint ngh) {
		if (isInRange(ngh) && isPracticable(ngh)) {
			char c = getChar(ngh);
			if (c == 'C' || c == 'S' || c == 'X') {
				if (distance[ngh.getX()][ngh.getY()] > distance[g.getX()][g.getY()] + 1) {
					distance[ngh.getX()][ngh.getY()] = distance[g.getX()][g.getY()] + 1;
					predecessors[ngh.getX()][ngh.getY()] = g;
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isFinished() {
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				if (isPracticable(grid.getLocation(grid.getObjectAt(i,j))) && !seen.contains(grid.getLocation(grid.getObjectAt(i,j))))
					return false;
		return true;
	}
	
	private boolean parcours(GridPoint g, GridPoint destination) {
		seen.add(g);
		if (isFinished())
			return true;
		System.out.println("Current point : " + g + "Destination : " + destination);
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
			System.out.println("DIJKSTRA RESULT");
			System.out.println("Way : " + way);
		}
		return way.pop();
	}
	
	public boolean arrived() {
		return way != null && way.isEmpty();
	}
	
	private Stack<GridPoint> findWay(GridPoint destination) {
		Stack<GridPoint> s = new Stack<GridPoint>();
		if (!parcours(source, destination))
			System.out.println("No way was found from source to destination");
		s.push(destination);
		GridPoint next = predecessors[destination.getX()][destination.getY()];
		while (next != null) {
			s.push(next);
			next = predecessors[next.getX()][next.getY()];
		}
		return s;
	}
}
