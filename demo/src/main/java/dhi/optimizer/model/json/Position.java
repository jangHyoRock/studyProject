package dhi.optimizer.model.json;

public class Position {

	private Number x;

	private Number y;

	public Position(Number x, Number y) {
		this.x = x;
		this.y = y;
	}

	public Number getX() {
		return x;
	}

	public void setX(Number x) {
		this.x = x;
	}

	public Number getY() {
		return y;
	}

	public void setY(Number y) {
		this.y = y;
	}
}
