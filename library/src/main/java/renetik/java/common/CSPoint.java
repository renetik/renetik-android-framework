package renetik.java.common;

public class CSPoint {

	public boolean equals(CSPoint point) {
		return x() == point.x() && y() == point.y();
	}

	@Override public boolean equals(Object o) {
		if (o instanceof CSPoint) return equals((CSPoint) o);
		return super.equals(o);
	}

	@Override public String toString() {
		return "[" + x + ";" + y + "]";
	}

	public final Number x;
	public final Number y;

	public CSPoint(Number x, Number y) {
		this.x = x;
		this.y = y;
	}

	public double doubleX() {
		return x.doubleValue();
	}

	public double doubleY() {
		return y.doubleValue();
	}

	public float x() {
		return x.floatValue();
	}

	public float y() {
		return y.floatValue();
	}

}
