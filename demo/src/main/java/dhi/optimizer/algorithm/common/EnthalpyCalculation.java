package dhi.optimizer.algorithm.common;

public class EnthalpyCalculation {

	private final double rgas_water = 461.526; // gas constant in J/(kg K)
	private final double tc_water = 647.096; // critical temperature in K
	private final double dc_water = 322; // critical density in kg/m**3

	// Initialize coefficients and exponents for region 1
	private int[] ireg1 = { 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 8, 8, 21, 23,
			29, 30, 31, 32 };
	private int[] jreg1 = { -2, -1, 0, 1, 2, 3, 4, 5, -9, -7, -1, 0, 1, 3, -3, 0, 1, 3, 17, -4, 0, 6, -5, -2, 10, -8,
			-11, -6, -29, -31, -38, -39, -40, -41 };
	private double[] nreg1 = { 0.14632971213167, -0.84548187169114, -3.756360367204, 3.3855169168385, -0.95791963387872,
			0.15772038513228, -0.016616417199501, 8.1214629983568E-04, 2.8319080123804E-04, -6.0706301565874E-04,
			-0.018990068218419, -0.032529748770505, -0.021841717175414, -5.283835796993E-05, -4.7184321073267E-04,
			-3.0001780793026E-04, 4.7661393906987E-05, -4.4141845330846E-06, -7.2694996297594E-16, -3.1679644845054E-05,
			-2.8270797985312E-06, -8.5205128120103E-10, -2.2425281908E-06, -6.5171222895601E-07, -1.4341729937924E-13,
			-4.0516996860117E-07, -1.2734301741641E-09, -1.7424871230634E-10, -6.8762131295531E-19, 1.4478307828521E-20,
			2.6335781662795E-23, -1.1947622640071E-23, 1.8228094581404E-24, -9.3537087292458E-26 };

	// Initialize coefficients and exponents for region 2
	private int[] j0reg2 = { 0, 1, -5, -4, -3, -2, -1, 2, 3 };
	private double[] n0reg2 = { -9.6927686500217, 10.086655968018, -0.005608791128302, 0.071452738081455,
			-0.40710498223928, 1.4240819171444, -4.383951131945, -0.28408632460772, 0.021268463753307 };
	private int[] ireg2 = { 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 5, 6, 6, 6, 7, 7, 7, 8, 8, 9, 10, 10,
			10, 16, 16, 18, 20, 20, 20, 21, 22, 23, 24, 24, 24 };
	private int[] jreg2 = { 0, 1, 2, 3, 6, 1, 2, 4, 7, 36, 0, 1, 3, 6, 35, 1, 2, 3, 7, 3, 16, 35, 0, 11, 25, 8, 36, 13,
			4, 10, 14, 29, 50, 57, 20, 35, 48, 21, 53, 39, 26, 40, 58 };
	private double[] nreg2 = { -1.7731742473213E-03, -0.017834862292358, -0.045996013696365, -0.057581259083432,
			-0.05032527872793, -3.3032641670203E-05, -1.8948987516315E-04, -3.9392777243355E-03, -0.043797295650573,
			-2.6674547914087E-05, 2.0481737692309E-08, 4.3870667284435E-07, -3.227767723857E-05, -1.5033924542148E-03,
			-0.040668253562649, -7.8847309559367E-10, 1.2790717852285E-08, 4.8225372718507E-07, 2.2922076337661E-06,
			-1.6714766451061E-11, -2.1171472321355E-03, -23.895741934104, -5.905956432427E-18, -1.2621808899101E-06,
			-0.038946842435739, 1.1256211360459E-11, -8.2311340897998, 1.9809712802088E-08, 1.0406965210174E-19,
			-1.0234747095929E-13, -1.0018179379511E-09, -8.0882908646985E-11, 0.10693031879409, -0.33662250574171,
			8.9185845355421E-25, 3.0629316876232E-13, -4.2002467698208E-06, -5.9056029685639E-26, 3.7826947613457E-06,
			-1.2768608934681E-15, 7.3087610595061E-29, 5.5414715350778E-17, -9.436970724121E-07 };

