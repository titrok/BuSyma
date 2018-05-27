package buSyma;

import repast.simphony.space.continuous.ContinuousAdder;
import repast.simphony.space.continuous.ContinuousSpace;

public class CustomAdder implements ContinuousAdder<Object> {
	
	public float x;
	public float y;
	
	@Override
	public void add(ContinuousSpace<Object> destination, Object object) {
		destination.moveTo(object, x, y);
	}

}
