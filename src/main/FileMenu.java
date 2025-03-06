package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import neuralNetwork.HiddenLayer;
import neuralNetwork.NeuralNetwork;
import neuralNetwork.Neuron;
import neuralNetwork.OutputLayer;

public class FileMenu {
	NeuralNetwork network;
	InputHandler scan;
	UtilityTool uTool = new UtilityTool();

	public FileMenu(NeuralNetwork network, InputHandler scan) {
		this.network = network;
		this.scan = scan;
	}
	
	public void save() {
		String realfilename = scan.stringInput("Save file as: ", "\\/:*?\"<>| ", 252) + ".dat";
		String filename = "result/training/" + realfilename;
		try {
			DataOutputStream file = new DataOutputStream(new FileOutputStream(filename));
			
			int hiddenLayerSize = network.numHiddenLayers.size();
			
			file.writeInt(hiddenLayerSize);
			file.writeInt(network.epoch);
			
			for (Integer i : network.numHiddenLayers) {
				file.writeInt(i);
			}
			
			for (String i : network.hiddenActivations) {
				char a = i.toLowerCase().charAt(0);
				file.writeChar(a);
			}
			
			for (int indexLayer = 1; indexLayer <= hiddenLayerSize; indexLayer++) {
				HiddenLayer hiddenLayer = (HiddenLayer) network.getLayer(indexLayer);
				Neuron[] neurons = hiddenLayer.getNeurons();
				for (int indexNeuron = 0; indexNeuron < neurons.length; indexNeuron++) {
					Neuron neuron = neurons[indexNeuron];
					double[] weights = neuron.getWeights();
					for (int indexWeight = 0; indexWeight < weights.length; indexWeight++) {
						file.writeDouble(weights[indexWeight]);
					}
					file.writeDouble(neuron.getBias());
				}
			}
			
			OutputLayer outputLayer = (OutputLayer) network.getLayer(hiddenLayerSize + 1);
			Neuron[] neurons = outputLayer.getNeurons();
			for (int indexNeuron = 0; indexNeuron < neurons.length; indexNeuron++) {
				Neuron neuron = neurons[indexNeuron];
				double[] weights = neuron.getWeights();
				for (int indexWeight = 0; indexWeight < weights.length; indexWeight++) {
					file.writeDouble(weights[indexWeight]);
				}
				file.writeDouble(neuron.getBias());
			}
			
			file.close();
			System.out.println("Succesfully loaded");
		} catch (Exception e) {
			System.out.println("Can't save the neural network");
		}
		scan.enter();
	}
	
	public void load() {
		File folder = new File("result/training");
		File[] files = folder.listFiles();
		String filename;

		int hiddenLayerSize;
		int epoch;
		int[] numHiddenLayers;
		String[] hiddenActivations = null;
		
		DataInputStream fileStream = null;
		
		while (true) {
			uTool.clear();
			
			System.out.println("Available File:");
			if (files == null) {
				System.out.println(" Empty");
				scan.enter();
				return;
			}
			
			for (File currentFile : files) {
				if (currentFile.getName().endsWith(".dat")) {
					System.out.println(" - " + currentFile.getName());
				}
			}
			
			System.out.println("____________________________________________________________________________________________________");
			filename = scan.stringInput("Load file as: ", "\\/:*?\"<>| ", 252);

			File file = null;
			for (File currentFile : files) {
				if (currentFile.getName().equals(filename)) {
					file = currentFile;
					break;
				}
			}
			
			if (file == null) {
				System.out.println("Can't find the file specified");
				scan.enter();
				continue;
			}
			
			try {
				fileStream = new DataInputStream(new FileInputStream("result/training/" + filename));
				hiddenLayerSize = fileStream.readInt();
				epoch = fileStream.readInt();
				numHiddenLayers = new int[hiddenLayerSize];
				for (int i = 0; i < hiddenLayerSize; i++) {
					numHiddenLayers[i] = fileStream.readInt();
				}
				hiddenActivations = new String[hiddenLayerSize];
				for (int i = 0; i < hiddenLayerSize; i++) {
					String result = "";
					switch (fileStream.readChar()) {
					case 's':
						result = "sigmoid";
						break;
					case 'r':
						result = "relu";
						break;
					default:
						result = "sigmoid";
					}
					hiddenActivations[i] = result;
				}
				fileStream.close();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			
			System.out.println();
			System.out.printf("Layers: %d\n", hiddenLayerSize+2);
			System.out.printf("Epoch: %d\n", epoch);
			System.out.print("Neurons: 784");
			for (int i : numHiddenLayers) {
				System.out.print(", " + i);
			}
			System.out.println(", 10");
			System.out.print("Function Activations: ");
			for (String str : hiddenActivations) {
				System.out.print(str + ", ");
			}
			System.out.println("sigmoid");
			System.out.println();
			// are you sure?
			String confirm = scan.stringInput("Load this file[yes/no]? ");
			if (confirm.toLowerCase().equals("yes")) break;
		}
		
		try {
			fileStream = new DataInputStream(new FileInputStream("result/training/" + filename));
			hiddenLayerSize = fileStream.readInt();
			epoch = fileStream.readInt();
			numHiddenLayers = new int[hiddenLayerSize];
			
			for (int i = 0; i < hiddenLayerSize; i++) {
				numHiddenLayers[i] = fileStream.readInt();
			}
			
			hiddenActivations = new String[hiddenLayerSize];
			for (int i = 0; i < hiddenLayerSize; i++) {
				String result = "";
				switch (fileStream.readChar()) {
				case 's':
					result = "sigmoid";
					break;
				case 'r':
					result = "relu";
					break;
				default:
					result = "sigmoid";
				}
				hiddenActivations[i] = result;
			}
			
			network.epoch = epoch;
			network.numHiddenLayers = new ArrayList<>();
	        for (int i : numHiddenLayers) {
	        	network.numHiddenLayers.add(i);
	        }
			network.hiddenActivations = new ArrayList<>(Arrays.asList(hiddenActivations));
			network.reconstruct();
			
			for (int indexLayer = 1; indexLayer <= hiddenLayerSize; indexLayer++) {
				HiddenLayer hiddenLayer = (HiddenLayer) network.getLayer(indexLayer);
				Neuron[] neurons = hiddenLayer.getNeurons();
				
				for (int indexNeuron = 0; indexNeuron < neurons.length; indexNeuron++) {
					Neuron neuron = neurons[indexNeuron];
					double[] weights = neuron.getWeights();
					for (int indexWeight = 0; indexWeight < weights.length; indexWeight++) {
						weights[indexWeight] = fileStream.readDouble();
					}
					neuron.setWeights(weights);
					neuron.setBias(fileStream.readDouble());
				}
				hiddenLayer.setNeurons(neurons);
				network.setLayer(indexLayer, hiddenLayer);
			}
			
			OutputLayer outputLayer = (OutputLayer) network.getLayer(hiddenLayerSize + 1);
			Neuron[] neurons = outputLayer.getNeurons();
			for (int indexNeuron = 0; indexNeuron < neurons.length; indexNeuron++) {
				Neuron neuron = neurons[indexNeuron];
				double[] weights = neuron.getWeights();
				for (int indexWeight = 0; indexWeight < weights.length; indexWeight++) {
					weights[indexWeight] = fileStream.readDouble();
				}
				neuron.setWeights(weights);
				neuron.setBias(fileStream.readDouble());
			}
			outputLayer.setNeurons(neurons);
			network.setLayer(hiddenLayerSize + 1, outputLayer);
			
			fileStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		System.out.println("Succesfully loaded!");
		scan.enter();
	}
}