	// Initialize coefficients and exponents for region 3
	private int[] ireg3 = { 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 6,
			6, 6, 7, 8, 9, 9, 10, 10, 11 };
	private int[] jreg3 = { 0, 0, 1, 2, 7, 10, 12, 23, 2, 6, 15, 17, 0, 2, 6, 7, 22, 26, 0, 2, 4, 16, 26, 0, 2, 4, 26,
			1, 3, 26, 0, 2, 26, 2, 26, 2, 26, 0, 1, 26 };
	private double[] nreg3 = { 1.0658070028513, -15.732845290239, 20.944396974307, -7.6867707878716, 2.6185947787954,
			-2.808078114862, 1.2053369696517, -8.4566812812502E-03, -1.2654315477714, -1.1524407806681,
			0.88521043984318, -0.64207765181607, 0.38493460186671, -0.85214708824206, 4.8972281541877, -3.0502617256965,
			0.039420536879154, 0.12558408424308, -0.2799932969871, 1.389979956946, -2.018991502357,
			-8.2147637173963E-03, -0.47596035734923, 0.0439840744735, -0.44476435428739, 0.90572070719733,
			0.70522450087967, 0.10770512626332, -0.32913623258954, -0.50871062041158, -0.022175400873096,
			0.094260751665092, 0.16436278447961, -0.013503372241348, -0.014834345352472, 5.7922953628084E-04,
			3.2308904703711E-03, 8.0964802996215E-05, -1.6557679795037E-04, -4.4923899061815E-05 };

	// Initialize coefficients for region 4
	private double[] nreg4 = { 1167.0521452767, -724213.16703206, -17.073846940092, 12020.82470247, -3232555.0322333,
			14.91510861353, -4823.2657361591, 405113.40542057, -0.23855557567849, 650.17534844798 };

	// Initialize coefficients for boundary equation
	private double[] nbound = { 348.05185628969, -1.1671859879975, 1.0192970039326E-03, 572.54459862746,
			13.91883977887 };

	/**
	 * boundary temperature between regions 2 and 3 tBound in K pressure in bar
	 * 
	 * tBound = -1: pressure outside range
	 * 
	 * @param pressure
	 * @return
	 */
	private double tBound(double pressure) {
		double tBound = 0.0;
		if (pressure < 165.292 || pressure > 1000) {
			tBound = -1;
		} else {
			tBound = nbound[3] + Math.pow(((0.1 * pressure - nbound[4]) / nbound[2]), 0.5);
		}
		return tBound;
	}

	/**
	 * specific enthalpy of water or steam enthalpyW in kJ/kg temperature in K
	 * pressure in bar
	 * 
	 * enthalpyW = -1: temperature and/or pressure outside range
	 * 
	 * @param temperature
	 * @param pressure
	 * @return
	 */
	public double enthalpyW(double temperature, double pressure) {
		double enthalpyW;
		double density;
		if (temperature >= 273.15 && temperature <= 623.15 && pressure >= pSatW(temperature) && pressure <= 1000) {
			enthalpyW = enthalpyreg1(temperature, pressure);
		} else if ((temperature >= 273.15 && temperature <= 623.15 && pressure > 0 && pressure <= pSatW(temperature))
				|| (temperature >= 623.15 && temperature <= 863.15 && pressure > 0 && pressure <= pBound(temperature))
				|| (temperature >= 863.15 && temperature <= 1073.15 && pressure > 0 && pressure <= 1000)) {
			enthalpyW = enthalpyreg2(temperature, pressure);
		} else if (temperature >= 623.15 && temperature <= tBound(pressure) && pressure >= pBound(temperature)
				&& pressure <= 1000) {
			density = densreg3(temperature, pressure);
			enthalpyW = enthalpyreg3(temperature, density);
		} else {
			enthalpyW = -1;
		}

		return enthalpyW;
	}

