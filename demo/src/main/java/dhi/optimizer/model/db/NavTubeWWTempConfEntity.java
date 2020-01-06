package dhi.optimizer.model.db;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_NAV_SPIRAL_WW_TEMP_CONF)
 */
@Entity
@Table(name="TB_NAV_SPIRAL_WW_TEMP_CONF")
public class NavTubeWWTempConfEntity {

	@EmbeddedId
	private NavTubeWWTempConfIdEntity navTubeWWTempConfIdEntity;
	
	@Column(name="TUBE_INDEX")
	private String tubeIndex;
	
	@Column(name="TAG_ID")
	private String tagId;
	
	public NavTubeWWTempConfIdEntity getNavTubeWWTempConfIdEntity() {
		return navTubeWWTempConfIdEntity;
	}

	public void setNavTubeWWTempConfIdEntity(NavTubeWWTempConfIdEntity navTubeWWTempConfIdEntity) {
		this.navTubeWWTempConfIdEntity = navTubeWWTempConfIdEntity;
	}	

	public String getTubeIndex() {
		return tubeIndex;
	}

	public void setTubeIndex(String tubeIndex) {
		this.tubeIndex = tubeIndex;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}	
}
