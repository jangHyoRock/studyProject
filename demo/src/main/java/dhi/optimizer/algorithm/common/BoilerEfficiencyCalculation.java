package dhi.optimizer.algorithm.common;

import dhi.optimizer.model.BoilerEfficiencyFactor;

public class BoilerEfficiencyCalculation {
		
    // 1) Total moisture (As fired basis)
	private double MFrWF;  //Input
	// 2) Proximate analysis (As fired basis)
	private double MFrFc; //Input
	private double MFrVm; //Input
	private double MFrAsF; //Input
	// 3) Absolute analysis
	private double MpCF; //Input
	private double MpH2F; //Input
	private double MpO2F; //Input
	private double MpN2F; //Input
	private double MpSF; //Input
	private double MpAsF; //Input 
	private double MpWF; //Input
	// 2) Higher Heating Value
	private double HHVF; //Input
	// 3) Fuel consumption rate
	private double MrF; //DCS
	private double MpRsb; //Input
	private double MpRse; //Input
 	//private double MpRsf; // MpRsf = 100 - (MpRsb + MpRse);
	private double MpCRsb; //Input
	private double MpCRse; //Input
	private double MpCRsf; //Input
	//private double MpCRs; //MpCRs =[(MpRsb * MpCRsb) + (MpRse * MpCRse) +  (MpRsf * MpCRsf)]/100
	//private double MFrRs; //MFrRs = MpAsF / (100-MpCRs) 
	private double TRsb; // Fixed : PTC 4 기준
	//private double Trse; // Trse = TFg14;
	//private double TRsf; //TRsf = TFgLvCr;
	//private double MqRsb; //MqRsb = (MpRsb * MFrRs) / (100 * HHVF)
	//private double MqRse; //MqRse = (MpRse * MFrRs) / (100 * HHVF)
	//private double MqRsf; //MqRsf = (MpRsf * MFrRs) / (100 * HHVF)
	//private double MpUbC;  //MpUbC = MpCRs * MFrRs
	//private double MpCb;  //MpCb = MpCF - MpUbC

	private double TRef; //Fixed : PTC 4 기준
	private double TA6; //DCS
	//private double TA7; //PAF과 SAF 가중평균온도
	private double TA8; //DCS


	//private double Tdb; // Tdb = TA6 
	private double Rhm; //Input
	private double PA; //Input
	//private double PsWvTdb; // PsWvTdb = (0.019257+1.289016*10^(-3)*(Tdb*1.8+32)+1.21122*10^(-5)*(Tdb*1.8+32)^2+4.534007*10^(-7)*(Tdb*1.8+32)^3+6.84188*10^(-11)*(Tdb*1.8+32)^4+2.197092*10^(-11)*(Tdb*1.8+32)^5)/14.5
	//private double PpWvA; //PpWvA = (Rhm * PsWvTdb) / 100
	//private double MFrWDA; //MFrWDA = 0.622 × PpWvA / (Pa-PpWvA )

	//private double MFrWA; //MFrWA = MFrWDA / (1+MFrWDA)

	//private double MFrThACr; //MFrThACr=(0.1151xMpCb)+(0.3429xMpH2F)+(0.0431xMpSF) - (0.0432xMpO2F)

	//private double MoThACr; //MoThACr = MFrThAPCr / 28.9625

	//private double MqThACr;  //MqThACr = MFrThACr / HHVF

	//private double MoDPc; //MoDPc = MpCb/1201.1 + MpSF/3206.5 + MpN2F/2801.34

	//private double XpA14;  // XpA14={[DVpO214x(MoDPc+0.7905xMoThACr)]/[MoThACr *(20.95-DVpO214)] } * 100

	//private double XpA15; //XpA15=100x{[DVpO215 * (MoDPc+0.7905 xMoThACr)]/[MoThACr *(20.95-DVpO215)]}

	//private double MqDA9; //MqDA9 = MqThACr *(1+ XpA14/100)
	//private double MqDA8; //MqDA8 = MqThACr *(1+ XpA15 / 100)

	private double DVpO214; //DCS
	private double DVpO215; //DCS
	private double DVpCO14; //DCS