	/**
	 * specific enthalpy in region 1 enthalpyreg1 in kJ/kg temperature in K pressure
	 * in bar
	 * 
	 * @param temperature
	 * @param pressure
	 * @return
	 */
	private double enthalpyreg1(double temperature, double pressure) {
		double tau = 1386 / temperature;
		double pi = 0.1 * pressure / 16.53;
		return 0.001 * rgas_water * temperature * tau * gammataureg1(tau, pi);
	}

	/**
	 * specific enthalpy in region 2 enthalpyreg1 in kJ/kg temperature in K pressure
	 * in bar
	 * 
	 * @param temperature
	 * @param pressure
	 * @return
	 */
	private double enthalpyreg2(double temperature, double pressure) {
		double tau = 540 / temperature;
		double pi = 0.1 * pressure;
		return 0.001 * rgas_water * temperature * tau * (gamma0taureg2(tau, pi) + gammartaureg2(tau, pi));
	}

	/**
	 * specific enthalpy in region 3 enthalpyreg1 in kJ/kg temperature in K pressure
	 * in bar
	 * 
	 * @param temperature
	 * @param density
	 * @return
	 */
	private double enthalpyreg3(double temperature, double density) {
		double tau = tc_water / temperature;
		double delta = density / dc_water;
		return 0.001 * rgas_water * temperature * (tau * fitaureg3(tau, delta) + delta * fideltareg3(tau, delta));
	}

	/**
	 * First derivative of fundamental equation in tau for region 1
	 * 
	 * @param tau
	 * @param pi
	 * @return
	 */
	private double gammataureg1(double tau, double pi) {
		double gammataureg1 = 0.0;
		for (int i = 0; i < 34; i++) {
			gammataureg1 = gammataureg1
					+ nreg1[i] * Math.pow((7.1 - pi), ireg1[i]) * jreg1[i] * Math.pow((tau - 1.222), (jreg1[i] - 1));
		}

		return gammataureg1;
	}

	/**
	 * First derivative in tau of ideal-gas part of fundamental equation for region
	 * 2
	 * 
	 * @param tau
	 * @param pi
	 * @return
	 */
	private double gamma0taureg2(double tau, double pi) {
		double gamma0taureg2 = 0.0;
		for (int i = 0; i < 9; i++) {
			gamma0taureg2 = gamma0taureg2 + n0reg2[i] * j0reg2[i] * Math.pow(tau, (j0reg2[i] - 1));
		}

		return gamma0taureg2;
	}

	/**
	 * First derivative in tau of residual part of fundamental equation for region 2
	 * 
	 * @param tau
	 * @param pi
	 * @return
	 */
	private double gammartaureg2(double tau, double pi) {
		double gammartaureg2 = 0.0;
		for (int i = 0; i < 43; i++) {
			gammartaureg2 = gammartaureg2
					+ nreg2[i] * Math.pow(pi, ireg2[i]) * jreg2[i] * Math.pow((tau - 0.5), (jreg2[i] - 1));
		}
		return gammartaureg2;
	}

	/**
	 * Determine density in region 3 iteratively using Newton method densreg3 in
	 * kg/m^3 temperature in K pressure in bar
	 * 
	 * densreg3 = -2: not converged
	 * 
	 * 
	 * @param temperature
	 * @param pressure
	 * @return
	 */
	private double densreg3(double temperature, double pressure) {
		double densold = 0.0;
		double delta = 0.0;
		double derivprho = 0.0;
		double densnew = 0.0;
		double diffdens = 0.0;

		if (temperature < tc_water && pressure < pSatW(temperature)) {
			densold = 100;
		} else {
			densold = 600;
		}
		double tau = tc_water / temperature;

		for (int j = 0; j < 1000; j++) {
			delta = densold / dc_water;
			derivprho = rgas_water * temperature / dc_water * (2 * densold * fideltareg3(tau, delta)
					+ Math.pow(densold, 2) / dc_water * fideltadeltareg3(tau, delta));
			densnew = densold + (pressure * 100000
					- rgas_water * temperature * Math.pow(densold, 2) / dc_water * fideltareg3(tau, delta)) / derivprho;
			diffdens = Math.abs(densnew - densold);
			if (diffdens < 0.000005) {
				return densnew;
			}
			densold = densnew;
		}
		return -2;
	}

