package chalmers.manel.jps;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class JPSTileMap {

	//Data stored in tiles
	private int widthTiles; 
	private int heightTiles;
	private int sizeTile;
	private boolean[] canWalk;
	private int[] costWalk;
	
	//Data stored in pixels
	private  int widthPixels;
	private int heightPixels;
	
	public JPSTileMap(String file){
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docBuilderFactory.newDocumentBuilder();
			InputStream is = getClass().getClassLoader().getResourceAsStream("maps/info/mapsInfo.xml");
			Document doc = docBuilder.parse(is);
		} 
		catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
