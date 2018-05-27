package buSyma;

import java.awt.Color;


import repast.simphony.gis.styleEditor.SimpleMarkFactory;
import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.VSpatial;

public class TrafficLightStyle extends DefaultStyleOGL2D {
	private static SimpleMarkFactory markFac = new SimpleMarkFactory();
    @Override
    public void init(ShapeFactory2D factory) {
        super.init(factory);
    }

    @Override
    public VSpatial getVSpatial(Object agent, VSpatial spatial) {
        if (spatial == null) {
            spatial = shapeFactory.createShape(markFac.getMark("circle"), true);
        }
        return spatial;
    }

    @Override
    public Color getColor(Object object) {
    	TrafficLight a = (TrafficLight)object;
        if (a.red) {
            return Color.RED;
        } else {
            return Color.GREEN;
        }
    }

    @Override
    public float getScale(Object object) {
        return 10;
    }

}