	private double TFg14; //DCS
	private double TFg15; //DCS 
//	private double TFgLvCr; //TFgLvCr = TFg15+MnCpA/MnCpFg * (MqFg15/MqFg14-1) * (TFg15-TA8)
//
//	private double MqFgF; //MqFgF  = (100 - MpAsF - MpUbC) / (100 * HHVF)
// 
//	private double MqWF; //MqWF = MpWF / (100 * HHVF)
//
//	private double MqWH2F; //MqWH2F = 8.937xMpH2F/(100xHHVF)
//
//	private double MqWA9; //MqWA9=MFrWDA xMqDA9
//	private double MqWA8; //MqWA8 = MFrWDA * MqDA8
//	private double MqWFg14; //MqWFg14=MqWF+MqWH2F+MqWA9+MqWAdz
//	private double MqWFg15; //MqWFg15=MqWF+MqWH2F+MqWA8+MqWAdz
//
//	private double MqFg14; //MqFg14=MqDA9+MqWA9+MqFgF+MqWAdz
//
//	private double MqFg15; //MqFg15=MqDA8+MqWA8+MqFgF+MqWAdz
//
//	private double MqDFg14; //MqDFg14 = MqFg14 -MqWFg14
//
//	private double MqDFg15; //MqDFg15 = MqFg15 -MqWFg15
//
//	private double MoDFg14; //MoDFg14=MoDPc + MoThACr * (0.7905 + XpA14/100 )
//
//	private double MoDFg15; //MoDFg15=MoDPc + MoThACr * (0.7905 + XpA15/100)
//
//	private double AL; //    AL = (MFrFg15 -MFrFg14) / MFrFg14 * 100
//
//	private double HDAEn; //HDAEn = 1.005 * (TA8 - TRef)
//
//	private double HDfgLvCr; //HDfgLv=C0+C1TK+C2TK2+C3TK3+C4TK4+C5TK5
//
//	private double HStLvCr; // HStLvCr= (0.4329*(TFgLvCr*1.8+32)+3.958*10^-5*(TFgLvCr*1.8+32)^2+1062.2)*2.326
//	private double HWRe;  // HWRe= ((TRef*1.8+32)-32)*2.326
//	private double HWvLvCr; // HWvLvCr = ((-0.2394034*10^3+0.8274589*(TFgLvCr+273.15)-0.1797539*10^(-3)*(TFgLvCr+273.15)^2+0.3934614*10^(-6)*(TFgLvCr+273.15)^3-0.2415873*10^(-9)*(TFgLvCr+273.15)^4+0.6069264*10^(-13)*(TFgLvCr+273.15)^5)*2.326)
//	private double HWvEn;  // HWvEn = (-0.2394034*10^3+0.8274589*(TA8+273.15)-0.1797539*10^-3*(TA8+273.15)^2+0.3934614*10^-6*(TA8+273.15)^3-0.2415873*10^-9*(TA8+273.15)^4+0.6069264*10^-13*(TA8+273.15)^5)*2.326
	                         //HWvEn = C0+C1TK+C2TK2+C3TK3+C4TK4+C5TK5
	 //HRs = (0.16 T + 1.09×10-4 T2 - 2.843×10-8 T3 - 12.95) * 2.326,  where T=oF
//	private double HRsb;  // HRsb = (0.16*(TRsb*1.8+32)+0.000109*(TRsb*1.8+32)^2-0.00000002843*(TRsb*1.8+32)^3-12.95)*2.326
//	private double HRse;  // HRse = (0.16*(Trse*1.8+32)+0.000109*(Trse*1.8+32)^2-0.00000002843*(Trse*1.8+32)^3-12.95)*2.326
//	private double HRsf;  // HRsf = (0.16*(TRsf*1.8+32)+0.000109*(TRsf*1.8+32)^2-0.00000002843*(TRsf*1.8+32)^3-12.95)*2.326
//
//	private double Tcoal;  // Tcoal = TA6
//
//	private double HFc;   // HFc = (0.152*(1.8*Tcoal+32)+0.000195*(1.8*Tcoal+32)^2-12.86)*2.326
//
//	private double HVm1; // HVm1 = (0.38*(1.8*Tcoal+32)+0.000225*(1.8*Tcoal+32)^2-30.594)*2.326
//
//	private double HVm2; // HVm2 = (0.7*(1.8*Tcoal+32)+0.00017*(1.8*Tcoal+32)^2-54.908)*2.326
//
//	private double HRs;  // HRs = (0.17*(1.8*Tcoal+32)+0.00008*(1.8*Tcoal+32)^2-13.564)*2.326
//
//	private double HW;  // HW = ((1.8*Tcoal+32)-77)*2.326
//
//	private double MFrVmCr; // MFrVmCr = (MFrVm/100)/(1-MFrAsF/100-MFrWF/100)
//
//	private double MFrVm1; // MFrVm1=  IF(MFrVmCr>0.1,MFrVm/100-MFrVm2,0)
//	private double MFrVm2; //  MFrVm2 = IF(MFrVmCr>0.1,0.1*(1-MFrAsF/100-MFrWF/100),MFrVm/100)
//	private double HFEn; // HFEn = (MFrFc/100)*HFc+(MFrAsF/100)*HRs+(MFrWF/100)*HW+MFrVm1*HVm1+MFrVm2*HVm2
//
//	private double QpLDFg; // QpLDFg = ROUND(100*MqDFg14*HDfgLvCr,3)
//
//	private double QpLH2F; // QpLH2F = ROUND(100*MqWH2F*(HStLvCr-HWRe),3)
//
//	private double QpLWF;  // QpLWF = ROUND(100*MqWF*(HStLvCr-HWRe),3)
//
//	private double QpLWA;  // QpLWA = ROUND(100*MFrWDA*MqDA9*HWvLvCr,3)
//
//	private double QpLUbC; // QpLUbC = ROUND(MpUbC*33700/HHVF,3)
//
//	private double QpLRs; // QpLRs = ROUND(100*((MqRsb*HRsb)+(MqRse*HRse)+(MqRsf*HRsf)),3)
//
	private double QrLAp; // QrLAp = 31.5*(6.96*0.8)/(HHVF*MrF/3600)*100
	
