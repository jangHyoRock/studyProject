package dhi.optimizer.algorithm.pso;

import static jcuda.driver.JCudaDriver.cuCtxCreate;
import static jcuda.driver.JCudaDriver.cuCtxDestroy;
import static jcuda.driver.JCudaDriver.cuCtxSynchronize;
import static jcuda.driver.JCudaDriver.cuDeviceGet;
import static jcuda.driver.JCudaDriver.cuInit;
import static jcuda.driver.JCudaDriver.cuLaunchKernel;
import static jcuda.driver.JCudaDriver.cuMemAlloc;
import static jcuda.driver.JCudaDriver.cuMemFree;
import static jcuda.driver.JCudaDriver.cuMemcpyDtoH;
import static jcuda.driver.JCudaDriver.cuMemcpyHtoD;
import static jcuda.driver.JCudaDriver.cuModuleGetFunction;
import static jcuda.driver.JCudaDriver.cuModuleGetGlobal;
import static jcuda.driver.JCudaDriver.cuModuleLoadData;
import static jcuda.driver.JCudaDriver.cuModuleUnload;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ClassPathResource;

import dhi.common.util.Utilities;
import dhi.optimizer.algorithm.common.NormalizeInfo;
import dhi.optimizer.algorithm.neuralNet.ActivationNetwork;
import dhi.optimizer.algorithm.neuralNet.ActivationNeuron;
import dhi.optimizer.algorithm.neuralNet.BipolarSigmoidFunction;
import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.driver.CUcontext;
import jcuda.driver.CUdevice;
import jcuda.driver.CUdeviceptr;
import jcuda.driver.CUfunction;
import jcuda.driver.CUmodule;
import jcuda.driver.JCudaDriver;

public class PsoAdvisorGPU extends PsoAdvisor implements IPsoAdvisor {
	
	private final static String GPU_KERNEL_CU_FINENAME = "target/classes/kernels/JCudaPSOKernel.cu";	
	private final static String GPU_KERNEL_PTX_FINENAME = "kernels/JCudaPSOKernel.ptx";
	private final static String GPU_KERNEL_FUNCTION_PSO_INITIALIZE = "psoInitialize";
	private final static String GPU_KERNEL_FUNCTION_PSO_UPDATA_POSITION = "psoUpdatePosition";
	private final static String GPU_KERNEL_FUNCTION_PSO_GLOBAL_BEST_OF_GENERATION_VALUE = "psoGlobalBestOfGenerationValue";
	private final static String GPU_KERNEL_FUNCTION_PSO_GLOBAL_BEST_OF_BLOCK_BEST= "psoGlobalBestOfBlockBest";
	private final static String GPU_KERNEL_FUNCTION_PSO_GLOBAL_BEST_MV = "psoGlobalBestMV";
	private final static String GPU_KERNEL_FUNCTION_PSO_VELOCITY_VECTOR = "psoUpdateVelocityVector";
	private final static String GPU_KERNEL_CONSTANT_PSO_CALCULATION_FUNCTION_PARAM = "const_CalcParam";
	
	private final static int MV_INFO_COLUMN_SIZE = 3; // MV Info Column 개수

	// JCuda Context.
	private CUcontext context;
	private CUmodule module;
	private CUfunction function;

	// JCuda Device Data Pointer.
	private CUdeviceptr deviceGenerationValue;
	private CUdeviceptr deviceParticleBestValue;
	private CUdeviceptr deviceMVValue;
	private CUdeviceptr deviceMVBest;
	private CUdeviceptr deviceMVVelocity;
	private CUdeviceptr deviceMVInfo;
	private CUdeviceptr deviceMVInputTagIndexs;
	private CUdeviceptr deviceInputData;
	private CUdeviceptr deviceModelInputNormalizeInfo;
	private CUdeviceptr deviceModelInputNormalizeArrayInfo;
    private CUdeviceptr deviceModelTargetNormalizeInfo;
    private CUdeviceptr deviceModelTargetNormalizeArrayInfo;
    private CUdeviceptr deviceModelConf;
    private CUdeviceptr deviceModelValue;
    private CUdeviceptr deviceMVSpreadData;
    private CUdeviceptr deviceConstantCalculationFunctionParam;
    
	// PSO configuration Info.
	private int mvSize = 0;
	private int swarmSize = 0;	
	private int iteration = 0;
	private int currentIndex = 0;
 	
	public PsoAdvisorGPU(List<PsoMV> mvList, double[] inputData, ActivationNetwork networkModel, PsoCalculationFunction calculationFunction, int particleSize, double inertia, double correctionFactorCognitive, double correctionFactorSocial, int iteration) {
		super(mvList, inputData, networkModel, calculationFunction, particleSize, inertia, correctionFactorCognitive, correctionFactorSocial);		
		this.iteration = iteration;
		this.currentIndex = 0;
	}
	
