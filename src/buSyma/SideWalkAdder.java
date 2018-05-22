package buSyma;

import repast.simphony.space.continuous.ContinuousAdder;
import repast.simphony.space.continuous.ContinuousSpace;

public class SideWalkAdder implements ContinuousAdder<Object> {
	
	public float x = 0.5f, y = 0.5f;
	@Override
	public void add(ContinuousSpace<Object> destination, Object object) {
		// TODO Auto-generated method stub
		destination.moveTo(object, x,y);
		}

}
