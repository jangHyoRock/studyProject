package dhi.optimizer.algorithm.common;

public abstract class XSequenceFunction {
	
	private int seqNo = 0;
	
	public int getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}

	public XSequenceFunction() {
	}
	
	public int excute() {
		int result = -1;
		int nextSeqNo = this.seqNo;

		switch (nextSeqNo) {
		case 0:
			nextSeqNo = 10;
			break;
		case 10:
			nextSeqNo = 0;
			break;
		case 1000:
			break;
		}

		this.seqNo = nextSeqNo;

		return result;
	}
}