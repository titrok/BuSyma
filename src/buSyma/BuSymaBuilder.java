package buSyma;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.StrictBorders;
import repast.simphony.util.SimUtilities;

/**
 * @author titrok
 *
 */
public class BuSymaBuilder implements ContextBuilder<Object> {
	
	private int height;
	private int width;
	
	
	public static void add(Context<Object> context, ContinuousSpace<Object> space, Grid<Object> grid, Object obj, float x, float y) {
		CustomAdder ca = (CustomAdder)space.getAdder();
		ca.x = x + 0.5f;
		ca.y = y + 0.5f;
		context.add(obj);
		grid.moveTo(obj, (int)ca.x, (int)ca.y);
	}
	public ArrayList<String> fileToList(String str)
	{
		Scanner s = null;
		try {
			s = new Scanner(new File(str));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ArrayList<String> list = new ArrayList<String>();
		while (s.hasNext()){
			list.add(s.next());
		}
		s.close();
		return list;
	}
	@Override
	public Context<Object> build(Context<Object> context) {
		context.setId("BuSyma");
		String mapName = RunEnvironment.getInstance().getParameters().getString("mapName");
		ArrayList<String> list = fileToList(System.getProperty("user.dir") + "/misc/" + mapName);
		Collections.reverse(list);
		this.height = list.size();
		this.width = list.get(0).length();
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace("space", context, new CustomAdder(), new repast.simphony.space.continuous.StrictBorders(), width, height);
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid("grid", context, new GridBuilderParameters<Object>(new StrictBorders(), new SimpleGridAdder<Object>(), true, width, height));
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				if (list.get(y).charAt(x) == 'X')
					add(context, space, grid, new Sidewalk(), x, y);
				if (list.get(y).charAt(x) == 'R')
					add(context, space, grid, new Road(), x, y);
				if (list.get(y).charAt(x) == 'S')
					add(context, space, grid, new Spawn(), x, y);
				if (list.get(y).charAt(x) == 'C')
					add(context, space, grid, new Crossing(), x, y);
				if (list.get(y).charAt(x) == 'A')
					add(context, space, grid, new BusShelter(), x, y);
				if (list.get(y).charAt(x) == 'T')
					add(context, space, grid, new TrafficLight(), x, y);
			}
		}
		
		addHuman(context, space, grid, list);
		addBus(context, space, grid, list, new GridPoint(0, 10), new GridPoint(18, 10));
		addBus(context, space, grid, list, new GridPoint(9, 18), new GridPoint(9, 0));
		return context;
	}
	
	public static void addBus(Context<Object> context, ContinuousSpace<Object> space, Grid<Object> grid, ArrayList<String> map, GridPoint spawn, GridPoint dest) {
		Bus b = new Bus(space, grid, dest, context, map, spawn);
		add(context, space, grid, b, (int)spawn.getX(), (int)spawn.getY());
		b.initDijkstra();
	}
	
	public static void addHuman(Context<Object> context, ContinuousSpace<Object> space, Grid<Object> grid, ArrayList<String> map) {
		int height = map.size();
		int width = map.get(0).length();
		GridCellNgh<Spawn> nghSpawnCreator = new GridCellNgh<Spawn>(grid, new GridPoint(0,0), Spawn.class, width, height);
		List<GridCell<Spawn>> spawnGridCells = nghSpawnCreator.getNeighborhood(true);
		SimUtilities.shuffle(spawnGridCells, RandomHelper.getUniform());
		GridPoint spawn = null;
		for (GridCell<Spawn> cell : spawnGridCells) {
			if (cell.size() > 0) {
				spawn = cell.getPoint();
			}
		}
		GridCellNgh<Sidewalk> nghCreator = new GridCellNgh<Sidewalk>(grid, new GridPoint(0,0), Sidewalk.class, width, height);
		List<GridCell<Sidewalk>> gridCells = nghCreator.getNeighborhood(true);
		SimUtilities.shuffle(gridCells, RandomHelper.getUniform());
		GridPoint dest = null;
		for (GridCell<Sidewalk> cell : gridCells) {
			if (cell.size() > 0) {
				dest = cell.getPoint();
			}
		}
		if (dest.getX() == width - 1 || dest.getX() == 0 || dest.getY() == height - 1 || dest.getY() == 0) {
			double distance = Double.MAX_VALUE;
			GridCellNgh<BusShelter> shelterCreator = new GridCellNgh<BusShelter>(grid, new GridPoint(0,0), BusShelter.class, width, height);
			List<GridCell<BusShelter>> shelterGridCells = shelterCreator.getNeighborhood(true);
			for (GridCell<BusShelter> shelter : shelterGridCells) {
				if (shelter.size() > 0) {
					GridPoint d = shelter.getPoint();
					if (Math.sqrt(Math.pow(d.getX() - spawn.getX(), 2) + Math.pow(d.getY() - spawn.getY(), 2)) < distance) {
						distance = Math.sqrt(Math.pow(d.getX() - spawn.getX(), 2) + Math.pow(d.getY() - spawn.getY(), 2));
						dest = d;
						
					}
				}
			}
		}
		Human h = new Human(space, grid, dest, context, map);
		add(context, space, grid, h, spawn.getX(), spawn.getY());
		h.initDijkstra();
	}
}