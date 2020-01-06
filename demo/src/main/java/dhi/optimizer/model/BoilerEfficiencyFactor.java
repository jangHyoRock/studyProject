package dhi.optimizer.model;

public class BoilerEfficiencyFactor {
	
	private final double BED_TEMPERATURE_FOR_BOTTOM_ASH = 1100; // Fixed : PTC 4 기준
	private final double REFERENCE_AIR_TEMPERATURE_FOR_EFFICIENCY_CALCULATION = 25; //Fixed : PTC 4 기준
	private final double HEATFLUX_THRU_FURNACE_HOPPER_OPENING = 31.5; // Heat flux thru furnace hopper opening, kW/m2
	
	// A. Coal Analysis (As-fired basis)
	private double totalMoistureAsFiredBasis = 4.4;  //Input
	private double fixedCarbonAsFireBasis = 52.8; //Input
	private double volatileMatterAsFireBasis = 23.1; //Input
	private double ashAsFireBasis = 19.7; //Input
	private double carbon = 57.74; //Input
	private double hydrogen = 2.6; //Input
	private double oxygen = 12.7; //Input
	private double nitrogen = 1.5; //Input
	private double sulfur = 1.36; //Input
	private double ash = 19.7; //Input 
	private double moisture = 4.4; //Input
	private double higherHeatingValue = 23790; //Input
	private double fuelConsumptionRate = 15368; //DCS 
	// 3) Fuel consumption rate
		/*
		 * 10GFUEL.AI_TCF.PNT(Total Coal Flow), 
		 * 좌측이 열량 보정값이면 개별 7개 (10GAI.AI086102.PNT, 10GAI.AI086106.PNT, 10GAI.AI086202.PNT, 10GAI.AI086206.PNT, 10HAI.AI096102.PNT, 10HAI.AI096106.PNT, 10HAI.AI096202.PNT) 합산필요, 
		 * 단위변환은 DCS가 t/h, 수식이 kg/h이므로 곱하기 1000 필요
		 */
	
	// B. Residue Analysis
	private double bottomAshTotalResidue = 15.0; //Input
	private double economizerHopperAshTotalResidue = 5.0; //Input
	private double bottomAshUBCResidue = 2.000; //Input
	private double econimizerHopperAshUBCResidue = 1.000; //Input
	private double flyAshUBCResidue = 1.000; //Input
	
	// C. Combustion Air Properties
	private double fanInletAir = 20.0; //DCS
	//private double weightedFanOutletAir = 25.0; //PAF과 SAF 가중평균온도
	
	/*
	 *  A = average(10GAI.AI088105.PNT, 10GAI.AI088205.PNT, 10HAI.AI098105.PNT, 10HAI.AI098205.PNT) : SAT
		B = average(10GAI.AI083307.PNT, 10GAI.AI084507.PNT, 10HAI.AI093307.PNT, 10HAI.AI094507.PNT) : PAT
		C = 10HFDF.AI_TAF.PNT : TAF
		D = SUM(10GAMILL.SEL100_2.OUT, 10GBMILL.SEL100_2.OUT, 10GCMILL.SEL100_2.OUT, 10GDMILL.SEL100_2.OUT, 10HEMILL.SEL100_2.OUT, 10HFMILL.SEL100_2.OUT, 10HGMILL.SEL100_2.OUT) : PAF
		
		수식입력값 = A*(C-D)/C + B*D/C
	 * 
	 */
	private double AHInletAir= 25.0; //DCS
	private double relativeHumidity = 60.0; //Input
	private double atmosphericPressure = 1.013; //Input

	// D. Flue Gas Analysis
	private double O2AtEconomizerOutlet = 3.1;  //DCS
	private double O2AtAHOutlet = 4; //DCS
	private double COAtAHOutlet = 0; //DCS
	private double economizerOutlet = 375.0; //DCS
	private double AHOutletGas = 130.0; //DCS 
	
	// G. Heat Losses
	private double wetAshPitLoss = 0.173; //Input
	private double projectedAreaOfHopperOpening= 5.57; // = 6.96*0.8 (Projected area of hopper opening, m2)
	private double surfaceRadiationAndConvection = 0.2; // Input (9) Surface radiation & convection)
	private double unmeasuredLosses = 0.1; // Input;
	
	// H. Heat credits
	private double auxiliaryDrivesPowerConsumption = 0.0; // P : Auxiliary drives power consumption(Mill Only)
	private double overallDriveEfficiency = 87.4; // η: Over-all drive efficiency
		
