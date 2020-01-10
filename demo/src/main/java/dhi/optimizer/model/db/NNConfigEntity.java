package dhi.optimizer.model.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * TODO : 다단 적용. 삭제예정
 * DB Table Model Class using JPA.(TB_NN_CONF)
 */
@Entity
@Table(name="TB_NN_CONF")
public class NNConfigEntity {

	public static final int defaultID = 1;

	@Id
	@Column(name="CONF_ID")
	private int confId = defaultID;
	
	@Column(name="TIMESTAMP")
	private Date timestamp;	

	@Column(name="LEARNING_RATE")
	private Double learningRate;
		
	@Column(name="MOMENTUM")
	private Double momentum;
	
	@Column(name="SIGMOID_ALPHA_VAL")
	private Double sigmoidAlphaVal;
	
	@Column(name="NEURON_CNT_LAYER_1")
	private int neuronCntLayer1;
	
	@Column(name="NEURON_CNT_LAYER_2")
	private int neuronCntLayer2;
	
	@Column(name="TRAINING_ALGORITHM")
	private String trainingAlgorithm;
	
	@Column(name="ITERATIONS")
	private int iterations;
	
	@Column(name="TRAINING_MSE")
	private double trainingMSE;
	
	@Column(name="MU")
	private double mu;
	
	@Column(name="VALIDATION_MSE")
	private double validationMSE;
		
	@Column(name="TRAINING_TIME")
	private int trainingTime;
	
	@Column(name="VALIDATION_CHECK")
	private int validationCheck;
	
	@Column(name="VALID_DATA_TIME")
	private int validDataTime;
	
	@Column(name="VALID_DATA_MIN_CNT")
	private int validDataMinCnt;
	
	@Column(name="TRAIN_DATA_TIME")
	private int trainDataTime;
	
	@Column(name="TRAIN_DATA_MIN_CNT")
	private int trainDataMinCnt;
	
	public int getConfId() {
		return confId;
	}

	public void setConfId(int confId) {
		this.confId = confId;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(Double learningRate) {
		this.learningRate = learningRate;
	}

	public Double getMomentum() {
		return momentum;
	}

	public void setMomentum(Double momentum) {
		this.momentum = momentum;
	}

	public Double getSigmoidAlphaVal() {
		return sigmoidAlphaVal;
	}

	public void setSigmoidAlphaVal(Double sigmoidAlphaVal) {
		this.sigmoidAlphaVal = sigmoidAlphaVal;
	}

	public int getNeuronCntLayer1() {
		return neuronCntLayer1;
	}

	public void setNeuronCntLayer1(int neuronCntLayer1) {
		this.neuronCntLayer1 = neuronCntLayer1;
	}

	public int getNeuronCntLayer2() {
		return neuronCntLayer2;
	}

	public void setNeuronCntLayer2(int neuronCntLayer2) {
		this.neuronCntLayer2 = neuronCntLayer2;
	}

	public String getTrainingAlgorithm() {
		return trainingAlgorithm;
	}

	public void setTrainingAlgorithm(String trainingAlgorithm) {
		this.trainingAlgorithm = trainingAlgorithm;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public double getTrainingMSE() {
		return trainingMSE;
	}

	public void setTrainingMSE(double trainingMSE) {
		this.trainingMSE = trainingMSE;
	}

	public double getMu() {
		return mu;
	}

	public void setMu(double mu) {
		this.mu = mu;
	}

	public double getValidationMSE() {
		return validationMSE;
	}

	public void setValidationMSE(double validationMSE) {
		this.validationMSE = validationMSE;
	}

	public int getTrainingTime() {
		return trainingTime;
	}

	public void setTrainingTime(int trainingTime) {
		this.trainingTime = trainingTime;
	}

	public int getValidationCheck() {
		return validationCheck;
	}

	public void setValidationCheck(int validationCheck) {
		this.validationCheck = validationCheck;
	}

	public int getValidDataTime() {
		return validDataTime;
	}

	public void setValidDataTime(int validDataTime) {
		this.validDataTime = validDataTime;
	}

	public int getValidDataMinCnt() {
		return validDataMinCnt;
	}

	public void setValidDataMinCnt(int validDataMinCnt) {
		this.validDataMinCnt = validDataMinCnt;
	}

	public int getTrainDataTime() {
		return trainDataTime;
	}

	public void setTrainDataTime(int trainDataTime) {
		this.trainDataTime = trainDataTime;
	}

	public int getTrainDataMinCnt() {
		return trainDataMinCnt;
	}

	public void setTrainDataMinCnt(int trainDataMinCnt) {
		this.trainDataMinCnt = trainDataMinCnt;
	}
}