	private double QrAp; // Heat flux thru furnace hopper opening, kW/m2
	
	private double ApAf; // = 6.96*0.8 (Projected area of hopper opening, m2)

//	private double QpLCO; // QpLCO = ROUND((DVpCO14*MoDFg14*28.01)*(10111/HHVF),3)  (8) Carbon monoxide in flue gas loss)

	private double QpLSrc; // Input (9) Surface radiation & convection)

	private double QpLum; // Input;
//	private double QpL; // QpL = QpLDFg+QpLH2F+QpLWF+QpLWA+QpLUbC+QpLRs+QrLAp+QpLCO+QpLSrc+QpLum

//	private double QpBDA; // QpBDA = ROUND(100*MqDA9*HDAEn,3)

//	private double QpBWA; // QpBWA = ROUND(100*MFrWDA*MqDA9*HWvEn,3)

//	private double QpBF; // QpBF = ROUND(100*(HFEn/HHVF),3)

//	private double QpBX; // QpBX = ROUND(100*(P*3600*η/100)/(MrF * HHVF),3)
	
	private double P; // P : Auxiliary drives power consumption(Mill Only)
	private double η; // η: Over-all drive efficiency

//	private double QpB;  // QpB = QpBDA+QpBWA+QpBF+QpBX

//	private double ηfuel_Fuel; // ηfuel = 100-QpL+QpB
	
//	private double ηfuel_Gross; // ηfuel = 100-100*QpL/(100+QpB)
	
	/*
	public BoilerEfficiencyCalculation() {
		//this.MFrWF = bef.get
	}*/
	
	public BoilerEfficiencyCalculation(BoilerEfficiencyFactor bef) {
		this.MFrWF = bef.getTotalMoistureAsFiredBasis();
		this.MFrFc = bef.getFixedCarbonAsFireBasis();
		this.MFrVm = bef.getVolatileMatterAsFireBasis();
		this.MFrAsF = bef.getAshAsFireBasis();
		this.MpCF = bef.getCarbon();
		this.MpH2F = bef.getHydrogen();
		this.MpO2F = bef.getOxygen();
		this.MpN2F = bef.getNitrogen();
		this.MpSF = bef.getSulfur();
		this.MpAsF = bef.getAsh();
		this.MpWF = bef.getMoisture();

		this.HHVF = bef.getHigherHeatingValue();
		this.MrF = bef.getFuelConsumptionRate();
		
		this.MpRsb = bef.getBottomAshTotalResidue();
		this.MpRse = bef.getEconomizerHopperAshTotalResidue();
		
		this.MpCRsb = bef.getBottomAshUBCResidue();
		this.MpCRse = bef.getEconimizerHopperAshUBCResidue();
		this.MpCRsf = bef.getFlyAshUBCResidue();
		
		this.TRsb = bef.getBedTemperatureForBottomAsh();

		this.TRef = bef.getReferenceAirTemperatureForEfficiencyCalculation();
		this.TA6 = bef.getFanInletAir();
		this.TA8 = bef.getAHInletAir();

		this.Rhm = bef.getRelativeHumidity();
		this.PA = bef.getAtmosphericPressure();
		
		this.DVpO214 = bef.getO2AtEconomizerOutlet();
		this.DVpO215 = bef.getO2AtAHOutlet();
		this.DVpCO14 = bef.getCOAtAHOutlet();

		this.TFg14 = bef.getEconomizerOutlet();
		this.TFg15 = bef.getAHOutletGas();

		this.QrLAp = bef.getWetAshPitLoss();
		this.QrAp = bef.getHeatfluxThruFurnaceHopperOpening();
		this.ApAf = bef.getProjectedAreaOfHopperOpening(); 
		this.QpLSrc = bef.getSurfaceRadiationAndConvection();
		this.QpLum = bef.getUnmeasuredLosses();
		this.P = bef.getAuxiliaryDrivesPowerConsumption();
		this.η = bef.getOverallDriveEfficiency();


	}
	/**
	 * 소수점 4자리로 만들어주는 함수
	 * 
	 * @param val
	 * @return
	 */
	private double round(double val, int digit) {
		double num = Math.pow(10d, digit);
		return (double)Math.round(val * num) / num;
	}
	
	
	/**
	 * MpRsf 
	 * 
	 * @param MpRsb
	 * @param MpRse
	 * @return
	 */
	private double MpRsf() {
		return 100 - (MpRsb + MpRse);
	}
	
	/**
	 * MpCRs
	 * 
	 * @return
	 */
	private double MpCRs() {
		return ((MpRsb * MpCRsb) + (MpRse * MpCRse) +  (MpRsf() * MpCRsf))/100;
	}
	
	/**
	 * MFrRs
	 * 
	 * @return
	 */
	private double MFrRs() {
		return MpAsF / (100-MpCRs());
	}
	
	/**
	 * Trse
	 * 
	 * @return
	 */
	private double Trse() {
		return TFg14;
	}
	
	/**
	 * TRsf
	 * 
	 * @return
	 */
	private double TRsf() {
		return TFgLvCr();
	}
	
