package chalmers.manel.jps;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import chalmers.manel.jps.exceptions.MapNotFoundInMapsInfoXML;

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
	
	public JPSTileMap(int map) throws MapNotFoundInMapsInfoXML{
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docBuilderFactory.newDocumentBuilder();
			InputStream is = getClass().getClassLoader().getResourceAsStream("maps/info/mapsInfo.xml");
			Document doc = docBuilder.parse(is);
			doc.getDocumentElement().normalize();
			
			NodeList listOfMaps = doc.getElementsByTagName("map");
			int totalMaps = listOfMaps.getLength();
			if(map > totalMaps) throw new MapNotFoundInMapsInfoXML("Map idex out of range");
			
			Node parseMap = listOfMaps.item(map);
			if(parseMap.getNodeType() == Node.ELEMENT_NODE){
				Element myMap = (Element) parseMap;
				
				NodeList widthTilesList = myMap.getElementsByTagName("widthTiles");
                Element widthTiles = (Element)widthTilesList.item(0);

                NodeList textFNList = widthTiles.getChildNodes();
                System.out.println("width Tiles : " + 
                       ((Node)textFNList.item(0)).getNodeValue().trim());
                this.widthTiles = Integer.parseInt(((Node)textFNList.item(0)).getNodeValue().trim());
                
                
                NodeList heightTilesList = myMap.getElementsByTagName("heightTiles");
                Element heightTiles = (Element)heightTilesList.item(0);

                textFNList = heightTiles.getChildNodes();
                System.out.println("height Tiles : " + 
                       ((Node)textFNList.item(0)).getNodeValue().trim());
                this.heightTiles = Integer.parseInt(((Node)textFNList.item(0)).getNodeValue().trim());
                
                
                NodeList sizeTilesList = myMap.getElementsByTagName("sizeTiles");
                Element sizeTiles = (Element)sizeTilesList.item(0);

                textFNList = sizeTiles.getChildNodes();
                System.out.println("sizeTiles : " + 
                       ((Node)textFNList.item(0)).getNodeValue().trim());
                this.sizeTile = Integer.parseInt(((Node)textFNList.item(0)).getNodeValue().trim());

                
                NodeList movementMapList = myMap.getElementsByTagName("movementMap");
                Element movementMap = (Element)movementMapList.item(0);

                textFNList = movementMap.getChildNodes();
                System.out.println("movementMap : " + 
                       ((Node)textFNList.item(0)).getNodeValue().trim());
                String movementMapPath = ((Node)textFNList.item(0)).getNodeValue().trim();
                
                NodeList costMapList = myMap.getElementsByTagName("costMap");
                Element costMap = (Element)costMapList.item(0);

                textFNList = costMap.getChildNodes();
                System.out.println("costMap : " + 
                       ((Node)textFNList.item(0)).getNodeValue().trim());
                String costMapPath = ((Node)textFNList.item(0)).getNodeValue().trim();
                
                NodeList tileMapList = myMap.getElementsByTagName("tileMap");
                Element tileMap = (Element)tileMapList.item(0);

                textFNList = tileMap.getChildNodes();
                System.out.println("tileMap : " + 
                       ((Node)textFNList.item(0)).getNodeValue().trim());
                String tileMapPath = ((Node)textFNList.item(0)).getNodeValue().trim();
                
                
			}
			
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
