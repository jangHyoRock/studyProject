package dhi.optimizer.service;

import dhi.optimizer.model.json.ItemStatus;
import dhi.optimizer.model.json.Operation;

/*
 * Operation management service interface.
 */
public interface OperationService {
	
	public Operation getOperationMode();
	
	public void saveOperationMode(ItemStatus optimizeMode) throws Exception;
	
	public void resetCumulatedCostSavingEffect();
}
