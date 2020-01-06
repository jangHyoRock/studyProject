package dhi.optimizer.model.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/*
 * DB Table PK Model Class using JPA.(TB_OPT_CODE_INF)
 */
@SuppressWarnings("serial")
@Embeddable
public class OptCodeInfIdEntity implements Serializable {
	
	@Column(name="GROUP_ID")
	private String groupId;	
	
	@Column(name="CODE_ID")
	private String codeId;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}
}
