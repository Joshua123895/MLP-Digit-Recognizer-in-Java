package main;

import mnist.MNISTManager;
import neuralNetwork.NeuralNetwork;

public class TrainMenu {
	NeuralNetwork network;
	MNISTManager trainData;
	InputHandler scan;
	UtilityTool uTool = new UtilityTool();
	
	final int progressLength = 50;

	public TrainMenu(NeuralNetwork network, MNISTManager source, InputHandler scan) {
		this.network = network;
		this.trainData = source;
		this.scan = scan;
	}
	
	public void train() {
		int epochs = scan.intInput("How many epochs[1..100]? ", 1, 100);
		uTool.clear();
		int trainSize = trainData.getSize();
		
		final double decayRate = 0.96;
		final double initialLearningRate = 0.01;
		
		for (int e = 1; e <= epochs; e++) {
	        network.learningRate = initialLearningRate * Math.exp(-decayRate * network.epoch);
	        
			System.out.printf("epoch %d/%d: --------------------------------------------------   0%%", e, epochs);
	        long startTime = System.nanoTime();
	        
	        int percent = 0;
	        int offset = uTool.countDigits(e) + uTool.countDigits(epochs); // 3 + 3
	        for (int i = 0; i < trainSize; i++) {
	            int currentPercent = (int) ((i+1) * 100.0 / trainSize);
	            if (currentPercent > percent) {
	            	percent++;
	                int cursorPos = (int) (currentPercent / 100.0 * progressLength);
	                cursorPos -= (percent > 50)? 1 : 0;
	                uTool.moveCursorTo(10 + cursorPos + offset, e);
	                System.out.print("#");
	                uTool.moveCursorTo(11 + progressLength + offset, e);
	                System.out.printf("%3d%%", percent);
	            }
	        	// real training
	        	network.feedForward(trainData.getImages(i));
	        	network.backpropagate(trainData.getLabels(i));
	        }
	        long endTime = System.nanoTime();
	        double time = (endTime - startTime) / 1_000_000_000.0;
	        System.out.printf(" time: %.3fs\n", time);
	        network.epoch++;
		}
		System.out.println("Done training!");
		scan.enter();
	}
}
