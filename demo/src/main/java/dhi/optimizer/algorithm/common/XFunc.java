package dhi.optimizer.algorithm.common;

import java.util.Random;

public class XFunc {

	static public double findMinValue(double[] data) {
		int dataLength = data.length;
		double minVal = 0.0;

		if (dataLength > 0) {
			minVal = data[0];
			for (int i = 0; i < dataLength; i++) {
				if (minVal > data[i]) {
					minVal = data[i];
				}
			}
		}

		return minVal;
	}
	
	static public int findMinIndex(double[] data) {
		int dataLength = data.length;
		double minVal = 0.0;
		int minIndex = 0;

		if (dataLength > 0) {
			minVal = data[0];
			for (int i = 0; i < dataLength; i++) {
				if (minVal > data[i]) {
					minVal = data[i];
					minIndex = i;
				}
			}
		}

		return minIndex;
	}
	
	static public double randomRange(double rangeMin, double rangeMax) {
		Random r = new Random();
		double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		return randomValue;
	}
	
}