	public int getGlobalBestIndex() {
		return this.globalBestIndex;
	}

	public double[] getGlobalBestMV() {
		return this.globalBestMV;
	}

	@Override
	public void initialize() {
		this.mvSize = this.mvList.size();
		this.swarmSize = (int)Math.pow(this.particleSize, this.mvSize);
		this.swarmSize++; // Increment to add '0' to the last line.
		
		try {
			
			this.cudaInit();
			this.cudaDeviceDataPointerLoad();
			
			// Obtain a function pointer to the "psoInitialize" function.	
			cuModuleGetFunction(this.function, this.module, GPU_KERNEL_FUNCTION_PSO_INITIALIZE);
			
			Pointer kernelParameters = Pointer.to(
					Pointer.to(this.deviceParticleBestValue), 
					Pointer.to(this.deviceMVValue),
					Pointer.to(this.deviceMVBest), 
					Pointer.to(this.deviceMVVelocity),
					Pointer.to(this.deviceMVSpreadData),
					Pointer.to(new int[] { this.mvSize }), 
					Pointer.to(new int[] { this.particleSize }),
					Pointer.to(new int[] { this.swarmSize }));

			// Call the kernel function.
			int singleThreadCount = 256;
			int threadCount = this.swarmSize > singleThreadCount ? singleThreadCount : this.swarmSize;
			int blockSizeX = (this.swarmSize / threadCount) + 1;
			cuLaunchKernel(this.function, blockSizeX, 1, 1, // Grid dimension
					threadCount, 1, 1, // Block dimension
					0, null, // Shared memory size and stream
					kernelParameters, null // Kernel- and extra parameters
			);
			cuCtxSynchronize();
		} catch (Exception e) {
			this.cudaShutdown();
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void iteration() {
		
		updatePosition();
		updateGlobalBest();
		updateVelocityVector();
		
		if (this.iteration <= ++currentIndex)
			this.cudaShutdown();
	}
	
	/*
	private void updatePosition() {    	
		try {			
			 // Obtain a function pointer to the "psoUpdatePosition" function.
	        cuModuleGetFunction(function, module, GPU_KERNEL_FUNCTION_PSO_UPDATA_POSITION);	        	       
	        
			// Set up the kernel parameters: A pointer to an array
			// of pointers which point to the actual values.
			Pointer kernelParameters = Pointer.to(
					Pointer.to(this.deviceSwarmBestValue),
					Pointer.to(this.deviceMVValue),
					Pointer.to(this.deviceMVBest),
					Pointer.to(this.deviceMVVelocity),
					Pointer.to(this.deviceMVInfo),
					Pointer.to(this.deviceMVInputTagIndexs),
					Pointer.to(this.deviceInputData),					
					Pointer.to(this.deviceModelInputNormalizeInfo),
					Pointer.to(this.deviceModelTargetNormalizeInfo),					
				    Pointer.to(this.deviceModelInputNormalizeArrayInfo),
					Pointer.to(this.deviceModelTargetNormalizeArrayInfo), 
					Pointer.to(this.deviceModelConf),
					Pointer.to(this.deviceModelValue),
					Pointer.to(new int[] { this.mvSize }),
					Pointer.to(new int[] { this.swarmSize }));
		
			// Call the kernel function.
			int singleThreadCount = 64;
			int threadCount = this.swarmSize > singleThreadCount ? singleThreadCount : this.swarmSize;
			int blockSizeX = (this.swarmSize / threadCount) + 1;
			cuLaunchKernel(this.function,				
					blockSizeX, 1, 1, // Grid dimension
					threadCount, 1, 1, // Block dimension
					0, null, // Shared memory size and stream
					kernelParameters, null // Kernel- and extra parameters
			);			
			cuCtxSynchronize();			
		} catch (Exception e) {
			this.cudaShutdown();
			throw new RuntimeException(e);
		}
    }*/
	
	private void updatePosition() {    	
		try {			
			
			// Call the kernel function.
			int singleThreadCount = 256;
			int threadSize = swarmSize > singleThreadCount ? singleThreadCount : swarmSize;

			int totalBlockSize = swarmSize % threadSize == 0 ? swarmSize / threadSize : swarmSize / threadSize + 1;
			int blockSize = totalBlockSize > 1000 ? 1000 : totalBlockSize;

			int endOfBlockSize = totalBlockSize % blockSize;
			int numberOfblockList = endOfBlockSize == 0 ? totalBlockSize / blockSize : totalBlockSize / blockSize + 1;
			
			int currentThreadIndex = 0;
			for (int i = 0; i < numberOfblockList; i++) {
				currentThreadIndex = i * blockSize * threadSize;
				if (i == numberOfblockList - 1) {
					if (endOfBlockSize > 0)
						blockSize = endOfBlockSize;
				}
				
				// Obtain a function pointer to the "psoUpdatePosition" function.
		        cuModuleGetFunction(function, module, GPU_KERNEL_FUNCTION_PSO_UPDATA_POSITION);
		        
				// Set up the kernel parameters: A pointer to an array
				// of pointers which point to the actual values.
		        Pointer kernelParameters = Pointer.to(
		        		Pointer.to(this.deviceGenerationValue),
						Pointer.to(this.deviceParticleBestValue),
						Pointer.to(this.deviceMVValue),
						Pointer.to(this.deviceMVBest),
						Pointer.to(this.deviceMVVelocity),
						Pointer.to(this.deviceMVInfo),
						Pointer.to(this.deviceMVInputTagIndexs),
						Pointer.to(this.deviceInputData),					
						Pointer.to(this.deviceModelInputNormalizeInfo),
						Pointer.to(this.deviceModelInputNormalizeArrayInfo),
						Pointer.to(this.deviceModelTargetNormalizeInfo),
						Pointer.to(this.deviceModelTargetNormalizeArrayInfo), 
						Pointer.to(this.deviceModelConf),
						Pointer.to(this.deviceModelValue),
						Pointer.to(new int[] { this.mvSize }),
						Pointer.to(new int[] { this.swarmSize }),
						Pointer.to(new int[] { currentThreadIndex }));
		        
				cuLaunchKernel(function, 
						blockSize, 1, 1, // Grid dimension,
						threadSize, 1, 1, // Block dimension
						0, null, // Shared memory size and stream
						kernelParameters, null // Kernel- and extra parameters
				);
				
				cuCtxSynchronize();				
			}
		} catch (Exception e) {
			this.cudaShutdown();
			throw new RuntimeException(e);
		}
    }
	
	private void updateGlobalBest() {		
		int threadCount = 1024;
		int blockBestValueSize = 0;
		int blockBestValueColumnSize = 2;

		double[] hostBlockBestValue;
		CUdeviceptr deviceBlockBestValue = null;
		CUdeviceptr deviceGlobalBestMV = null;
		try {

			int blockSize = 0;
			int threadSize = swarmSize > threadCount ? threadCount : swarmSize;
			int endOfBlockDimX = swarmSize % threadSize;
			if (endOfBlockDimX == 0) {
				blockSize = swarmSize / threadSize;
				endOfBlockDimX = threadSize;
			} else {
				blockSize = swarmSize / threadSize + 1;
			}

			blockBestValueSize = blockSize * blockBestValueColumnSize;
			hostBlockBestValue = new double[blockBestValueSize];

			deviceBlockBestValue = new CUdeviceptr();
			cuMemAlloc(deviceBlockBestValue, blockBestValueSize * Sizeof.DOUBLE);

			boolean isfirst = true;
			do {
				
				int sharedBlockBestValueSize = threadSize * blockBestValueColumnSize * Sizeof.DOUBLE;				
				Pointer kernelParameters = null;
				if (isfirst) {
					
					// Obtain a function pointer to the "psoGlobalBest" function.
					cuModuleGetFunction(this.function, this.module, GPU_KERNEL_FUNCTION_PSO_GLOBAL_BEST_OF_GENERATION_VALUE);
					
					kernelParameters = Pointer.to(Pointer.to(deviceBlockBestValue),
							Pointer.to(this.deviceGenerationValue), 
							Pointer.to(new int[] { this.swarmSize }), 
							Pointer.to(new int[] { endOfBlockDimX }));
					isfirst = false;
				} else {
					
					// Obtain a function pointer to the "psoGlobalBest" function.
					cuModuleGetFunction(this.function, this.module, GPU_KERNEL_FUNCTION_PSO_GLOBAL_BEST_OF_BLOCK_BEST);
					
					kernelParameters = Pointer.to(Pointer.to(deviceBlockBestValue),
							Pointer.to(new int[] { this.swarmSize }), 
							Pointer.to(new int[] { endOfBlockDimX }));
				}

				cuLaunchKernel(this.function, blockSize, 1, 1, // Grid dimension
						threadSize, 1, 1, // Block dimension
						sharedBlockBestValueSize, null, // Shared memory size and stream
						kernelParameters, null // Kernel- and extra parameters
				);
				cuCtxSynchronize();

				if (blockSize <= 1)
					break;

				// BlockBestValue 결과를 Block 단위로 나누어 다시 최적을 값을 찾기 위함.
				threadSize = blockSize > threadCount ? threadCount : blockSize;
				endOfBlockDimX = blockSize % threadSize;
				if (endOfBlockDimX == 0) {
					blockSize = blockSize / threadSize;
					endOfBlockDimX = threadSize;
				} else {
					blockSize = blockSize / threadSize + 1;
				}
			} while (true);

			cuMemcpyDtoH(Pointer.to(hostBlockBestValue), deviceBlockBestValue, blockBestValueSize * Sizeof.DOUBLE);

			/*double bestValue = Double.MAX_VALUE;		
			for (int i = 0; i < blockSize; i++) {
				if (bestValue > hostBlockBestValue[i * 2 + 0]) {
					bestValue = hostBlockBestValue[i * 2 + 0];
					this.globalBestIndex[0] = (int) hostBlockBestValue[i * 2 + 1];
					this.globalBestValue = bestValue;
				}
			}
			*/
			
			this.globalBestValue = hostBlockBestValue[0];
			this.globalBestIndex = (int) hostBlockBestValue[1];
			
			System.out.printf("bestValue : %f, bestIndex : %d \n", this.globalBestValue, this.globalBestIndex);
			
			double[] hostGlobalBestMV = new double[mvSize];
			deviceGlobalBestMV = new CUdeviceptr();
			cuMemAlloc(deviceGlobalBestMV, this.mvSize * Sizeof.DOUBLE);
			
			// Obtain a function pointer to the "psoGlobalBestMV" function.
	        cuModuleGetFunction(this.function, this.module, GPU_KERNEL_FUNCTION_PSO_GLOBAL_BEST_MV);
	        
	        // Set up the kernel parameters: A pointer to an array
			// of pointers which point to the actual values.
			Pointer kernelParameters = Pointer.to(
					Pointer.to(deviceGlobalBestMV),
					Pointer.to(this.deviceMVValue),
					Pointer.to(new int[] { this.mvSize }),			
					Pointer.to(new int[] { this.globalBestIndex }));
			
			// Block = 1, Thread = 1 만 하는 이유는 전체 MV Best 정보에서 Global Best Index 값을 한번만 조회하면 되므로.
			cuLaunchKernel(this.function,				
					1, 1, 1, // Grid dimension
					1, 1, 1, // Block dimension
					0, null, // Shared memory size and stream
					kernelParameters, null // Kernel- and extra parameters
			);

			cuMemcpyDtoH(Pointer.to(hostGlobalBestMV), deviceGlobalBestMV, mvSize * Sizeof.DOUBLE);
			this.globalBestMV = hostGlobalBestMV;

		} catch (Exception e) {
			this.cudaShutdown();
			throw new RuntimeException(e);
		} finally {
			cuMemFree(deviceBlockBestValue);
			cuMemFree(deviceGlobalBestMV);
		}
	}

	private void updateVelocityVector() {
		try {
			
			cuModuleGetFunction(this.function, this.module, GPU_KERNEL_FUNCTION_PSO_VELOCITY_VECTOR);
		
			Pointer kernelParameters = Pointer.to(Pointer.to(this.deviceMVValue),
					Pointer.to(this.deviceMVBest),
					Pointer.to(this.deviceMVVelocity), 
					Pointer.to(new int[] { this.mvSize }),
					Pointer.to(new int[] { this.swarmSize }), 
					Pointer.to(new double[] { this.inertia }),
					Pointer.to(new double[] { this.correctionFactorCognitive }), 
					Pointer.to(new double[] { this.correctionFactorSocial }),
					Pointer.to(new int[] { this.globalBestIndex }));

			// Call the kernel function.
			int singleThreadCount = 256;
			int threadCount = this.swarmSize > singleThreadCount ? singleThreadCount : this.swarmSize;
			int blockSizeX = (this.swarmSize / threadCount) + 1;
			cuLaunchKernel(this.function, blockSizeX, 1, 1, // Grid dimension
					threadCount, 1, 1, // Block dimension
					0, null, // Shared memory size and stream
					kernelParameters, null // Kernel- and extra parameters
			);			
			cuCtxSynchronize();
		} catch (Exception e) {
			this.cudaShutdown();
			throw new RuntimeException(e);
		}
	}		
	
	/**
     * Initialize the context, module, function and other elements used 
     * in this sample
     */
    private void cudaInit() {

		// Enable exceptions and omit all subsequent error checks
		JCudaDriver.setExceptionsEnabled(true);

		// Initialize the driver API and create a context for the first device
		cuInit(0);
		CUdevice device = new CUdevice();
		cuDeviceGet(device, 0);
		this.context = new CUcontext();
		cuCtxCreate(context, 0, device);

		// PTX 파일 만들기 & Load
		// NVCC -ptx JCudaPSOKernel.cu -o JCudaPSOKernel.ptx
		ClassPathResource classPathResource = new ClassPathResource(GPU_KERNEL_PTX_FINENAME);
		try {
			InputStream inputStream = classPathResource.getInputStream();			
			byte ptxData[] = Utilities.convertInputStreamToByteArray(inputStream);
			this.module = new CUmodule();

			// cuModuleLoad(this.module, GPU_KERNEL_PTX_FINENAME);
			cuModuleLoadData(this.module, ptxData);

			this.function = new CUfunction();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Release all resources allocated by this class
	 */
	private void cudaShutdown() {
		
		if(deviceGenerationValue != null)
			cuMemFree(deviceGenerationValue);
		
		if(deviceParticleBestValue != null)
			cuMemFree(deviceParticleBestValue);
		
		if(deviceMVValue != null)
			cuMemFree(deviceMVValue);
		
		if(deviceMVBest != null)
			cuMemFree(deviceMVBest);
		
		if(deviceMVVelocity != null)
			cuMemFree(deviceMVVelocity);
		
		if(deviceMVInfo != null)
			cuMemFree(deviceMVInfo);
		
		if(deviceMVInputTagIndexs != null)
			cuMemFree(deviceMVInputTagIndexs);
		
		if(deviceInputData != null)
			cuMemFree(deviceInputData);
		
		if(deviceModelInputNormalizeInfo != null)
			cuMemFree(deviceModelInputNormalizeInfo);
		
		if(deviceModelInputNormalizeArrayInfo != null)
			cuMemFree(deviceModelInputNormalizeArrayInfo);
		
		if(deviceModelTargetNormalizeInfo != null)
			cuMemFree(deviceModelTargetNormalizeInfo);
		
		if(deviceModelTargetNormalizeArrayInfo != null)
			cuMemFree(deviceModelTargetNormalizeArrayInfo);
		
		if(deviceModelConf != null)
			cuMemFree(deviceModelConf);
		
		if(deviceModelValue != null)
			cuMemFree(deviceModelValue);
		
		if(deviceMVSpreadData != null)
			cuMemFree(deviceMVSpreadData);
		
		if (this.module != null || this.context != null) {			
			cuModuleUnload(this.module);
			cuCtxDestroy(this.context);
		}
	}
	
	private void cudaDeviceDataPointerLoad() {
		/*
		 * # GPU Kernel - MV Info 생성. 
		 * : MV 별 Type, Min, Max 정보를 담음.
		 * : 2차원 배열의 3개 컬럼으로 구성됨.
		 * 
		 * [MVSize][ColumnSize] = [9][3]
		 * [0][0] = 0 (MV Type 0:Burner, 1:OFA, 2:Total Air) 
		 * [0][1] = -5 (MV Min) 
		 * [0][2] = 5 (MV Max)
		 */		
    	double[] mvInfo = new double[this.mvList.size() * MV_INFO_COLUMN_SIZE];
    	
    	/*
		 * # GPU Kernel - MV Spread Data 생성. : MV 별 Min, Max 값 으로 ParticleSize로 균등하게 나눈
		 * 값을 담음. : 2차원 배열의 Particle Size의 컬럼으로 구성됨. ParticleSize = 3 이면 3개의 컬럼 구성.
		 * 
		 * [MVSize][ParticleSize] = [9][3] 
		 * [0][0] = -5 (MV Min) 
		 * [0][1] = 0 (MV Step)
		 * [0][2] = 5 (MV Max);
		 */
		double[] mvSpreadData = new double[this.mvList.size() * particleSize];
		
		for (int i = 0; i < this.mvList.size(); i++) {
			PsoMV psoMV = this.mvList.get(i);
			int mvType = -1;
			switch (psoMV.getMvType()) {
			case Burner:
				mvType = 0;
				break;
			case OFA:
				mvType = 1;
				break;
			case Air:
				mvType = 2;
				break;
			}

			mvInfo[i * MV_INFO_COLUMN_SIZE + 0] = mvType;
			mvInfo[i * MV_INFO_COLUMN_SIZE + 1] = psoMV.getMin();
			mvInfo[i * MV_INFO_COLUMN_SIZE + 2] = psoMV.getMax();

			// MV 별 Spread Data 생성.
			double spaceSize = (psoMV.getMax() - psoMV.getMin()) / (particleSize - 1);
			for (int j = 0; j < particleSize; j++) {
				mvSpreadData[i * particleSize + j] = psoMV.getMin() + (spaceSize * j);
			}
		}		
		
		// Model - 작업
    	// Normalize 
    	/*
    	modelInputNormalizeInfo	Structure
    	0 [0] = InputSize
    	1 [1] = NormalizeScale
    	2 [2] = NormalizeScaleHalf

    	modelInputNormalizeArrayInfo Structure
    	[inputTag][3]
    	0 [0][0] => ScaleFactors
    	1 [0][1] => min
    	2 [0][2] => max
    	3 [1][0] => ScaleFactors
    	4 [1][1] => min
    	5 [1][2] => max

    	modelTargetNormalizeInfo Structure
    	0 [0] = OutputSize
    	1 [1] = NormalizeScale
    	2 [2] = NormalizeScaleHalf

    	[outputTag][3]
    	0 [0][0] => ScaleFactors
    	1 [0][1] => min
    	2 [0][2] => max
    	3 [1][0] => ScaleFactors
    	4 [1][1] => min
    	5 [1][2] => max
    	*/

    	/*
    	model conf
    	0 [layer][layerSize] = 3;
    	1  0 [neuron][neuronSize] = 30;
    	2  1 [neuron][weightSize] = 210;
    	3  2 [neuron][activationFunctionType] = 0;
    	4  0 [neuron][neuronSize] = 30;
    	5  1 [neuron][weightSize] = 210
    	6  2 [neuron][activationFunctionType] = 0;
    	7  0 [neuron][neuronSize] = 30;
    	8  1 [neuron][weightSize] = 210
    	9  2 [neuron][activationFunctionType] = 0;

    	model value
    	0  [neuron][threshold]                => 0.2
    	1  [neuron][activationFunctionParam]  => 0.5
    	2 ~ 211 [neuron][weights]            => Array weights
    	*/
		
		// Input Normalize 배열화.
		NormalizeInfo inputNormalizeInfo = networkModel.getInputNormalizeInfo();
		int inputNormalizeSize = inputNormalizeInfo.getScaleFactors().length;
		int normalizeColumnSize = 3;	
    	double[] modelInputNormalizeInfo = new double[normalizeColumnSize];
    	double[] modelInputNormalizeArrayInfo = new double[inputNormalizeSize * normalizeColumnSize];    	    	
    	modelInputNormalizeInfo[0] = inputNormalizeSize;
    	modelInputNormalizeInfo[1] = inputNormalizeInfo.getNormalizeScale();
    	modelInputNormalizeInfo[2] = inputNormalizeInfo.getNormalizeScaleHalf();
    	for (int i = 0; i < networkModel.getInputsCount(); i++) {
    		modelInputNormalizeArrayInfo[i * normalizeColumnSize + 0] = inputNormalizeInfo.getScaleFactors()[i];
    		modelInputNormalizeArrayInfo[i * normalizeColumnSize + 1] = inputNormalizeInfo.getRanges()[i].getMin();
    		modelInputNormalizeArrayInfo[i * normalizeColumnSize + 2] = inputNormalizeInfo.getRanges()[i].getMax();
    	}

    	// Target Normalize 배열화.
    	NormalizeInfo targetNormalizeInfo = networkModel.getTargetNormalizeInfo();
    	int targetNormalizeSize = targetNormalizeInfo.getScaleFactors().length;    	
    	double[] modelTargetNormalizeInfo = new double[normalizeColumnSize];
    	double[] modelTargetNormalizeArrayInfo = new double[targetNormalizeSize * normalizeColumnSize];    	
    	modelTargetNormalizeInfo[0] = targetNormalizeSize;
    	modelTargetNormalizeInfo[1] = targetNormalizeInfo.getNormalizeScale();
    	modelTargetNormalizeInfo[2] = targetNormalizeInfo.getNormalizeScaleHalf();
    	for (int i = 0; i < targetNormalizeSize; i++) {
    		modelTargetNormalizeArrayInfo[i * normalizeColumnSize + 0] = targetNormalizeInfo.getScaleFactors()[i];
    		modelTargetNormalizeArrayInfo[i * normalizeColumnSize + 1] = targetNormalizeInfo.getRanges()[i].getMin();
    		modelTargetNormalizeArrayInfo[i * normalizeColumnSize + 2] = targetNormalizeInfo.getRanges()[i].getMax();
    	}
    	
    	// Model Conf 배열화.    	
    	int modelConfSize = 1 + (networkModel.getLayersCount() * 3);    	    	
    	int[] modelConf = new int[modelConfSize];
    	int confIndex = 0;
    	modelConf[confIndex++] = networkModel.getLayersCount();
    	
    	List<Double> modelValueList = new ArrayList<Double>();
		for (int i = 0; i < networkModel.getLayersCount(); i++) {			
			ActivationNeuron activationNeuron = (ActivationNeuron) networkModel.getLayers()[i].getNeurons()[0];
			modelConf[confIndex++] = networkModel.getLayers()[i].getNeurons().length;
			modelConf[confIndex++] = activationNeuron.getWeights().length;
			modelConf[confIndex++] = 0; // activationNeuron.getActivationFunction() Type
			
			for (int j = 0; j < networkModel.getLayers()[i].getNeurons().length; j++) {
				activationNeuron = (ActivationNeuron) networkModel.getLayers()[i].getNeurons()[j];				
				modelValueList.add(activationNeuron.getThreshold());
				
				BipolarSigmoidFunction sigmoidFunction = (BipolarSigmoidFunction) activationNeuron.getActivationFunction();
				modelValueList.add(sigmoidFunction.getAlpha());
				for (double weight : activationNeuron.getWeights()) {
					modelValueList.add(weight);
				}
			}
		}
		
		// Model Value 배열화.
		double[] modelValue = new double[modelValueList.size()];
		for (int i = 0; i < modelValueList.size(); i++) {
			modelValue[i] = modelValueList.get(i);
		}
		
		/* MV 가 9개 인경우 18번째 부터 Tag 정보가 있음.
		- MV1
		[0] = 20; : Tag Index Start
		[1] = 23; : Tag Index End

		- MV9
		[18] = 60 : Tag Index Start
		[19] = 60 : Tag Index End.
		.
		- Tag Info
		[20] = MV 1 TagIndex
		[21] = MV 1 TagIndex
		[22] = MV 1 TagIndex
		[23] = MV 1 TagIndex
		.
		.
		[60] = MV 9 TagIndex
		*/

		// ##############################  Mv to Tag Index
		// MV to Input Count.
		int mvInputTotalCount = 0;
		for (PsoMV psoMV : this.mvList) {
			mvInputTotalCount += psoMV.getInputTagList().length;
		}
		
		int[] mvInputTagIndexs = new int[this.mvList.size() * 2 + mvInputTotalCount];		
		int index = this.mvList.size() * 2;
		for (int i = 0; i < this.mvList.size(); i++) {			
			PsoMV psoMV = this.mvList.get(i);
			mvInputTagIndexs[i * 2] = index;			
			for (PsoTag psoTag : psoMV.getInputTagList()) {
				mvInputTagIndexs[index++] = psoTag.getIndex();
			}
			mvInputTagIndexs[i * 2 + 1] = index;
		}
		
		int swarmSizeOf = swarmSize * Sizeof.DOUBLE;
		int swarmMVSizeOf = swarmSize * this.mvList.size() * Sizeof.DOUBLE;

		this.deviceGenerationValue = new CUdeviceptr();
		cuMemAlloc(this.deviceGenerationValue, swarmSizeOf);
		
		this.deviceParticleBestValue = new CUdeviceptr();
		cuMemAlloc(this.deviceParticleBestValue, swarmSizeOf);

		this.deviceMVValue = new CUdeviceptr();
		cuMemAlloc(this.deviceMVValue, swarmMVSizeOf);

		this.deviceMVBest = new CUdeviceptr();
		cuMemAlloc(this.deviceMVBest, swarmMVSizeOf);

		this.deviceMVVelocity = new CUdeviceptr();
		cuMemAlloc(this.deviceMVVelocity, swarmMVSizeOf);	    
	    
	    this.deviceMVInfo = new CUdeviceptr();
		cuMemAlloc(this.deviceMVInfo, mvInfo.length * Sizeof.DOUBLE); 
	    cuMemcpyHtoD(this.deviceMVInfo, Pointer.to(mvInfo), mvInfo.length * Sizeof.DOUBLE);

	    this.deviceMVInputTagIndexs = new CUdeviceptr();
		cuMemAlloc(this.deviceMVInputTagIndexs, mvInputTagIndexs.length * Sizeof.INT);
		cuMemcpyHtoD(this.deviceMVInputTagIndexs, Pointer.to(mvInputTagIndexs), mvInputTagIndexs.length * Sizeof.INT);
		
		this.deviceInputData = new CUdeviceptr();
		cuMemAlloc(this.deviceInputData, this.inputData.length * Sizeof.DOUBLE);
		cuMemcpyHtoD(this.deviceInputData, Pointer.to(this.inputData), this.inputData.length * Sizeof.DOUBLE);

		this.deviceModelInputNormalizeInfo = new CUdeviceptr();
		cuMemAlloc(this.deviceModelInputNormalizeInfo, modelInputNormalizeInfo.length * Sizeof.DOUBLE);
		cuMemcpyHtoD(this.deviceModelInputNormalizeInfo, Pointer.to(modelInputNormalizeInfo),modelInputNormalizeInfo.length * Sizeof.DOUBLE);

		this.deviceModelInputNormalizeArrayInfo = new CUdeviceptr();
		cuMemAlloc(this.deviceModelInputNormalizeArrayInfo, modelInputNormalizeArrayInfo.length * Sizeof.DOUBLE);
		cuMemcpyHtoD(this.deviceModelInputNormalizeArrayInfo, Pointer.to(modelInputNormalizeArrayInfo), modelInputNormalizeArrayInfo.length * Sizeof.DOUBLE);

		this.deviceModelTargetNormalizeInfo = new CUdeviceptr();
		cuMemAlloc(this.deviceModelTargetNormalizeInfo, modelTargetNormalizeInfo.length * Sizeof.DOUBLE);
		cuMemcpyHtoD(this.deviceModelTargetNormalizeInfo, Pointer.to(modelTargetNormalizeInfo), modelTargetNormalizeInfo.length * Sizeof.DOUBLE);

		this.deviceModelTargetNormalizeArrayInfo = new CUdeviceptr();
		cuMemAlloc(this.deviceModelTargetNormalizeArrayInfo, modelTargetNormalizeArrayInfo.length * Sizeof.DOUBLE);
		cuMemcpyHtoD(this.deviceModelTargetNormalizeArrayInfo, Pointer.to(modelTargetNormalizeArrayInfo), modelTargetNormalizeArrayInfo.length * Sizeof.DOUBLE);

		this.deviceModelConf = new CUdeviceptr();
		cuMemAlloc(this.deviceModelConf, modelConf.length * Sizeof.INT);
		cuMemcpyHtoD(this.deviceModelConf, Pointer.to(modelConf), modelConf.length * Sizeof.INT);

		this.deviceModelValue = new CUdeviceptr();
		cuMemAlloc(this.deviceModelValue, modelValue.length * Sizeof.DOUBLE);
		cuMemcpyHtoD(this.deviceModelValue, Pointer.to(modelValue), modelValue.length * Sizeof.DOUBLE);
	    
		this.deviceMVSpreadData = new CUdeviceptr();
		cuMemAlloc(this.deviceMVSpreadData, mvSize * particleSize * Sizeof.DOUBLE);
		cuMemcpyHtoD(this.deviceMVSpreadData, Pointer.to(mvSpreadData), mvSize * particleSize * Sizeof.DOUBLE);
		
		this.deviceConstantCalculationFunctionParam = new CUdeviceptr();			

		double[] calculationFunctionParam = { this.calculationFunction.getProfitWeight(), this.calculationFunction.getEmissionWeight(),
				this.calculationFunction.getEquipmentWeight(), this.calculationFunction.getRhSparyK(), this.calculationFunction.getO2AvgK(),
				this.calculationFunction.getCoK(), this.calculationFunction.getNoxK(), this.calculationFunction.getFgtK(), this.calculationFunction.getRhSparyDiffK(),
				this.calculationFunction.getO2DiffK(), this.calculationFunction.getO2AvgBoundary(), this.calculationFunction.getO2AvgPenalyWeight(),
				this.calculationFunction.getO2MinBoundary(), this.calculationFunction.getO2MinPenalyWeight(),
				this.calculationFunction.getLoadPenaltyWeight(), this.calculationFunction.getLoadSetPointPenaltyWeight(),
				this.calculationFunction.getNoxBoundary(), this.calculationFunction.getNoxPenaltyWeight(), this.calculationFunction.getStackCoBoundary(),
				this.calculationFunction.getStackCoPenaltyWeight(), this.calculationFunction.getFgTempBoundary(),
				this.calculationFunction.getFgTempPenaltyWeight(), this.calculationFunction.getOpOutputLoadMW(),
				this.calculationFunction.getOpInputLoadSetPointMW() };
		
		long constantMemorySizeArray[] = { calculationFunctionParam.length };
		cuModuleGetGlobal(this.deviceConstantCalculationFunctionParam, constantMemorySizeArray, this.module, GPU_KERNEL_CONSTANT_PSO_CALCULATION_FUNCTION_PARAM);
		cuMemcpyHtoD(this.deviceConstantCalculationFunctionParam, Pointer.to(calculationFunctionParam), calculationFunctionParam.length * Sizeof.DOUBLE);
	}
}
