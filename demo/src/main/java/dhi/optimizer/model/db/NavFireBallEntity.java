package dhi.optimizer.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_NAV_CONF)
 */
@Entity
@Table(name="TB_NAV_FIREBALL")
public class NavFireBallEntity {

	@Column(name="TYPE")
	private String type;
	
	@Id
	@Column(name="DIRECTION")
	private String direction;
	
	@Column(name="CORNER_1")
	private boolean corner1;
	
	@Column(name="CORNER_2")
	private boolean corner2;
	
	@Column(name="CORNER_3")
	private boolean corner3;
	
	@Column(name="CORNER_4")
	private boolean corner4;
	
	@Column(name="DIRECTION_ORDER")
	private int directionOrder;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public boolean isCorner1() {
		return corner1;
	}

	public void setCorner1(boolean corner1) {
		this.corner1 = corner1;
	}

	public boolean isCorner2() {
		return corner2;
	}

	public void setCorner2(boolean corner2) {
		this.corner2 = corner2;
	}

	public boolean isCorner3() {
		return corner3;
	}

	public void setCorner3(boolean corner3) {
		this.corner3 = corner3;
	}

	public boolean isCorner4() {
		return corner4;
	}

	public void setCorner4(boolean corner4) {
		this.corner4 = corner4;
	}

	public int getDirectionOrder() {
		return directionOrder;
	}

	public void setDirectionOrder(int directionOrder) {
		this.directionOrder = directionOrder;
	}
}