	public double getTotalMoistureAsFiredBasis() {
		return totalMoistureAsFiredBasis;
	}
	public void setTotalMoistureAsFiredBasis(double totalMoistureAsFiredBasis) {
		this.totalMoistureAsFiredBasis = totalMoistureAsFiredBasis;
	}
	public double getFixedCarbonAsFireBasis() {
		return fixedCarbonAsFireBasis;
	}
	public void setFixedCarbonAsFireBasis(double fixedCarbonAsFireBasis) {
		this.fixedCarbonAsFireBasis = fixedCarbonAsFireBasis;
	}
	public double getVolatileMatterAsFireBasis() {
		return volatileMatterAsFireBasis;
	}
	public void setVolatileMatterAsFireBasis(double volatileMatterAsFireBasis) {
		this.volatileMatterAsFireBasis = volatileMatterAsFireBasis;
	}
	public double getAshAsFireBasis() {
		return ashAsFireBasis;
	}
	public void setAshAsFireBasis(double ashAsFireBasis) {
		this.ashAsFireBasis = ashAsFireBasis;
	}
	public double getCarbon() {
		return carbon;
	}
	public void setCarbon(double carbon) {
		this.carbon = carbon;
	}
	public double getHydrogen() {
		return hydrogen;
	}
	public void setHydrogen(double hydrogen) {
		this.hydrogen = hydrogen;
	}
	public double getOxygen() {
		return oxygen;
	}
	public void setOxygen(double oxygen) {
		this.oxygen = oxygen;
	}
	public double getNitrogen() {
		return nitrogen;
	}
	public void setNitrogen(double nitrogen) {
		this.nitrogen = nitrogen;
	}
	public double getSulfur() {
		return sulfur;
	}
	public void setSulfur(double sulfur) {
		this.sulfur = sulfur;
	}
	public double getAsh() {
		return ash;
	}
	public void setAsh(double ash) {
		this.ash = ash;
	}
	public double getMoisture() {
		return moisture;
	}
	public void setMoisture(double moisture) {
		this.moisture = moisture;
	}
	public double getHigherHeatingValue() {
		return higherHeatingValue;
	}
	public void setHigherHeatingValue(double higherHeatingValue) {
		this.higherHeatingValue = higherHeatingValue;
	}
	public double getFuelConsumptionRate() {
		return fuelConsumptionRate;
	}
	public void setFuelConsumptionRate(double fuelConsumptionRate) {
		this.fuelConsumptionRate = fuelConsumptionRate;
	}
	public double getBottomAshTotalResidue() {
		return bottomAshTotalResidue;
	}
	public void setBottomAshTotalResidue(double bottomAshTotalResidue) {
		this.bottomAshTotalResidue = bottomAshTotalResidue;
	}
	public double getEconomizerHopperAshTotalResidue() {
		return economizerHopperAshTotalResidue;
	}
	public void setEconomizerHopperAshTotalResidue(double economizerHopperAshTotalResidue) {
		this.economizerHopperAshTotalResidue = economizerHopperAshTotalResidue;
	}
	public double getBottomAshUBCResidue() {
		return bottomAshUBCResidue;
	}
	public void setBottomAshUBCResidue(double bottomAshUBCResidue) {
		this.bottomAshUBCResidue = bottomAshUBCResidue;
	}
	public double getEconimizerHopperAshUBCResidue() {
		return econimizerHopperAshUBCResidue;
	}
	public void setEconimizerHopperAshUBCResidue(double econimizerHopperAshUBCResidue) {
		this.econimizerHopperAshUBCResidue = econimizerHopperAshUBCResidue;
	}
	public double getFlyAshUBCResidue() {
		return flyAshUBCResidue;
	}
	public void setFlyAshUBCResidue(double flyAshUBCResidue) {
		this.flyAshUBCResidue = flyAshUBCResidue;
	}
	public double getFanInletAir() {
		return fanInletAir;
	}
	public void setFanInletAir(double fanInletAir) {
		this.fanInletAir = fanInletAir;
	}
	public double getAHInletAir() {
		return AHInletAir;
	}
	public void setAHInletAir(double aHInletAir) {
		AHInletAir = aHInletAir;
	}
	public double getRelativeHumidity() {
		return relativeHumidity;
	}
	public void setRelativeHumidity(double relativeHumidity) {
		this.relativeHumidity = relativeHumidity;
	}
	public double getAtmosphericPressure() {
		return atmosphericPressure;
	}
	public void setAtmosphericPressure(double atmosphericPressure) {
		this.atmosphericPressure = atmosphericPressure;
	}
	public double getO2AtEconomizerOutlet() {
		return O2AtEconomizerOutlet;
	}
	public void setO2AtEconomizerOutlet(double o2AtEconomizerOutlet) {
		O2AtEconomizerOutlet = o2AtEconomizerOutlet;
	}
	public double getO2AtAHOutlet() {
		return O2AtAHOutlet;
	}
	public void setO2AtAHOutlet(double o2AtAHOutlet) {
		O2AtAHOutlet = o2AtAHOutlet;
	}
	public double getCOAtAHOutlet() {
		return COAtAHOutlet;
	}
	public void setCOAtAHOutlet(double cOAtAHOutlet) {
		COAtAHOutlet = cOAtAHOutlet;
	}
	public double getEconomizerOutlet() {
		return economizerOutlet;
	}
	public void setEconomizerOutlet(double economizerOutlet) {
		this.economizerOutlet = economizerOutlet;
	}
	public double getAHOutletGas() {
		return AHOutletGas;
	}
	public void setAHOutletGas(double aHOutletGas) {
		AHOutletGas = aHOutletGas;
	}
	public double getWetAshPitLoss() {
		return wetAshPitLoss;
	}
	public void setWetAshPitLoss(double wetAshPitLoss) {
		this.wetAshPitLoss = wetAshPitLoss;
	}
	public double getProjectedAreaOfHopperOpening() {
		return projectedAreaOfHopperOpening;
	}
	public void setProjectedAreaOfHopperOpening(double projectedAreaOfHopperOpening) {
		this.projectedAreaOfHopperOpening = projectedAreaOfHopperOpening;
	}
	public double getSurfaceRadiationAndConvection() {
		return surfaceRadiationAndConvection;
	}
	public void setSurfaceRadiationAndConvection(double surfaceRadiationAndConvection) {
		this.surfaceRadiationAndConvection = surfaceRadiationAndConvection;
	}
	public double getUnmeasuredLosses() {
		return unmeasuredLosses;
	}
	public void setUnmeasuredLosses(double unmeasuredLosses) {
		this.unmeasuredLosses = unmeasuredLosses;
	}
	public double getAuxiliaryDrivesPowerConsumption() {
		return auxiliaryDrivesPowerConsumption;
	}
	public void setAuxiliaryDrivesPowerConsumption(double auxiliaryDrivesPowerConsumption) {
		this.auxiliaryDrivesPowerConsumption = auxiliaryDrivesPowerConsumption;
	}
	public double getOverallDriveEfficiency() {
		return overallDriveEfficiency;
	}
	public void setOverallDriveEfficiency(double overallDriveEfficiency) {
		this.overallDriveEfficiency = overallDriveEfficiency;
	}
	public double getBedTemperatureForBottomAsh() {
		return BED_TEMPERATURE_FOR_BOTTOM_ASH;
	}
	public double getReferenceAirTemperatureForEfficiencyCalculation() {
		return REFERENCE_AIR_TEMPERATURE_FOR_EFFICIENCY_CALCULATION;
	}
	public double getHeatfluxThruFurnaceHopperOpening() {
		return HEATFLUX_THRU_FURNACE_HOPPER_OPENING;
	}	
	
