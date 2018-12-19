package fileIO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class XMLUtils {

	   public static Document getDoc(Document configFile, String configDataFieldName, String dataPath){
			Element elem = getElement(configFile, configDataFieldName);
			String path = getString(elem, "path");
			String file = getString(elem, "name");
			
			Document doc = null; 
			try {
				doc = dBuilder.parse(new File(dataPath + path + file));
			} catch (SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return doc;
		}
	    
		
		/** Convenience document builder factory for XML. */
		public static DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		/** Convenience document builder for XML. */
		public static DocumentBuilder dBuilder;
		static {try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}}
		
		/** Convenience function to get the text value of an element. */
		public static String getString(Element element, String name){return(element.getElementsByTagName(name).item(0).getTextContent());}
		/** Convenience function to get the text value of an element. */
		public static String getString(Node node, String name){return(getString((Element)node, name));}
		
		/** Convenience function to get the parsed double value of the text value of an element. */
		public static double getDouble(Element element, String name){return(Double.parseDouble(element.getElementsByTagName(name).item(0).getTextContent()));}
		/** Convenience function to get the parsed double value of the text value of an element. */
		public static double getDouble(Node node, String name){return(getDouble((Element)node, name));}
		
		/** Convenience function to get the parsed double value of the text value of an element. */
		public static int getInt(Element element, String name){return(Integer.parseInt(element.getElementsByTagName(name).item(0).getTextContent()));}
		/** Convenience function to get the parsed double value of the text value of an element. */
		public static int getInt(Node node, String name){return(getInt((Element)node, name));}
		
		/** Convenience function to get the parsed double value of the text value of an element. */
		public static boolean getBool(Element element, String name){return(Boolean.parseBoolean(element.getElementsByTagName(name).item(0).getTextContent()));}
		/** Convenience function to get the parsed double value of the text value of an element. */
		public static boolean getBool(Node node, String name){return(getBool((Element)node, name));}

		/** Convenience function to get an Element from a Document. */
		public static Element getElement(Document doc, String name){
			return((Element)doc.getElementsByTagName(name).item(0));
		}
	
		public static double[] readDoubleArray(NodeList nodes, String name, String field){
			List<Double> vals = new ArrayList<Double>();
			for(int i = 0; i < nodes.getLength(); i++){
				Element elem = (Element)nodes.item(i);
				if(elem.getNodeName().equals(name)){
					vals.add(//Double.parseDouble(elem.getAttribute(field)));
							getDouble(elem, field));
				}
			}
			
			double[] out = new double[vals.size()];
			for(int i = 0; i < vals.size(); i++){
				out[i] = vals.get(i);
			}
			return out;
		}
		
		public static int[] readIntArray(NodeList nodes, String name, String field){
			List<Integer> vals = new ArrayList<Integer>();
			for(int i = 0; i < nodes.getLength(); i++){
				Element elem = (Element)nodes.item(i);
				if(elem.getNodeName().equals(name)){
					vals.add(//Double.parseDouble(elem.getAttribute(field)));
							getInt(elem, field));
				}
			}
			
			int[] out = new int[vals.size()];
			for(int i = 0; i < vals.size(); i++){
				out[i] = vals.get(i);
			}
			return out;
		}
		public static Element getElement(Node operation, String string, int i) {
			return (Element) ((Element)operation).getElementsByTagName("string").item(i);
		}
		

}
