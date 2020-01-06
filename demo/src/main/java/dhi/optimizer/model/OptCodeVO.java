package dhi.optimizer.model;

/**
 * Optimizer Code Model VO Class.
 */
public class OptCodeVO {

	private String codeId;

	private String codeNm;

	private String tagNm;

	private String unit;

	public OptCodeVO() {
	};

	public OptCodeVO(String codeId, String codeNm, String tagNm, String unit) {
		this.codeId = codeId;
		this.codeNm = codeNm;
		this.tagNm = tagNm;
		this.unit = unit;
	}

	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	public String getCodeNm() {
		return codeNm;
	}

	public void setCodeNm(String codeNm) {
		this.codeNm = codeNm;
	}

	public String getTagNm() {
		return tagNm;
	}

	public void setTagNm(String tagNm) {
		this.tagNm = tagNm;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
}
