package dhi.optimizer.algorithm.neuralNet;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dhi.optimizer.algorithm.common.DoubleRange;
import dhi.optimizer.algorithm.common.NormalizeInfo;

public class NetworkXmlHelper {
	
	public NetworkXmlHelper() {};
	
	public ActivationNetwork XMLDecoder(InputStream inputStream) {
		ActivationNetwork network = null;
		try {
			// Get Document Builder
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			// Build Document
			Document document = builder.parse(inputStream);

			// Normalize the XML Structure; It's just too important !!
			document.getDocumentElement().normalize();

			network = this.createNetwork(document);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return network;
	}
	
	public ActivationNetwork XMLDecoder(String filePath) {
		ActivationNetwork network = null;
		try {
			File file = new File(filePath);			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(file);
			document.getDocumentElement().normalize();
			network = this.createNetwork(document);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
		return network;
	}
	
	public String XMLEncoder(ActivationNetwork network) {		
		String xmlString = "";
		try {			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
            Document doc = docBuilder.newDocument();
            doc.setXmlStandalone(true); 

            // Root Element
            Element rootElement = doc.createElement("ActivationNetwork");
            rootElement.setAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
            rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            doc.appendChild(rootElement);
            
            // InputsCount Element
            Element inputsCountElement = this.createXmlToElement(doc, "InputsCount", String.valueOf(network.getInputsCount()));            
            rootElement.appendChild(inputsCountElement);
            
            // LayersCount Element
            Element layersCountElement = this.createXmlToElement(doc, "LayersCount", String.valueOf(network.getLayersCount()));
            rootElement.appendChild(layersCountElement);
            
            // Layers Element
            Element layersElement = doc.createElement("Layers");
            rootElement.appendChild(layersElement);
            
            for(Layer layer : network.getLayers()) {            	
            	ActivationLayer activationLayer = (ActivationLayer)layer;
            	
            	 Element layerElement = doc.createElement("Layer");
            	 layerElement.setAttribute("xsi:type", activationLayer.getClass().getSimpleName());
            	 
            	 Element layerInputsCountElement = this.createXmlToElement(doc, "InputsCount", String.valueOf(layer.getInputsCount()));
            	 layerElement.appendChild(layerInputsCountElement);
            	 
            	 Element layerNeuronsCountElement = this.createXmlToElement(doc, "NeuronsCount", String.valueOf(layer.getNeuronsCount()));
            	 layerElement.appendChild(layerNeuronsCountElement);
                 
                 Element neuronsElement = doc.createElement("Neurons");
                 layerElement.appendChild(neuronsElement);
                 for(Neuron neuron : layer.getNeurons()) {
                	 ActivationNeuron activationNeuron = (ActivationNeuron)neuron;                	 
                	 Element neuronElement = doc.createElement("Neuron");
                	 neuronElement.setAttribute("xsi:type", activationNeuron.getClass().getSimpleName());
                	 neuronsElement.appendChild(neuronElement);
                	 
                	 Element neuronInputsCountElement =  this.createXmlToElement(doc, "InputsCount", String.valueOf(neuron.getInputsCount()));
                	 neuronElement.appendChild(neuronInputsCountElement);
                	 
                	 Element neuronOutputsElement = this.createXmlToElement(doc, "Output", String.valueOf(neuron.getOutput()));
                	 neuronElement.appendChild(neuronOutputsElement);
                	 
                	 Element neuronWeightsElement = this.createDoubleValuesXmlToElement(doc, "Weights", "double", neuron.getWeights());
                	 neuronElement.appendChild(neuronWeightsElement);                
                	 
                	 Element neuronThresholdElement = this.createXmlToElement(doc, "Threshold", String.valueOf(activationNeuron.getThreshold()));
                	 neuronElement.appendChild(neuronThresholdElement);
                	 
                	 Element neuronActionvationFunctionElement = doc.createElement("ActivationFunction");
                	 neuronActionvationFunctionElement.setAttribute("xsi:type", activationNeuron.getActivationFunction().getClass().getSimpleName());
                	 neuronElement.appendChild(neuronActionvationFunctionElement);                	 
                	 if(activationNeuron.getActivationFunction() instanceof BipolarSigmoidFunction){
                		 BipolarSigmoidFunction function = (BipolarSigmoidFunction)activationNeuron.getActivationFunction();                		 
                		 Element alphaElement =  this.createXmlToElement(doc, "Alpha", String.valueOf(function.getAlpha()));
                		 neuronActionvationFunctionElement.appendChild(alphaElement);
                	 }
                 }
                 
            	 layersElement.appendChild(layerElement);
            }     
            
            // Output Element
            Element outputsElement = doc.createElement("Output");            
            rootElement.appendChild(outputsElement);            
            for(double output : network.getOutput()) {
            	 Element outputDoubleElement = doc.createElement("double");
            	 outputDoubleElement.appendChild(doc.createTextNode(String.valueOf(output)));
            	 outputsElement.appendChild(outputDoubleElement);
            }
            
            // InputNormalizeInfo Element            
            Element inputNormalizeInfoElement = this.createNormalizeInfoXmlToElement(doc, "InputNormalizeInfo", network.inputNormalizeInfo);
            rootElement.appendChild(inputNormalizeInfoElement);
            
            // TargetNormalizeInfo Element
            Element targetNormalizeInfoInfoElement = this.createNormalizeInfoXmlToElement(doc, "TargetNormalizeInfo", network.targetNormalizeInfo);
            rootElement.appendChild(targetNormalizeInfoInfoElement);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance(); 
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
 
            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer); 
            transformer.transform(source, result);
            xmlString = writer.getBuffer().toString();
            
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}	
		
		return xmlString;
	}
	
	private Element createNormalizeInfoXmlToElement(Document doc, String elementName, NormalizeInfo normalizeInfo) {
		Element normalizeInfoElement =  doc.createElement(elementName);
		
		// NormalizeScale Element
		Element normalizeScaleElement = this.createXmlToElement(doc, "NormalizeScale", String.valueOf(normalizeInfo.getNormalizeScale()));
		normalizeInfoElement.appendChild(normalizeScaleElement);
		
		// NormalizeScaleHalf Element
		Element normalizeScaleHalfElement = this.createXmlToElement(doc, "NormalizeScaleHalf", String.valueOf(normalizeInfo.getNormalizeScaleHalf()));
		normalizeInfoElement.appendChild(normalizeScaleHalfElement);
		
		// ScaleFactors Element
        Element scaleFactorsElement =  this.createDoubleValuesXmlToElement(doc, "ScaleFactors", "double", normalizeInfo.getScaleFactors());     
        normalizeInfoElement.appendChild(scaleFactorsElement);
        
        // Ranges
        Element rangesElement = doc.createElement("Ranges");
        normalizeInfoElement.appendChild(rangesElement);
        
        for(DoubleRange doubleRange : normalizeInfo.getRanges()) {
        	// DoubleRange
	        Element doubleRangeElement = doc.createElement("DoubleRange");
	        rangesElement.appendChild(doubleRangeElement);
	        
	        Element minElement = this.createXmlToElement(doc, "Min", String.valueOf(doubleRange.getMin()));
	        doubleRangeElement.appendChild(minElement);
	        
	        Element maxElement = this.createXmlToElement(doc, "Max", String.valueOf(doubleRange.getMax()));
	        doubleRangeElement.appendChild(maxElement);
        }
        
        return normalizeInfoElement;
	}
	
	private Element createXmlToElement(Document doc, String elementName, String value) {
		Element element = doc.createElement(elementName);
		element.appendChild(doc.createTextNode(String.valueOf(value)));
		return element;
	}
	
	private Element createDoubleValuesXmlToElement(Document doc, String groupElementName, String elementName,
			double[] values) {
		Element elements = doc.createElement(groupElementName);
		for (double value : values) {
			Element doubleElement = doc.createElement(elementName);
			doubleElement.appendChild(doc.createTextNode(String.valueOf(value)));
			elements.appendChild(doubleElement);
		}

		return elements;
	}
	
	private ActivationNetwork createNetwork(Document document) {
		
		ActivationNetwork network = new ActivationNetwork();		
		Element root = document.getDocumentElement();
		
		int inputsCount = 0;
		int layersCount = 0;		
		Layer[] layers = null;
		double outputs[] = null;
		NormalizeInfo inputNormalizeInfo = null;
		NormalizeInfo targetNormalizeInfo = null;

		NodeList nodeList = root.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) nodeList.item(i);

				switch (element.getTagName()) {
				case "InputsCount":
					inputsCount = Integer.parseInt(element.getTextContent()); 
					break;
				case "LayersCount":
					layersCount = Integer.parseInt(element.getTextContent());
					break;
				case "Layers":
					layers = this.createActivationLayers(element, layersCount);
					break;
				case "Output":
					outputs = this.createDoubleValues(element);
					break;
				case "InputNormalizeInfo":
					inputNormalizeInfo = this.createNormalizeInfo(element);
					break;
				case "TargetNormalizeInfo":
					targetNormalizeInfo = this.createNormalizeInfo(element);
					break;
				}
			}
		}
		
		network.setInputsCount(inputsCount);
		network.setLayersCount(layersCount);
		network.setLayers(layers);
		network.setOutput(outputs);
		network.setInputNormalizeInfo(inputNormalizeInfo);		
		network.setTargetNormalizeInfo(targetNormalizeInfo);
		
		return network;
	}
	
	private Layer[] createActivationLayers(Element layersElement, int layersCount) {
		
		Layer[] layers = new Layer[layersCount];		
		NodeList layerNodeList = layersElement.getElementsByTagName("Layer");
		
		int index = 0;
		for (int i = 0; i < layerNodeList.getLength(); i++) {
			Node layerNode = layerNodeList.item(i);
			if (layerNode.getNodeType() == Node.ELEMENT_NODE) {
				
				Element layerElement = (Element)layerNode;
				
				int inputsCount = Integer.parseInt(layerElement.getElementsByTagName("InputsCount").item(0).getTextContent());
				int neuronsCount = Integer.parseInt(layerElement.getElementsByTagName("NeuronsCount").item(0).getTextContent());
				
				Element neuronsElement = this.getNodeListToFirstElement(layerElement.getElementsByTagName("Neurons"));
				Neuron[] neurons = this.createActivationNeuron(neuronsElement, neuronsCount);
				
				ActivationLayer activationLayer = new ActivationLayer();
				activationLayer.setInputsCount(inputsCount);
				activationLayer.setNeuronsCount(neuronsCount);
				activationLayer.setNeurons(neurons);
				layers[index] = activationLayer;
				index++;
			}
		}

		return layers;
	}
	
	private Neuron[] createActivationNeuron(Element neuronsElement, int neuronsCount) {
		Neuron[] neurons = new Neuron[neuronsCount];
		
		NodeList neuronNodeList = neuronsElement.getElementsByTagName("Neuron");
		
		int index = 0;
		for (int i = 0; i < neuronNodeList.getLength(); i++) {
			Node neuronNode = neuronNodeList.item(i);
			if (neuronNode.getNodeType() == Node.ELEMENT_NODE) {
				Element neuronElement = (Element)neuronNode;
				
				int inputsCount = Integer.parseInt(neuronElement.getElementsByTagName("InputsCount").item(0).getTextContent());				
				double output = Double.parseDouble(neuronElement.getElementsByTagName("Output").item(0).getTextContent());
				double threshold = Double.parseDouble(neuronElement.getElementsByTagName("Threshold").item(0).getTextContent());
				
				Element weightsElement = this.getNodeListToFirstElement(neuronElement.getElementsByTagName("Weights"));
				double weights[] = this.createDoubleValues(weightsElement);
				
				Element activationFunctionElement = this.getNodeListToFirstElement(neuronElement.getElementsByTagName("ActivationFunction"));
				IActivationFunction activationFunction = this.createIActivationFunction(activationFunctionElement);
				
				ActivationNeuron activationNeuron = new ActivationNeuron(inputsCount, activationFunction);
				activationNeuron.setOutput(output);
				activationNeuron.setThreshold(threshold);				
				activationNeuron.setWeights(weights);
				neurons[index] = activationNeuron;
				index++;
			}
		}

		return neurons;
	}
	
	private IActivationFunction createIActivationFunction(Element activationFunctionElement) {		
		IActivationFunction activationFunction = null;
		double alpha = Double.parseDouble(activationFunctionElement.getElementsByTagName("Alpha").item(0).getTextContent());
		String activationFunctionType = activationFunctionElement.getAttribute("xsi:type");

		Class<?> dynamicClass;
		try {
			dynamicClass = Class.forName("dhi.optimizer.algorithm.neuralNet." + activationFunctionType);
			Constructor<?> constructor = dynamicClass.getConstructor(new Class<?>[] { double.class });
			activationFunction = (IActivationFunction) constructor.newInstance(alpha);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return activationFunction;
	}
	
	private NormalizeInfo createNormalizeInfo(Element element) {
		NormalizeInfo normalizeInfo = new NormalizeInfo();
		double normalizeScale = Double.parseDouble(element.getElementsByTagName("NormalizeScale").item(0).getTextContent());
		double normalizeScaleHalf = Double.parseDouble(element.getElementsByTagName("NormalizeScaleHalf").item(0).getTextContent());

		Element scaleFactorsElement = this.getNodeListToFirstElement(element.getElementsByTagName("ScaleFactors"));
		double scaleFactors[] = this.createDoubleValues(scaleFactorsElement);

		Element rangesElement = this.getNodeListToFirstElement(element.getElementsByTagName("Ranges"));
		DoubleRange[] ranges = this.crateNormalizeDoubleRange(rangesElement);

		normalizeInfo.setNormalizeScale(normalizeScale);
		normalizeInfo.setNormalizeScaleHalf(normalizeScaleHalf);
		normalizeInfo.setScaleFactors(scaleFactors);
		normalizeInfo.setRanges(ranges);

		return normalizeInfo;
	}
	
	private DoubleRange[] crateNormalizeDoubleRange(Element element) {
		List<DoubleRange> doubleRangeList = new ArrayList<DoubleRange>();
		NodeList doubleRangeNodeList = element.getElementsByTagName("DoubleRange");
		for (int i = 0; i < doubleRangeNodeList.getLength(); i++) {
			Node doubleRangeNode = doubleRangeNodeList.item(i);
			if (doubleRangeNode.getNodeType() == Node.ELEMENT_NODE) {
				Element doubleRangeElement = (Element) doubleRangeNode;
				double min = Double.parseDouble(doubleRangeElement.getElementsByTagName("Min").item(0).getTextContent());
				double max = Double.parseDouble(doubleRangeElement.getElementsByTagName("Max").item(0).getTextContent());
				doubleRangeList.add(new DoubleRange(min, max));
			}
		}

		DoubleRange[] doubleRanges = doubleRangeList.toArray(new DoubleRange[doubleRangeList.size()]);
		return doubleRanges;
	}
	
	private double[] createDoubleValues(Element element) {

		NodeList nodeList = element.getElementsByTagName("double");
		List<Double> valuesList = new ArrayList<Double>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				valuesList.add(Double.parseDouble(node.getTextContent()));
			}
		}

		Double[] values = valuesList.toArray(new Double[valuesList.size()]);
		double[] resultValues= new double[values.length];
		for (int i = 0; i < resultValues.length; i++) {
			resultValues[i] = values[i];
		}

		return resultValues;
	}
	
	private Element getNodeListToFirstElement(NodeList nodeList) {
		Element element = null;
		if (nodeList != null && nodeList.getLength() > 0) {
			element = (Element)nodeList.item(0);
		}

		return element;
	}
}
