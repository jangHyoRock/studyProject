package dhi.common.simplify;

public class PointSet implements Point {
	
	double x;
	double y;

	public PointSet(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public String toString() {
		return "{" + "x=" + x + ", y=" + y + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PointSet myPoint = (PointSet) o;

		if (Double.compare(myPoint.x, x) != 0) return false;
		if (Double.compare(myPoint.y, y) != 0) return false;

		return true;
	}
}