	/**
	 * MqRsb
	 * 
	 * @return
	 */
	private double MqRsb() {
		return (MpRsb * MFrRs()) / (100 * HHVF);
	}
	
	
	private double MqRse() {
		return (MpRse * MFrRs()) / (100 * HHVF);
	}
	
	/**
	 * MqRsf = (MpRsf * MFrRs) / (100 * HHVF)
	 */
	private double MqRsf() {
		return  (MpRsf() * MFrRs()) / (100 * HHVF);
	}
	
	/**
	 * MpUbC
	 * @return
	 */
	private double MpUbC() { 
		return MpCRs() * MFrRs();
	}

	
	/**
	 * MpCb
	 */
	private double MpCb() { 
		return MpCF - MpUbC();
	}
	
	/**
	 * Tdb
	 * @return
	 */
	private double Tdb() {
		return TA6;				
	}
	
	/**
	 * e) Saturation pressure at Tdb
	 * @param Tdb
	 * @return
	 */
	public double PsWvTdb() {
		return round((0.019257+1.289016* Math.pow(10, -3) * (Tdb()*1.8+32) + 1.21122*Math.pow(10, -5)*Math.pow((Tdb()*1.8+32), 2)+4.534007*Math.pow(10, -7)*Math.pow((Tdb()*1.8+32), 3)+6.84188*Math.pow(10,-11)*Math.pow((Tdb()*1.8+32),4)+2.197092*Math.pow(10,-11)*Math.pow((Tdb()*1.8+32), 5)) / 14.5, 4);
	}
	
	/**
	 *  f) Partial pressure of vapor in wet air 
     *  [PpWvA = (Rhm * PsWvTdb) / 100]
	 * @return
	 */
	private double PpWvA() {
		return (Rhm * PsWvTdb()) / 100;
	}
	
	/**
	 * g) Moisture in dry air    
	 * 
	 * @return
	 */
	private double MFrWDA() {
		return 0.622 * PpWvA() / (PA-PpWvA() );
	}
	
	
	/**
	 * h) Moisture in wet air    
	 * 
	 * @return
	 */
	private double MFrWA() {
		return MFrWDA() / (1+MFrWDA());
	}
	
	/**
	 * a) Theoretical air
	 * 
	 * @return
	 */
	private double MFrThACr() {
		return (0.1151 * MpCb())+(0.3429 * MpH2F)+(0.0431 * MpSF) - (0.0432 * MpO2F);
	}
	
	/**
	 * b) Theoretical air, mole/mass fuel
	 * MoThACr = MFrThAPCr / 28.9625
	 * 
	 * @return
	 */
	private double MoThACr() {
		return MFrThACr() / 28.9625;
	}
	
	/**
	 * c) Theoretical air weight
	 * MqThACr = MFrThACr / HHVF
	 * 
	 * @return
	 */
	private double MqThACr() {
		return MFrThACr() / HHVF;
		
	}
	
	/**
	 * a) moles of dry products
	 * [MoDPc = MpCb/1201.1 + MpSF/3206.5 + MpN2F/2801.34]
	 * 
	 * @return
	 */
	private double MoDPc() {
		return MpCb()/1201.1 + MpSF/3206.5 + MpN2F/2801.34;
	}
	
	/**
	 * d) Excess air (Economizer outlet base)
	 * XpA14={[DVpO214x(MoDPc+0.7905xMoThACr)]/[MoThACr x(20.95-DVpO214)] } x 100
	 * 
	 * @return
	 */
	private double XpA14() {
		return ((DVpO214 * (MoDPc() + 0.7905 * MoThACr())) / (MoThACr() * (20.95 - DVpO214))) * 100;
	}
	
	/**
	 * e) Excess air (A/H outlet base)
	 * XpA15=100x{[DVpO215 x (MoDPc+0.7905 xMoThACr)]/[MoThACr x(20.95-DVpO215)]}
	 * 
	 * @return
	 */
	private double XpA15() {
		return 100 * ((DVpO215 * (MoDPc() + 0.7905*MoThACr()))/(MoThACr()*(20.95-DVpO215)));
	}
	
	/**
	 * a) leaving A/H 
	 * [MqDA9 = MqThACr x(1+ XpA14/100)]
	 * 
	 * @return
	 */
	private double MqDA9() {
		return MqThACr() * (1 + XpA14() / 100);
	}
	
	/**
	 * b) entering A/H 
	 * [MqDA8 = MqThACr x(1+ XpA15 / 100)]
	 * 
	 * @return
	 */
	private double MqDA8() {
		return MqThACr() * (1 + XpA15() / 100);
	}
	
	/**
	 * c) A/H Outlet gas (undiluted)
	 * 원 수식: TFg15+MnCpA/MnCpFg * (MqFg15/MqFg14-1) * (TFg15-TA8)
	 * 약식 계산 : TFg15 * ( 1+  AL/100) - TA8*AL/100;
	 * @return
	 */
	private double TFgLvCr() {
		//return TFg15 + MnCpA/MnCpFg * (MqFg15/MqFg14-1) * (TFg15-TA8);
		return TFg15 * ( 1+  AL()/100) - TA8*AL()/100; 
		
	}
	
