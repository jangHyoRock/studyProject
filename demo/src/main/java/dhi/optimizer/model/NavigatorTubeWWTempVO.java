package dhi.optimizer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dhi.optimizer.model.json.KeyValue;

/**
 * Tube Water Wall Temperature Class. <br>
 *  : Null 값을 제외한 온도값을 부터 평균값 계산. <br>
 *  : 1차 평균값 계산 후  평균 온도에서 “Doosan Mode-Settings”의 “Metal Temp. Dev. For  Filtering” 설정 값보다 크거나 작은 값을 제외한 평균값 재 계산.
 */
public class NavigatorTubeWWTempVO {	
	private List<Double> frontTempList;
	private List<Double> rearTempList;
	private List<Double> leftTempList;
	private List<Double> rightTempList;
	
	private double frontTempAvgNoFilter;
	private double rearTempAvgNoFilter;
	private double leftTempAvgNoFilter;
	private double rightTempAvgNoFilter;
	
	private double frontTempAvg;
	private double rearTempAvg;
	private double leftTempAvg;
	private double rightTempAvg;
		
	private double deviationRangeRate = 0;
	private double allowableTempDev = 0;
	
	public NavigatorTubeWWTempVO(List<Double> frontTemp, List<Double> rearTemp, List<Double> leftTemp, List<Double> rightTemp, double deviationRangeRate, double allowableTempDev) {
		
		this.frontTempList = frontTemp;
		this.rearTempList = rearTemp;
		this.leftTempList = leftTemp;
		this.rightTempList = rightTemp;
		this.deviationRangeRate = deviationRangeRate;
		this.allowableTempDev = allowableTempDev;
				
		this.frontTempAvgNoFilter = this.avg(frontTemp);
		this.rearTempAvgNoFilter = this.avg(rearTemp);
		this.leftTempAvgNoFilter = this.avg(leftTemp);
		this.rightTempAvgNoFilter = this.avg(rightTemp);
				
		this.frontTempAvg = this.avg(frontTemp,
				this.frontTempAvgNoFilter - this.frontTempAvgNoFilter * (this.deviationRangeRate / 100),
				this.frontTempAvgNoFilter + this.frontTempAvgNoFilter * (this.deviationRangeRate / 100));
		
		this.rearTempAvg = this.avg(rearTemp,
				this.rearTempAvgNoFilter - this.rearTempAvgNoFilter * (this.deviationRangeRate / 100),
				this.rearTempAvgNoFilter + this.rearTempAvgNoFilter * (this.deviationRangeRate / 100));
		
		this.leftTempAvg = this.avg(leftTemp,
				this.leftTempAvgNoFilter - this.leftTempAvgNoFilter * (this.deviationRangeRate / 100),
				this.leftTempAvgNoFilter + this.leftTempAvgNoFilter * (this.deviationRangeRate / 100));
		
		this.rightTempAvg = this.avg(rightTemp,
				this.rightTempAvgNoFilter - this.rightTempAvgNoFilter * (this.deviationRangeRate / 100),
				this.rightTempAvgNoFilter + this.rightTempAvgNoFilter * (this.deviationRangeRate / 100));
	}

	public double getFrontTempAvg() {
		return this.frontTempAvg;
	}
	
	public double getRearTempAvg() {
		return this.rearTempAvg;
	}

	public double getLeftTempAvg() {
		return this.leftTempAvg;
	}
	
	public double getRightTempAvg() {
		return rightTempAvg;
	}	
	
	public double getFrontTempAvgNoFilter() {
		return frontTempAvgNoFilter;
	}
	
	public double getRearTempAvgNoFilter() {
		return rearTempAvgNoFilter;
	}

	public double getLeftTempAvgNoFilter() {
		return leftTempAvgNoFilter;
	}

	public double getRightTempAvgNoFilter() {
		return rightTempAvgNoFilter;
	}

	/**
	 * 벽면 온도 편차에 따른 우선 순위 함수.(AA, AB-FG)
	 * @return 우선순위 List.
	 */
	public List<KeyValue> getDeviationPriorityList() {
		
		// 1. 벽면 전체 평균 값을 구하고
		// 2. 전체 평균에서 각 단면의 편자가 가장 큰 순으로 우선 순위 정의함.
		double metalTempAvg = (this.frontTempAvg + this.rearTempAvg + this.leftTempAvg + this.rightTempAvg) / 4;
		double metalTempFrontDev = this.frontTempAvg - metalTempAvg;
		double metalTempRearDev = this.rearTempAvg - metalTempAvg;
		double metalTempLeftDev = this.leftTempAvg - metalTempAvg;
		double metalTempRightDev = this.rightTempAvg - metalTempAvg;

		List<KeyValue> keyValueList = new ArrayList<KeyValue>();
		keyValueList.add(new KeyValue("Front", metalTempFrontDev));
		keyValueList.add(new KeyValue("Rear", metalTempRearDev));
		keyValueList.add(new KeyValue("Left", metalTempLeftDev));
		keyValueList.add(new KeyValue("Right", metalTempRightDev));
		Collections.sort(keyValueList, new Comparator<KeyValue>(){
			@Override
			public int compare(KeyValue a, KeyValue b) {
				return a.getDoubleValue() > b.getDoubleValue() ? -1 : a.getDoubleValue() < b.getDoubleValue() ? 1 : 0;
			}
		});

		// 가장 큰 온도편차의 단면의 값이  Allowable Metal Temp.Dev 보다 큰 경우 사용 한다. 
		if (keyValueList.get(0).getDoubleValue() <= this.allowableTempDev)
			return null;

		return keyValueList;
	}
	
