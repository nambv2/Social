package at.nambv.social.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReadXML {
	public static List<String> read(String pathname) {
		File xml = new File(pathname);
		List<String> listLink = new ArrayList<String>();
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance();
		try {
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document doc = builder.parse(xml);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("url");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String link = eElement.getElementsByTagName("loc").item(0)
							.getTextContent();
					//doc.renameNode(nNode, null, "done-url");
					eElement.setAttribute("used", "true");
					listLink.add(link);
					// write the content into xml file
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = transformerFactory.newTransformer();
					DOMSource source = new DOMSource(doc);
					StreamResult result = new StreamResult(new File(pathname));
					transformer.transform(source, result);
				}
			}
			return listLink;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String args[]) {
		read("src/main/resources/sitemap.com.xml");
	}
}