	/**
	 * a) Wet gas from fuel
	 * MqFgF  = (100 - MpAsF - MpUbC) / (100 x HHVF)
	 * 
	 * @return
	 */
	private double MqFgF() {
		return (100 - MpAsF - MpUbC()) / (100 * HHVF);
		
	}
	
	/**
	 * b) Moisture from H2O(Water) in fuel
	 * MqWF = MpWF / (100 x HHVF)
	 * 
	 * @return
	 */
	private double MqWF() {
		return MpWF / (100 * HHVF);
	}
	
	/**
	 * c) Water from combustion of hydrogen in fuel
	 * [MqWH2F = 8.937xMpH2F/(100xHHVF)]
	 * 
	 * @return
	 */
	private double MqWH2F() {
		return 8.937*MpH2F/(100*HHVF);
		
	}
	
	/**
	 * d) Moisture from air at the inlet of A/H
	 * MqWA9=MFrWDA xMqDA9
	 * 
	 * @return
	 */
	private double MqWA9() {
		return MFrWDA() * MqDA9();
	}
	
	/**
	 * d) Moisture from air at the outlet of A/H 
	 * MqWA8 = MFrWDA x MqDA8
	 * 
	 * @return
	 */
	private double MqWA8() {
		return MFrWDA() * MqDA8();
	}
	
	/**
	 * f) Total moisture in flue gas at Economizer outlet 
     * MqWFg14=MqWF+MqWH2F+MqWA9+MqWAdz
	 * 
	 * @return
	 */
	private double MqWFg14() {
		//return MqWF+MqWH2F+MqWA9+MqWAdz; 
		return MqWF() + MqWH2F() + MqWA9();
	}
	
	/**
	 * g) Total moisture in flue gas at A/H outlet
	 * MqWFg15=MqWF+MqWH2F+MqWA8+MqWAdz
	 * 
	 * @return
	 */
	private double MqWFg15() {
		//return MqWF+MqWH2F+MqWA8+MqWAdz;
		return MqWF() + MqWH2F() + MqWA8();
	}
	
	
	/**
	 * a) Wet flue gas at Economizer outlet
	 * MqFg14=MqDA9+MqWA9+MqFgF+MqWAdz
	 * 
	 * @return
	 */
	private double MqFg14() {
		//return MqDA9+MqWA9+MqFgF+MqWAdz;
		return MqDA9() + MqWA9() + MqFgF();
	}
	
	/**
	 * b) Wet flue gas at A/H outlet
	 * MqFg15=MqDA8+MqWA8+MqFgF+MqWAdz
	 * 
	 * @return
	 */
	private double MqFg15() {
		//return MqDA8+MqWA8+MqFgF+MqWAdz;
		return MqDA8() + MqWA8() + MqFgF();
	}
	
	/**
	 * a) Dry flue gas at Economizer outlet
	 * MqDFg14 = MqFg14 -MqWFg14
	 * 
	 * @return
	 */
	private double MqDFg14() {
		
		return MqFg14() - MqWFg14();
	}
	
	/**
	 * b) Dry flue gas at A/H outlet
	 * MqDFg15 = MqFg15 -MqWFg15
	 * 
	 * @return
	 */
	private double MqDFg15() {
		return MqFg15() - MqWFg15();
	}
	
	/**
	 * a) Dry flue gas at Economizer outlet
	 * MoDFg14=MoDPc + MoThACr x (0.7905 + XpA14/100)
	 * 
	 * @return
	 */
	private double MoDFg14() {
		return MoDPc() + MoThACr() * (0.7905 + XpA14()/100);
	}
	
	/**
	 * b) Dry flue gas at A/H outlet
	 * MoDFg15=MoDPc + MoThACr x (0.7905 + XpA15/100)
	 * 
	 * @return
	 */
	private double MoDFg15() {
		return MoDPc() + MoThACr() * (0.7905 + XpA15()/100);
	}
	
	/**
	 * a) A/H leakage ratio
	 *     AL = (MFrFg15 -MFrFg14) / MFrFg14 x 100
	 * @return
	 */
	private double AL() {
		return (MqFg15() / MqFg14() - 1) * 100;
	}
	
	/**
	 * 1) Enthalpy of dry air at TAEn(TA8) with reference to Tref	
	 * HDAEn = 1.005 x (TA8 - TRef)
	 * 
	 * @return
	 */
	private double HDAEn() {
		return 1.005 * (TA8 - TRef);
		
	}
	
	/**
	 * 2) Enthalpy of dry gas at A/H outlet temp TFgLvCr
	 * 
	 * @return
	 */
	private double HDfgLvCr() {
		//=((-0.1231899*10^3+0.4065568*(F91+273.15) +0.579505*10^(-5)*(F91+273.15)^2+0.6331121*10^(-7)*(F91+273.15)^3-0.2924434*10^(-10)*(F91+273.15)^4+0.2491009*10^(-14)*(F91+273.15)^5)*2.326)
		return (-0.1231899*Math.pow(10, 3)+0.4065568*(TFgLvCr()+273.15) + 0.579505*Math.pow(10,-5)*Math.pow((TFgLvCr()+273.15),2)+0.6331121*Math.pow(10, -7)*Math.pow((TFgLvCr()+273.15),3)-0.2924434*Math.pow(10,-10)*Math.pow((TFgLvCr()+273.15),4)+0.2491009*Math.pow(10,-14)*Math.pow((TFgLvCr()+273.15),5))*2.326;
	}
	
