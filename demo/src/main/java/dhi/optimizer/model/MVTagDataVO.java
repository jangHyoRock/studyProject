package dhi.optimizer.model;

/**
 * PSO MV Tag Data Model VO Class.
 */
public class MVTagDataVO {

	private String psoMV;
	
	private String psoType;
	
	private String tagNm;
	
	private Double tagVal;
	
	public MVTagDataVO() {};

	public MVTagDataVO(String psoMV, String psoType, String tagNm, Double tagVal) {
		this.psoMV = psoMV;
		this.psoType = psoType;
		this.tagNm = tagNm;
		this.tagVal = tagVal;
	}

	public String getPsoMV() {
		return psoMV;
	}

	public void setPsoMV(String psoMV) {
		this.psoMV = psoMV;
	}

	public String getPsoType() {
		return psoType;
	}

	public void setPsoType(String psoType) {
		this.psoType = psoType;
	}

	public String getTagNm() {
		return tagNm;
	}

	public void setTagNm(String tagNm) {
		this.tagNm = tagNm;
	}

	public Double getTagVal() {
		return tagVal;
	}

	public void setTagVal(Double tagVal) {
		this.tagVal = tagVal;
	}
}