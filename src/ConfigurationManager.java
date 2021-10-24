import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class ConfigurationManager {
	
	Document readingDocument;
	Document writingDocument;
	Element rootElement;
	
	ConfigurationManager(){
		try {

			// Writing
			DocumentBuilderFactory writingDocFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder writingDocBuilder = writingDocFactory.newDocumentBuilder();
			writingDocument = writingDocBuilder.newDocument();
			rootElement = writingDocument.createElement(Constants.configurationField);
			writingDocument.appendChild(rootElement);
			
			// Reading
			File configurationFile = new File(Constants.configurationFile);
			if (!configurationFile.exists()) {
				readingDocument = null;
				return;
			}
			
			DocumentBuilderFactory readingDocFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder readingDocBuilder = readingDocFactory.newDocumentBuilder();
			readingDocument = readingDocBuilder.parse(configurationFile); 			
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getParameter(String parameterName) {
		if (readingDocument == null) {
			return "";
		}
		
		Node node = readingDocument.getElementsByTagName(parameterName).item(0).getFirstChild();
				
		if (node != null)
			return node.getNodeValue();
		else
			return "";
	}
	
	public void setParameter(String parameterName, String value){
		Element p = writingDocument.createElement(parameterName);
		p.appendChild(writingDocument.createTextNode(value));
		rootElement.appendChild(p);
	}
	

	public void saveConfiguration() {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(writingDocument);
			StreamResult result = new StreamResult(new File(Constants.configurationFile));
			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
