package main;

import mnist.MNISTManager;
import neuralNetwork.NeuralNetwork;

public class TestMenu {
	NeuralNetwork network;
	MNISTManager testData;
	InputHandler scan;
	UtilityTool uTool = new UtilityTool();

	public TestMenu(NeuralNetwork network, MNISTManager source, InputHandler scan) {
		this.network = network;
		this.testData = source;
		this.scan = scan;
	}
	
	void drawImage(double []image) {
		int len_shade = 24;
		System.out.println(" +--------------------------------------------------------+");
		for (int i = 0; i < 28; i++) {
			System.out.print(" |");
			for (int j = 0; j < 28; j++) {
				int bit = (int) (image[i*28 + j] * len_shade);
				bit -= bit / len_shade; // wont go >= len, not out of bound
				uTool.change_colour(bit);
				System.out.print("  ");
			}
			uTool.reset_colour();
			System.out.println("|");
		}
		System.out.println(" +--------------------------------------------------------+");
	}
	
	public int checkImage(double []image) {
		double[] result = network.feedForward(image);
    	double max = result[0];
    	int predicted = 0;
    	for (int a = 1; a < 10; a++) {
    		if (max < result[a]) {
    			max = result[a];
    			predicted = a;
    		}
    	}
    	return predicted;
	}
	
	public void test() {
		uTool.clear();
		System.out.print("Test accuracy: Loading...");
		
		int correct = 0;
        for (int i = 0; i < testData.getSize(); i++) {
        	double []image = testData.getImages(i);
        	double []label = testData.getLabels(i);
        	if (label[checkImage(image)] > 0.0) {
        		correct++;
        	}
        }
        uTool.clear();
        System.out.printf("Test accuracy: %.3f%%\n", (double) correct * 100.0/testData.getSize());
        scan.enter();
        int index = 0;
        while (true) {
        	uTool.clear();
        	index = scan.intInput("Test from 0 to 9.999 [-1 to exit]: ", -1, 9_999);
        	if (index == -1) break;

        	double []image = testData.getImages(index);
        	double []label = testData.getLabels(index);
        	
        	drawImage(testData.getImages(index));
        	
        	int expected = 0;
        	double max = label[0];
        	for (int i = 1; i < label.length; i++) {
        		if (max < label[i]) {
        			max = label[i];
        			expected = i;
        		}
        	}
        	
            int predicted = checkImage(image);
            System.out.println("Expected: " + expected);
            System.out.println("Predicted: " + predicted);
            scan.enter();
    	}
	}
}
