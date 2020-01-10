package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;

/*
 * Entity that contain values for viewing trend chart.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Chart {

	private Tag[] tag;

	private Optimizer[] optimizer;

	public Tag[] getTag() {
		return tag;
	}

	public void setTag(Tag[] tag) {
		this.tag = tag;
	}

	public Optimizer[] getOptimizer() {
		return optimizer;
	}

	public void setOptimizer(Optimizer[] optimizer) {
		this.optimizer = optimizer;
	}
}