	@Override
	public String toString() {
		return "BoilerEfficiencyFactor [getTotalMoistureAsFiredBasis(4.4)=" + getTotalMoistureAsFiredBasis()
				+ ", getFixedCarbonAsFireBasis(34.54)=" + getFixedCarbonAsFireBasis() + ", getVolatileMatterAsFireBasis(23.72)="
				+ getVolatileMatterAsFireBasis() + ", getAshAsFireBasis(27.74)=" + getAshAsFireBasis() + ", getCarbon(46.08)="
				+ getCarbon() + ", getHydrogen(2.81)=" + getHydrogen() + ", getOxygen(8.09)=" + getOxygen() + ", getNitrogen(0.96)="
				+ getNitrogen() + ", getSulfur(0.32)=" + getSulfur() + ", getAsh(27.74)=" + getAsh() + ", getMoisture(14.0)="
				+ getMoisture() + ", getHigherHeatingValue(17648.59)=" + getHigherHeatingValue() + ", getFuelConsumptionRate(360208.29)="
				+ getFuelConsumptionRate() + ", getBottomAshTotalResidue(15.0)=" + getBottomAshTotalResidue()
				+ ", getEconomizerHopperAshTotalResidue(5.0)=" + getEconomizerHopperAshTotalResidue()
				+ ", getBottomAshUBCResidue(2.0)=" + getBottomAshUBCResidue() + ", getEconimizerHopperAshUBCResidue(1.0)="
				+ getEconimizerHopperAshUBCResidue() + ", getFlyAshUBCResidue(1.0)=" + getFlyAshUBCResidue()
				+ ", getFanInletAir(25.95)=" + getFanInletAir() + ", getAHInletAir(32.64)=" + getAHInletAir()
				+ ", getRelativeHumidity(60.0)=" + getRelativeHumidity() + ", getAtmosphericPressure(1.013)="
				+ getAtmosphericPressure() + ", getO2AtEconomizerOutlet(3.42)=" + getO2AtEconomizerOutlet()
				+ ", getO2AtAHOutlet(3.84)=" + getO2AtAHOutlet() + ", getCOAtAHOutlet(0.01)=" + getCOAtAHOutlet()
				+ ", getEconomizerOutlet(350.76)=" + getEconomizerOutlet() + ", getAHOutletGas(144.10)=" + getAHOutletGas()
				+ ", getWetAshPitLoss(0.009)=" + getWetAshPitLoss() + ", getProjectedAreaOfHopperOpening(5.57)="
				+ getProjectedAreaOfHopperOpening() + ", getSurfaceRadiationAndConvection(0.2)="
				+ getSurfaceRadiationAndConvection() + ", getUnmeasuredLosses(0.1)=" + getUnmeasuredLosses()
				+ ", getAuxiliaryDrivesPowerConsumption(0.0)=" + getAuxiliaryDrivesPowerConsumption()
				+ ", getOverallDriveEfficiency(87.4)=" + getOverallDriveEfficiency() + "]";
	}
}
