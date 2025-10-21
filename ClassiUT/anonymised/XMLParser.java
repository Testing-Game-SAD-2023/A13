import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.NodeList;

/**
 * reads and  writes an XML file
 */
public class XMLParser {
    private Element root;
    private String fileName;
    File file;
    Document doc;
    Transformer transformer ;
    TransformerFactory tFactory;
    DocumentBuilder builder = null;
    DocumentBuilderFactory factory;
    /**
     * the constructor to read or write a file
     * @param fileName
     */
    public XMLParser(File file)throws Exception{
        this.fileName = file.getAbsolutePath();
        try {
            tFactory = TransformerFactory.newInstance();
            transformer = tFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            
            e.printStackTrace();
        }
        // make your xml doc into a DOMSource
        /* get an xml doc */
        factory = DocumentBuilderFactory.newInstance();
        
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            
            e.printStackTrace();
        }
        init();        
    }
    public XMLParser(String fileName)throws Exception {
        this.fileName = fileName;
        try {
            tFactory = TransformerFactory.newInstance();
            transformer = tFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            
            e.printStackTrace();
        }
        // make your xml doc into a DOMSource
        /* get an xml doc */
        factory = DocumentBuilderFactory.newInstance();
        
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            
            e.printStackTrace();
        }
        init();
    }
    /**
     * initializes the document
     *
     */
    private void init() throws Exception {
        file=new File(fileName);
        try {
            
            if(file.exists()) {
                builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                doc = builder.parse(file);
                root=doc.getDocumentElement();
            } else {
                doc = builder.newDocument();
                root=doc.createElement("data");
            }
        }catch(Exception e) {
            e.printStackTrace();
            throw new Exception("Not A Valid Format!");
        }
    }
    /**
     * sets a tagName of certain id with its elements and values
     * as
     * <tagName>
     * 		<element1>val1<element1>
     *  	<element2>val1<element2>
     * 		<element3>val1<element3>
     * </tagName>
     * @param tagName
     * @param id
     * @param elements
     */
    public void setProperty(String tagName,String id,Hashtable elements) {
        elements.remove("id");
        boolean replaced=false;
        /* create the root node */
        Element rowElement = doc.createElement(tagName);
        rowElement.setAttribute("id",id);
        NodeList nodeList = root.getElementsByTagName(tagName);
        String temp=null;
        /* create the column nodes and append each to the row node */
        Element columnElement;
        Text textData;
        Enumeration attributeEnum = elements.keys();
        while(attributeEnum.hasMoreElements()) {
            temp=(String)attributeEnum.nextElement();
            columnElement =doc.createElement(temp);
            textData = doc.createTextNode((String)elements.get(temp));
            columnElement.appendChild(textData);
            rowElement.appendChild(columnElement);
        }
        //*********************************************
        for(int i=0;i<nodeList.getLength();i++) {
            String keyID=((Element)nodeList.item(i)).getAttribute("id");
            if(keyID.equalsIgnoreCase(id)) {
                root.replaceChild(rowElement,nodeList.item(i));
                replaced=true;
            }
        }
        //*********************************************************
        if(!replaced) {
            root.appendChild(rowElement);
        }
        
        
    }
    public void addProperty(String tagName,String id,Hashtable elements) {
        /* create the root node */
        Element rowElement = doc.createElement(tagName);
        rowElement.setAttribute("id",id);
        NodeList nodeList = root.getElementsByTagName(tagName);
        String temp=null;
        /* create the column nodes and append each to the row node */
        Element columnElement;
        Text textData;
        Enumeration attributeEnum = elements.keys();
        int k=0;
        while(attributeEnum.hasMoreElements()) {
            temp=(String)attributeEnum.nextElement();
            columnElement =doc.createElement(temp);
            textData = doc.createTextNode((String)elements.get(temp));
            columnElement.appendChild(textData);
            rowElement.appendChild(columnElement);
        }
        //*********************************************
        root.appendChild(rowElement);
    }
    /**
     * gets all elments and values of all tags carry tagName
     * @param tagName
     * @return a vector of hashTable of all elements and values
     */
    public Vector getProperty(String tagName) {
        Vector text=new Vector();
        String text2=new String();
        String subTag=new String();
        boolean found = false;
        
        File file = new File(fileName);
        NodeList nodes = root.getElementsByTagName(tagName);
        String keyID=null;
        int i = 0;
        if(nodes.getLength() != 0)
            do{
            Hashtable table = new Hashtable();
            keyID=((Element)nodes.item(i)).getAttribute("id");
            table.put("id",keyID);
            NodeList lines = nodes.item(i).getChildNodes();
            for (int j = 0; j < lines.getLength(); j++){
                Node name = (Node)lines.item(j);
                // Collect the text from the <Line> element
                StringBuffer sb = new StringBuffer();
                for (Node child = name.getFirstChild();child != null;child = child.getNextSibling()){
                    if (child instanceof CharacterData){
                        CharacterData cd = (CharacterData) child;
                        sb.append(cd.getData());
                        text2 = sb.toString().trim();
                        //System.out.println("[XMLReader] "+child.getParentNode().getNodeName()+"="+text2);
                        table.put(child.getParentNode().getNodeName(),text2);
                    }
                }
            }
            i++;
            text.addElement(table);
            }while(i<nodes.getLength() && !found);
        return text;
    }
    /**
     * gets a value of a tag name having the id ==id
     * @param tagName
     * @param id
     * @return String  the value of the tag
     */
    public String getSingleProperty(String tagName) {
        String text = new String();
        boolean found = false;
        Hashtable table = new Hashtable();
        NodeList nodes = root.getElementsByTagName(tagName);
        int i = 0;
        if(nodes.getLength() != 0)
            do{
            StringBuffer sb = new StringBuffer();
            for (Node child = nodes.item(i).getFirstChild();child != null;child = child.getNextSibling()){
                if (child instanceof CharacterData){
                    CharacterData cd = (CharacterData) child;
                    sb.append(cd.getData());
                    text = sb.toString().trim();
                }
            }
            i++;
            }while(i<nodes.getLength() && !found);
        
        return text;
    }
    /**
     * stores the file in XML formate
     *
     */
    public void store() throws Exception {
        
        DOMSource source = new DOMSource(root);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
        Result result = new StreamResult(file );
        try {
            //		 stream it out
            transformer.transform(source, result);
        } catch (TransformerException e1){
            e1.printStackTrace();
            throw e1;
        }
    }
    public static String returnSpecial(String input){
        String output=input.replaceAll("&lt;","<");
        return output;
    }
}