	/**
	 * 벽면 온도 High, Low 온도 값에 따른 우선 순위 함수. (GG) 
	 * @return 우선선위 List
	 */
	public List<KeyValue> getHighLowPriorityList() {
		List<KeyValue> keyValueList = new ArrayList<KeyValue>();
		keyValueList.add(new KeyValue("Front", this.getHighLow(this.frontTempList, this.frontTempAvgNoFilter, true)));
		keyValueList.add(new KeyValue("Rear", this.getHighLow(this.rearTempList, this.rearTempAvgNoFilter, false)));
		keyValueList.add(new KeyValue("Left", this.getHighLow(this.leftTempList, this.leftTempAvgNoFilter, false)));
		keyValueList.add(new KeyValue("Right", this.getHighLow(this.rightTempList, this.rightTempAvgNoFilter, true)));			
		return keyValueList;
	}

	private double avg(List<Double> temps) {
		double tempCount = 0;
		double tempSum = 0;
		for (Double temp : temps) {
			if (temp != null) {
				tempCount++;
				tempSum += temp;
			}
		}

		return tempCount > 0 ? tempSum / tempCount : 0;
	}

	private double avg(List<Double> temps, double rangeMin, double rangeMax) {
		double tempCount = 0;
		double tempSum = 0;
		for (Double temp : temps) {
			if (temp != null) {
				if (temp >= rangeMin && temp <= rangeMax) {
					tempCount++;
					tempSum += temp;
				}
			}
		}

		return tempCount > 0 ? tempSum / tempCount : 0;
	}
	
	/**
	 * 벽면의 우측에서 Min, Max 값을 추출함.(가운데를 기준으로 우축) <br>
	 * 평균온도와 각각 Min, Max 절대값을 구하고, 그 값이 allowableTempDev보다 크고, 편차가 큰쪽의  Low, High 내보냄.
	 * Front, Right => 좌-> 우
	 * Rear, Left => 우 -> 좌
	 */
	private String getHighLow(List<Double> tempValues, double tempAvgNoFilter, boolean isLeftToRightDirection) {
		String result = "";		
		double maxValue = Double.MIN_VALUE;
		double minValue = Double.MAX_VALUE;
		boolean minRightExist = false;
		boolean maxRightExist = false;
		double tempCount = 0;
		double tempSum = 0;
		double rangeMin = tempAvgNoFilter - tempAvgNoFilter * (this.deviationRangeRate / 100);
		double rangeMax = tempAvgNoFilter + tempAvgNoFilter * (this.deviationRangeRate / 100);
		int rightStartIndex = tempValues.size() / 2;
		for (int i = 0; i < tempValues.size(); i++) {
			Double tempValue = tempValues.get(i);
			if (tempValue != null && tempValue >= rangeMin && tempValue <= rangeMax) {				
				if (minValue > tempValue) {
					minValue = tempValue;					
					if (isLeftToRightDirection)
						minRightExist = i > rightStartIndex ? true : false;
					else
						minRightExist = i < rightStartIndex ? true : false;
				}
				
				if (maxValue < tempValue) {
					maxValue = tempValue;
					if (isLeftToRightDirection)
						maxRightExist = i > rightStartIndex ? true : false;
					else
						maxRightExist = i < rightStartIndex ? true : false;
				}
				
				tempCount++;
				tempSum += tempValue;
			}
		}
		
		double tempAvg = tempCount > 0 ? tempSum / tempCount : 0;
		double minDeviation = -1;
		double maxDeviation = -1;
		if (minRightExist) {
			double deviation = Math.abs(tempAvg - minValue);
			if (deviation > this.allowableTempDev)
				minDeviation = deviation;
		}

		if (maxRightExist) {
			double deviation = Math.abs(tempAvg - maxValue);
			if (deviation > this.allowableTempDev)
				maxDeviation = deviation;
		}

		if (minDeviation == -1 && maxDeviation == -1)
			return result;

		// 편차가 큰 쪽에 Low, High 를 구함.
		if (minDeviation > maxDeviation)
			result = "Low";
		else
			result = "High";

		return result;
	}
}