	/**
	 * saturation pressure of water pSatW in bar temperature in K
	 * 
	 * pSatW = -1: temperature outside range
	 * 
	 * @param temperature
	 * @return
	 */
	private double pSatW(double temperature) {
		double pSatW = 0.0;
		double del = 0.0;
		double aco = 0.0;
		double bco = 0.0;
		double cco = 0.0;
		if (temperature < 273.15 || temperature > 647.096) {
			pSatW = -1;
		} else {
			del = temperature + nreg4[8] / (temperature - nreg4[9]);
			aco = Math.pow(del, 2) + nreg4[0] * del + nreg4[1];
			bco = nreg4[2] * Math.pow(del, 2) + nreg4[3] * del + nreg4[4];
			cco = nreg4[5] * Math.pow(del, 2) + nreg4[6] * del + nreg4[7];
			pSatW = Math.pow(2 * cco / (-bco + Math.pow(Math.pow(bco, 2) - 4 * aco * cco, 0.5)), 4) * 10;
		}
		return pSatW;
	}

	/**
	 * boundary pressure between regions 2 and 3 pBound in bar temperature in K
	 * 
	 * pBound = -1: temperature outside range
	 * 
	 * @param temperature
	 * @return
	 */
	private double pBound(double temperature) {
		double pBound = 0.0;
		if (temperature < 623.15 || temperature > 863.15) {
			pBound = -1;
		} else {
			pBound = (nbound[0] + nbound[1] * temperature + nbound[2] * Math.pow(temperature, 2)) * 10;
		}
		return pBound;
	}

	/**
	 * First derivative in delta of fundamental equation for region 3
	 * 
	 * @param tau
	 * @param delta
	 * @return
	 */
	private double fideltareg3(double tau, double delta) {
		double fideltareg3 = nreg3[0] / delta;
		for (int i = 1; i < 40; i++) {
			fideltareg3 = fideltareg3 + nreg3[i] * ireg3[i] * Math.pow(delta, (ireg3[i] - 1)) * Math.pow(tau, jreg3[i]);
		}
		return fideltareg3;
	}

	/**
	 * Second derivative in delta of fundamental equation for region 3
	 * 
	 * @param tau
	 * @param delta
	 * @return
	 */
	private double fideltadeltareg3(double tau, double delta) {
		double fideltadeltareg3 = -nreg3[0] / Math.pow(delta, 2);
		for (int i = 1; i < 40; i++) {
			fideltadeltareg3 = fideltadeltareg3
					+ nreg3[i] * ireg3[i] * (ireg3[i] - 1) * Math.pow(delta, (ireg3[i] - 2)) * Math.pow(tau, jreg3[i]);
		}
		return fideltadeltareg3;
	}

	/**
	 * First derivative in tau of fundamental equation for region 3
	 * 
	 * @param tau
	 * @param delta
	 * @return
	 */
	private double fitaureg3(double tau, double delta) {
		double fitaureg3 = 0.0;
		for (int i = 1; i < 40; i++) {
			fitaureg3 = fitaureg3 + nreg3[i] * Math.pow(delta, ireg3[i]) * jreg3[i] * Math.pow(tau, (jreg3[i] - 1));
		}
		return fitaureg3;
	}

	public static void main(String[] args) {
		EnthalpyCalculation ec = new EnthalpyCalculation();
		System.out.println(ec.enthalpyW(610.806214, 46.643));
	}

}
