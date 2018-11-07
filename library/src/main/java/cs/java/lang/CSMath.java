package cs.java.lang;

import cs.java.common.CSPoint;

public class CSMath {

    public static float getDistance(CSPoint point1, CSPoint point2) {
        return (float) Math.sqrt((point1.x() - point2.x()) * (point1.x() - point2.x()) + (point1.y() - point2.y()) * (point1.y() - point2.y()));
    }

    public static int randomInt(int minimum, int maximum) {
        return minimum + (int) (Math.random() * maximum);
    }
}
