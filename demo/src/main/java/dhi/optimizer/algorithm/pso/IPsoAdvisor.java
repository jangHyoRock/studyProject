package dhi.optimizer.algorithm.pso;

public interface IPsoAdvisor {

	void initialize();

	void iteration();
	
	int getGlobalBestIndex();
	
	double getGlobalBestValue();
	
	double[] getGlobalBestOutput();	
	
	double[] getGlobalBestMV();
}