	/**
	 * a) Enthalpy of steam at TFgLvCr & 1 psia
	 * 
	 * @return
	 */
	private double HStLvCr() {
		//(0.4329*(F91*1.8+32)+3.958*10^-5*(F91*1.8+32)^2+1062.2)*2.326
		return (0.4329*(TFgLvCr()*1.8+32)+3.958*Math.pow(10,-5)*Math.pow((TFgLvCr()*1.8+32),2)+1062.2)*2.326;
	}
	
	
	/**
	 * b) Enthalpy of water at Tref & 1 psia
	 * 
	 * 
	 * @return
	 */
	private double HWRe() {
		return ((TRef*1.8+32)-32)*2.326;
	}
	
	/**
	 * c) Enthalpy of water vapor at TFgLvCr with reference to Tref
	 * 
	 * @return
	 */
	private double HWvLvCr() {
		// =((-0.2394034*10^3+0.8274589*(F91+273.15)-0.1797539*10^(-3)*(F91+273.15)^2+0.3934614*10^(-6)*(F91+273.15)^3-0.2415873*10^(-9)*(F91+273.15)^4+0.6069264*10^(-13)*(F91+273.15)^5)*2.326)
		return (-0.2394034*Math.pow(10,3)+0.8274589*(TFgLvCr()+273.15)-0.1797539*Math.pow(10,-3)*Math.pow((TFgLvCr()+273.15),2)+0.3934614*Math.pow(10,-6)*Math.pow((TFgLvCr()+273.15),3)-0.2415873*Math.pow(10,-9)*Math.pow((TFgLvCr()+273.15),4)+0.6069264*Math.pow(10,-13)*Math.pow((TFgLvCr()+273.15),5))*2.326;
	
	}
	
	/**
	 * d) Enthalpy of water vapor at TAEn(TA8) with reference to Tref
	 * 
	 * @return
	 */
	private double HWvEn() {
		//=(-0.2394034*10^3+0.8274589*(F54+273.15)-0.1797539*10^-3*(F54+273.15)^2+0.3934614*10^-6*(F54+273.15)^3-0.2415873*10^-9*(F54+273.15)^4+0.6069264*10^-13*(F54+273.15)^5)*2.326
		return (-0.2394034*Math.pow(10,3)+0.8274589*(TA8+273.15)-0.1797539*Math.pow(10,-3)*Math.pow((TA8+273.15),2)+0.3934614*Math.pow(10,-6)*Math.pow((TA8+273.15),3)-0.2415873*Math.pow(10,-9)*Math.pow((TA8+273.15),4)+0.6069264*Math.pow(10,-13)*Math.pow((TA8+273.15),5))*2.326;
	}
	
	/**
	 * a) Enthalpy of bottom ash with reference to Tref
	 * 
	 * @return
	 */
	private double HRsb() {
		return (0.16*(TRsb*1.8+32)+0.000109*Math.pow((TRsb*1.8+32),2)-0.00000002843*Math.pow((TRsb*1.8+32),3)-12.95)*2.326;
	}
	
	/**
	 * b) Enthalpy of economizer hopper ash with reference to Tref
	 * 
	 * @return
	 */
	private double HRse() {
		return (0.16*(Trse()*1.8+32)+0.000109*Math.pow((Trse()*1.8+32),2)-0.00000002843*Math.pow((Trse()*1.8+32),3)-12.95)*2.326;	
	}
	
	/**
	 * c) Enthalpy of fly ash with reference to Tref
	 * 
	 * @return
	 */
	private double HRsf() {
		return (0.16*(TRsf()*1.8+32)+0.000109*Math.pow((TRsf()*1.8+32),2)-0.00000002843*Math.pow((TRsf()*1.8+32),3)-12.95)*2.326;
	}
	
	/**
	 * a) Temperature of Coal
	 * 
	 * @return
	 */
	private double Tcoal() {
		return TA6;
	}
	
	/**
	 * • Enthalpy of fixed carbon
	 * 
	 * @return
	 */
	private double HFc() {
		return (0.152*(1.8*Tcoal()+32)+0.000195*Math.pow((1.8*Tcoal()+32),2)-12.86)*2.326;
	}
	
	/**
	 * • Enthalpy of volatile matter 1
	 * 
	 * @return
	 */
	private double HVm1() {
		return (0.38*(1.8*Tcoal()+32)+0.000225*Math.pow((1.8*Tcoal()+32),2)-30.594)*2.326;
	}
	
	/**
	 * • Enthalpy of volatile matter 2
	 * 
	 * @return
	 */
	private double HVm2() {
		return (0.7*(1.8*Tcoal()+32)+0.00017*Math.pow((1.8*Tcoal()+32),2)-54.908)*2.326;
	}
	
