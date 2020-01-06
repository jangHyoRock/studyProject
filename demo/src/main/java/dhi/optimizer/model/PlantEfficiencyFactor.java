package dhi.optimizer.model;

public class PlantEfficiencyFactor {
	private double mainSteamTemp;
	private double mainSteamPressure;
	private double fwTemp;
	private double fwPressure;
	private double hotRHSteamTemp;
	private double hotRHSteamPressure;
	private double coldRHSteamTemp;
	private double coldRHSteamPressure;
	private double rhSprayWTRTemp;
	private double rhSprayWTRPressure;
	private double mainSteamFlow;
	private double hotRHFlow;
	private double coldRHFlow;
	private double rhSprayFlow;
	private double generatorOutput;
	private double makeupFlow;
	private double pipingLoss;
	private double _100TmcrMsFlow = 1975.833;  // Input
	private double ipLpTbnFlowK = 39.833;  // Input
	private double rhSprayLossK = 8; // Input
	private double ammoniaSavingK = 1.1426; // Input
	private double totalAuxPower;
	private double stackNOxAvg;
	private double currentNOx;
	private double ammoniaPrice;
	
	public double getMainSteamTemp() {
		return mainSteamTemp;
	}
	public void setMainSteamTemp(double mainSteamTemp) {
		this.mainSteamTemp = mainSteamTemp;
	}
	public double getMainSteamPressure() {
		return mainSteamPressure;
	}
	public void setMainSteamPressure(double mainSteamPressure) {
		this.mainSteamPressure = mainSteamPressure;
	}
	public double getFwTemp() {
		return fwTemp;
	}
	public void setFwTemp(double fwTemp) {
		this.fwTemp = fwTemp;
	}
	public double getFwPressure() {
		return fwPressure;
	}
	public void setFwPressure(double fwPressure) {
		this.fwPressure = fwPressure;
	}
	public double getHotRHSteamTemp() {
		return hotRHSteamTemp;
	}
	public void setHotRHSteamTemp(double hotRHSteamTemp) {
		this.hotRHSteamTemp = hotRHSteamTemp;
	}
	public double getHotRHSteamPressure() {
		return hotRHSteamPressure;
	}
	public void setHotRHSteamPressure(double hotRHSteamPressure) {
		this.hotRHSteamPressure = hotRHSteamPressure;
	}
	public double getColdRHSteamTemp() {
		return coldRHSteamTemp;
	}
	public void setColdRHSteamTemp(double coldRHSteamTemp) {
		this.coldRHSteamTemp = coldRHSteamTemp;
	}
	public double getColdRHSteamPressure() {
		return coldRHSteamPressure;
	}
	public void setColdRHSteamPressure(double coldRHSteamPressure) {
		this.coldRHSteamPressure = coldRHSteamPressure;
	}
	public double getRhSprayWTRTemp() {
		return rhSprayWTRTemp;
	}
	public void setRhSprayWTRTemp(double rhSprayWTRTemp) {
		this.rhSprayWTRTemp = rhSprayWTRTemp;
	}
	public double getRhSprayWTRPressure() {
		return rhSprayWTRPressure;
	}
	public void setRhSprayWTRPressure(double rhSprayWTRPressure) {
		this.rhSprayWTRPressure = rhSprayWTRPressure;
	}
	public double getMainSteamFlow() {
		return mainSteamFlow;
	}
	public void setMainSteamFlow(double mainSteamFlow) {
		this.mainSteamFlow = mainSteamFlow;
	}
	public double getHotRHFlow() {
		return hotRHFlow;
	}
	public void setHotRHFlow(double hotRHFlow) {
		this.hotRHFlow = hotRHFlow;
	}
	public double getColdRHFlow() {
		return coldRHFlow;
	}
	public void setColdRHFlow(double coldRHFlow) {
		this.coldRHFlow = coldRHFlow;
	}
	public double getRhSprayFlow() {
		return rhSprayFlow;
	}
	public void setRhSprayFlow(double rhSprayFlow) {
		this.rhSprayFlow = rhSprayFlow;
	}
	public double getGeneratorOutput() {
		return generatorOutput;
	}
	public void setGeneratorOutput(double generatorOutput) {
		this.generatorOutput = generatorOutput;
	}
	public double getMakeupFlow() {
		return makeupFlow;
	}
	public void setMakeupFlow(double makeupFlow) {
		this.makeupFlow = makeupFlow;
	}
	public double getPipingLoss() {
		return pipingLoss;
	}
	public void setPipingLoss(double pipingLoss) {
		this.pipingLoss = pipingLoss;
	}
	public double get_100TmcrMsFlow() {
		return _100TmcrMsFlow;
	}
	public void set_100TmcrMsFlow(double _100TmcrMsFlow) {
		this._100TmcrMsFlow = _100TmcrMsFlow;
	}
	public double getIpLpTbnFlowK() {
		return ipLpTbnFlowK;
	}
	public void setIpLpTbnFlowK(double ipLpTbnFlowK) {
		this.ipLpTbnFlowK = ipLpTbnFlowK;
	}
	public double getRhSprayLossK() {
		return rhSprayLossK;
	}
	public void setRhSprayLossK(double rhSprayLossK) {
		this.rhSprayLossK = rhSprayLossK;
	}
	public double getAmmoniaSavingK() {
		return ammoniaSavingK;
	}
	public void setAmmoniaSavingK(double ammoniaSavingK) {
		this.ammoniaSavingK = ammoniaSavingK;
	}
	public double getTotalAuxPower() {
		return totalAuxPower;
	}
	public void setTotalAuxPower(double totalAuxPower) {
		this.totalAuxPower = totalAuxPower;
	}
	public double getStackNOxAvg() {
		return stackNOxAvg;
	}
	public void setStackNOxAvg(double stackNOxAvg) {
		this.stackNOxAvg = stackNOxAvg;
	}
	public double getCurrentNOx() {
		return currentNOx;
	}
	public void setCurrentNOx(double currentNOx) {
		this.currentNOx = currentNOx;
	}
	public double getAmmoniaPrice() {
		return ammoniaPrice;
	}
	public void setAmmoniaPrice(double ammoniaPrice) {
		this.ammoniaPrice = ammoniaPrice;
	}
}
