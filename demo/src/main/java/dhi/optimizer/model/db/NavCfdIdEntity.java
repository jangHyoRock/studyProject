package dhi.optimizer.model.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/*
 * DB Table PK Model Class using JPA.(TB_CONFIG_CHK)
 */
@SuppressWarnings("serial")
@Embeddable
public class NavCfdIdEntity implements Serializable {
	
	@Column(name="CFD_ID")
	private String cfdId;
		
	@Column(name="ROW")
	private String row;
	
	@Column(name="COL")
	private String col;

	public String getCfdId() {
		return cfdId;
	}

	public void setCfdId(String cfdId) {
		this.cfdId = cfdId;
	}

	public String getRow() {
		return row;
	}

	public void setRow(String row) {
		this.row = row;
	}

	public String getCol() {
		return col;
	}

	public void setCol(String col) {
		this.col = col;
	}	
}