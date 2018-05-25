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
	
	private Grid<Object> grid;
	private ContinuousSpace<Object> space;
	private Context<Object> context;
	private int height;
	private int width;
	
	
	public static void add(Context<Object> context, ContinuousSpace<Object> space, Grid<Object> grid, Object obj, float x, float y) {
		SideWalkAdder swa = (SideWalkAdder)space.getAdder();
		swa.x = x + 0.5f;
		swa.y = y + 0.5f;
		context.add(obj);
		grid.moveTo(obj, (int)x, (int)y);
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
		this.width= list.get(0).length();
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		SideWalkAdder swa = new SideWalkAdder();
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace("space", context, swa, new repast.simphony.space.continuous.StrictBorders(), width, height);
		this.space = space;
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid("grid", context, new GridBuilderParameters<Object>(new StrictBorders(), new SimpleGridAdder<Object>(), true, width, height));
		this.grid = grid;
		
		for (int i = 0; i < height; i++)
		{
			float y = i;
			for (int j = 0; j < width; j++)
			{
				float x = j;
				if (list.get(i).charAt(j) == 'X')
					add(context, space, grid, new Sidewalk(space, grid), x, y);
				if (list.get(i).charAt(j) == 'R')
					add(context, space, grid, new Road(space, grid), x, y);
				if (list.get(i).charAt(j) == 'S')
					add(context, space, grid, new Spawn(space, grid), x, y);
				if (list.get(i).charAt(j) == 'C')
					add(context, space, grid, new Crossing(space, grid), x, y);
				if (list.get(i).charAt(j) == 'A')
					add(context, space, grid, new BusShelter(space, grid), x, y);
				if (list.get(i).charAt(j) == 'T')
					add(context, space, grid, new TrafficLight(space, grid), x, y);
			}
		}
		
		for (int  i = 0; i < 1; i++)
			addHuman(context, space, grid, list);
		addBus(context, space, grid, list);
		this.context = context;
		return context;
	}
	
	public static void addBus(Context context, ContinuousSpace<Object> space, Grid<Object> grid, ArrayList<String> map) {
		SideWalkAdder swa = (SideWalkAdder)space.getAdder();
		GridPoint spawn = new GridPoint(0, 9);
		GridPoint dest = new GridPoint(18, 9);
		
		Bus b = new Bus(space, grid, dest, context, map);
		add(context, space, grid, b, (int)spawn.getX(), (int)spawn.getY());
		b.initDijkstra();
	}
	
	public static void addHuman(Context context, ContinuousSpace<Object> space, Grid<Object> grid, ArrayList<String> map) {
		SideWalkAdder swa = (SideWalkAdder)space.getAdder();
		GridCellNgh<Spawn> nghSpawnCreator = new GridCellNgh<Spawn>(grid, new GridPoint(0,0), Spawn.class, map.get(0).length(), map.size());
		List<GridCell<Spawn>> spawnGridCells = nghSpawnCreator.getNeighborhood(true);
		SimUtilities.shuffle(spawnGridCells, RandomHelper.getUniform());
		GridPoint spawn = null;
		for (GridCell<Spawn> cell : spawnGridCells) {
			if (cell.size() > 0) {
				spawn = cell.getPoint();
			}
		}
		GridCellNgh<Sidewalk> nghCreator = new GridCellNgh<Sidewalk>(grid, new GridPoint(0,0), Sidewalk.class, map.get(0).length(), map.size());
		List<GridCell<Sidewalk>> gridCells = nghCreator.getNeighborhood(true);
		SimUtilities.shuffle(gridCells, RandomHelper.getUniform());
		GridPoint dest = null;
		for (GridCell<Sidewalk> cell : gridCells) {
			if (cell.size() > 0) {
				dest = cell.getPoint();
			}
		}
		Human h = new Human(space, grid, dest, context, map);
		add(context, space, grid, h, spawn.getX(), spawn.getY());
		h.initDijkstra();
	}
}