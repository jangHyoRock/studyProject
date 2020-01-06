package dhi.optimizer.model.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/*
 * DB Table PK Model Class using JPA.(TB_NAV_RESULT)
 */
@SuppressWarnings("serial")
@Embeddable
public class NavResultIdEntity implements Serializable {

	@Column(name="GROUP_ID")
	private String groupId;

	@Column(name="ITEM_ID")
	private String itemId;
	
	public NavResultIdEntity() {};

	public NavResultIdEntity(String groupId, String itemId) {
		this.groupId = groupId;
		this.itemId = itemId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	@Override
	public int hashCode() {
		String s = this.groupId + this.itemId;
		return s.hashCode();
	}

	@Override
	public boolean equals(Object object) {
		NavResultIdEntity NavResultIdEntity = (NavResultIdEntity) object;
		if (this.groupId.equals(NavResultIdEntity.getGroupId()) && this.itemId.equals(NavResultIdEntity.getItemId()))
			return true;

		return false;
	}
}