	/**
	 * • Enthalpy of ash
	 * 
	 * @return
	 */
	private double HRs() {
		//=(0.17*(1.8*F141+32)+0.00008*(1.8*F141+32)^2-13.564)*2.326
		return (0.17*(1.8*Tcoal()+32)+0.00008*Math.pow((1.8*Tcoal()+32),2)-13.564)*2.326;
	}
	
	/**
	 * • Enthalpy of water
	 * 
	 * @return
	 */
	private double HW() {
		return ((1.8*Tcoal()+32)-77)*2.326;
	}
	
	/**
	 * • Mass fraction of VM on a dry and ash-free basis
	 * 
	 * @return
	 */
	private double MFrVmCr() {
		return (MFrVm/100)/(1-MFrAsF/100-MFrWF/100);
	}
	
	/**
	 * •  Primary volatile matter
	 * 
	 * @return
	 */
	private double MFrVm1() {
		return MFrVmCr()>0.1 ? MFrVm/100-MFrVm2() : 0;
	}
	
	/**
	 * •  Secondary volatile matter 
	 * 
	 * @return
	 */
	private double MFrVm2() {
		return MFrVmCr() > 0.1 ? 0.1*(1-MFrAsF/100-MFrWF/100) : MFrVm/100;
	}
	
	/**
	 * • Enthalpy of Coal
	 * 
	 * @return
	 */
	private double HFEn() {
		return (MFrFc/100)*HFc()+(MFrAsF/100)*HRs()+(MFrWF/100)*HW()+ MFrVm1()*HVm1()+MFrVm2()*HVm2();
		//return MFrFc * HFc() + MFrVm1() *HVm1() + MFrVm2() * HVm2() + MFrWF * HW() + MFrAsF * HRs();
	}
	
	/**
	 * 1) Heat in dry Flue gas
	 * 
	 * @return
	 */
	public double QpLDFg() {
		return round(100 * MqDFg14() * HDfgLvCr(), 3);
	}
	
	/**
	 * 2) Moisture from burning of hydrogen
	 * 
	 * @return
	 */
	private double QpLH2F() {
		return round(100 * MqWH2F() * (HStLvCr() - HWRe()), 3);
	}
	
	/**
	 * 3) Moisture in the "as fired fuel"
	 * 
	 * @return
	 */
	private double QpLWF() {
		return round(100 * MqWF() * (HStLvCr() - HWRe()), 3);
	}
	
	/**
	 * 4) Moisture in the air 
	 * 
	 * @return
	 */
	private double QpLWA() {
		return round(100 * MFrWDA() * MqDA9() * HWvLvCr(), 3);
	}
	
	/**
	 * 5) Unburned carbon in refuse
	 * 
	 * @return
	 */
	private double QpLUbC() {
		return round(MpUbC() * (33700/HHVF), 3);
	}
	
	/**
	 * 6) Sensible heat of residue loss
	 * 
	 * @return
	 */
	private double QpLRs() {
		return round(100 * ((MqRsb() * HRsb())+ (MqRse() * HRse()) +(MqRsf() * HRsf())), 3);
	}
	
	/**
	 * 7) Wet ash pit loss
	 * 
	 * @return
	 */
	private double QrLAp() {
		//return QrAp * ApAf;
		return round(QrAp*ApAf/(HHVF*MrF/3600)*100, 3);
	}
	
	/**
	 * 8) Carbon monoxide in flue gas loss
	 * 
	 * @return
	 */
	private double QpLCO() {
		return round((DVpCO14 * MoDFg14() * 28.01)*(10111/HHVF), 3);
	}
	
	/**
	 * 11) Total Loss
	 * 
	 * @return
	 */
	public double QpL() { 
		return QpLDFg() + QpLH2F() + QpLWF() + QpLWA() +QpLUbC() + QpLRs() + QrLAp() + QpLCO() + QpLSrc + QpLum;
	}
	
	/**
	 * 1) Heat in entering dry air
	 * 
	 * @return
	 */
	private double QpBDA() {
		return round(100 * MqDA9() * HDAEn(), 3);
	}
	
	/**
	 * 2) Moisture in entering air 
	 * 
	 * @return
	 */
	private double QpBWA() {
		return round(100 * MFrWDA() * MqDA9() * HWvEn(), 3);
	}
	
	/**
	 * 3) Sensible heat in fuel
	 * 
	 * @return
	 */
	private double QpBF() {
		return round(100 * (HFEn() / HHVF), 3);
	}
	
	/**
	 * 4) Auxiliary drives within the envelope
	 * 
	 * @return
	 */
	private double QpBX() {
		return round(100 * (P * 3600 * η/100) / (MrF * HHVF), 3);
	}
	
	/**
	 * 5) Total heat credit
	 * 
	 * @return
	 */
	public double QpB() {
		return QpBDA() + QpBWA() +  QpBF() + QpBX();
	}
	
	/**
	 * 1) Fuel efficiency
	 * 
	 * @return
	 */
	public double ηfuel_Fuel() {
		return round(100- QpL() + QpB(),3);
	}
	
