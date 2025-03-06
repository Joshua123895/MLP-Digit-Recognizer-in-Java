package neuralNetwork;

import java.util.ArrayList;
import java.util.Arrays;

import activationFunction.ActivationFunction;
import activationFunction.ReLu;
import activationFunction.Sigmoid;

public class NeuralNetwork {
    private ArrayList<Layer> layers;
    public ArrayList<Integer> numHiddenLayers = new ArrayList<>(Arrays.asList(128));
    public ArrayList<String> hiddenActivations = new ArrayList<>(Arrays.asList("sigmoid"));
    public double learningRate = 0.01;
    public int epoch = 0;
    
    public NeuralNetwork() {
    	reconstruct();
    }
    
    public void reconstruct() {
    	if (numHiddenLayers.size() != hiddenActivations.size()) {
    		System.out.println("numHiddenLayers.length != hiddenActivations.length");
    		System.out.println("numHiddenLayers: " + numHiddenLayers.size());
    		for (int i : numHiddenLayers) {
    			System.out.println(i);
    		}
    		System.out.println("hiddenActivations: " + hiddenActivations.size());
    		for (String i : hiddenActivations) {
    			System.out.println(i);
    		}
    		System.exit(1);
    	}
        layers = new ArrayList<>();
		layers.add(new InputLayer(784));
		int prev = 784;
    	for (int i = 0; i < numHiddenLayers.size(); i++) {
    		int num = numHiddenLayers.get(i);
    		ActivationFunction func = chooseFunction(hiddenActivations.get(i));
    		layers.add(new HiddenLayer(prev, num, func));
    		prev = num;
    	}
		layers.add(new OutputLayer(prev, 10, new Sigmoid()));
    }
    
    private ActivationFunction chooseFunction(String text) {
    	switch(text.toLowerCase().trim()) {
	    case "sigmoid":
			return new Sigmoid();
		case "relu":
			return new ReLu();
		default:
			return new Sigmoid();
		}
    }
    
    public double[] feedForward(double[] input) {
        double[] currentInput = input;

        for (Layer layer : layers) {
            currentInput = layer.forward(currentInput);
        }
        
        return currentInput;
    }
    
    public void backpropagate(double[] result) {
    	// assume MLS has minimum 1 hidden layer
    	int layerSize = layers.size();
    	
        OutputLayer outputLayer = (OutputLayer) layers.get(layerSize - 1);

        // hidden-output
        double[] nextError = outputLayer.backward(layers.get(layerSize - 2).output, result, learningRate);
        
        // hidden-hidden-output
        HiddenLayer hiddenLayer = (HiddenLayer) layers.get(layerSize - 2);
        nextError = hiddenLayer.backward(layers.get(layerSize - 3).output, nextError, outputLayer.neurons, learningRate);
        
        // hidden-hidden
        for (int i = layers.size() - 3; i > 0; i--) {
            hiddenLayer = (HiddenLayer) layers.get(i);
            HiddenLayer nextLayer = (HiddenLayer) layers.get(i + 1);
            
            nextError = hiddenLayer.backward(layers.get(i - 1).output, nextError, nextLayer.neurons, learningRate);
        }
    }
    
    public Layer getLayer(int index) {
    	return layers.get(index);
    }
    
    public void setLayer(int index, Layer layer) {
    	layers.set(index, layer);
    }
}