	/**
	 * 2) Gross efficiency
	 * 
	 * @return
	 */
	public double ηfuel_Gross() {
		return round(100-100* QpL() / (100+QpB()), 3);
	}
	
		
	@Override
	public String toString() {
		return "BoilerEfficiencyCalculation [MpRsf(80.0)=" + MpRsf() + ", MpCRs(1.15)=" + MpCRs() + ", MFrRs(0.28)=" + MFrRs()
				+ ", Trse(350.76)=" + Trse() + ", TRsf(146.56)=" + TRsf() + ", MqRsb(2.38)=" + MqRsb() + ", MqRse(7.95)=" + MqRse()
				+ ", MqRsf(1.27)=" + MqRsf() + ", MpUbC(0.32)=" + MpUbC() + ", MpCb(45.75)=" + MpCb() + ", Tdb(25.95)=" + Tdb()
				+ ", PsWvTdb(0.03)=" + PsWvTdb() + ", PpWvA(0.02)=" + PpWvA() + ", MFrWDA(0.01)=" + MFrWDA() + ", MFrWA(0.01)="
				+ MFrWA() + ", MFrThACr(5.89)=" + MFrThACr() + ", MoThACr(0.20)=" + MoThACr() + ", MqThACr(3.33)=" + MqThACr()
				+ ", MoDPc(0.04)=" + MoDPc() + ", XpA14(19.13)=" + XpA14() + ", XpA15(22.0)=" + XpA15() + ", MqDA9(3.97)=" + MqDA9()
				+ ", MqDA8(4.07)=" + MqDA8() + ", TFgLvCr(146.56)=" + TFgLvCr() + ", MqFgF(4.07)=" + MqFgF() + ", MqWF(7.93)=" + MqWF()
				+ ", MqWH2F(1.42)=" + MqWH2F() + ", MqWA9(5.00)=" + MqWA9() + ", MqWA8(5.13)=" + MqWA8() + ", MqWFg14(2.71)="
				+ MqWFg14() + ", MqWFg15(2.72)=" + MqWFg15() + ", MqFg14(4.43)=" + MqFg14() + ", MqFg15(4.53)=" + MqFg15()
				+ ", MqDFg14(4.16)=" + MqDFg14() + ", MqDFg15(4.26)=" + MqDFg15() + ", MoDFg14(0.24)=" + MoDFg14() + ", MoDFg15(0.24)="
				+ MoDFg15() + ", AL(2.20)=" + AL() + ", HDAEn(7.67)=" + HDAEn() + ", HDfgLvCr(121.59)=" + HDfgLvCr() + ", HStLvCr(2776.60)="
				+ HStLvCr() + ", HWRe(104.67)=" + HWRe() + ", HWvLvCr(229.37)=" + HWvLvCr() + ", HWvEn(14.22)=" + HWvEn() + ", HRsb(1206.40)="
				+ HRsb() + ", HRse(309.02)=" + HRse() + ", HRsf(100.45)=" + HRsf() + ", Tcoal(25.95)=" + Tcoal() + ", HFc(0.72)=" + HFc()
				+ ", HVm1(1.65)=" + HVm1() + ", HVm2(2.89)=" + HVm2() + ", HRs(0.73)=" + HRs() + ", HW(3.98)=" + HW() + ", MFrVmCr(0.35)="
				+ MFrVmCr() + ", MFrVm1(0.16)=" + MFrVm1() + ", MFrVm2(0.06)=" + MFrVm2() + ", HFEn(1.10)=" + HFEn() + ", QpLDFg(5.06)="
				+ QpLDFg() + ", QpLH2F(3.80)=" + QpLH2F() + ", QpLWF(2.12)=" + QpLWF() + ", QpLWA(0.11)=" + QpLWA() + ", QpLUbC(0.61)="
				+ QpLUbC() + ", QpLRs(0.44)=" + QpLRs() + ", QrLAp(0.01)=" + QrLAp() + ", QpLCO(0.04)=" + QpLCO() + ", QpL(12.51)="
				+ QpL() + ", QpBDA(0.30)=" + QpBDA() + ", QpBWA(0.007)=" + QpBWA() + ", QpBF(0.006)=" + QpBF() + ", QpBX(0.0)=" + QpBX()
				+ ", QpB(0.318)=" + QpB() + ", ηfuel_Fuel(87.807)=" + ηfuel_Fuel() + ", ηfuel_Gross(87.529)=" + ηfuel_Gross() + "]";
	}
	public static void main(String[] args) {
		BoilerEfficiencyCalculation b = new BoilerEfficiencyCalculation(new BoilerEfficiencyFactor());
		//System.out.println(b.HHVF);
		//System.out.println(b.HFEn());
		//System.out.println(b.QpLDFg());
		//System.out.println(b.QpLRs());
		System.out.println(b.ηfuel_Gross());
		System.out.println(b.ηfuel_Fuel());
		//double newB = (double)Math.round(b.PsWvTdb(20.0) * 10000d) / 10000d;
		//System.out.println(b.PsWvTdb(18.0));
		System.out.println(b.toString());
	}
}
