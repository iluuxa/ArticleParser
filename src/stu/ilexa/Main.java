package stu.ilexa;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    public static final String outputPath = "D:\\trash\\IdeaProjects\\ArticleParser\\TestData\\мандрик_25,01,22 (1).xlsx";
    public static final int outputIndex = 7;
    public static ArrayList<Interpreter> interpreters;

    public static void main(String[] args) {
        Parameters parameters = new Parameters();
        parseXML("D:\\trash\\IdeaProjects\\ArticleParser\\me.xml");
        /*Interpreter testInterpreter = new Interpreter("111111");
        ArrayList<String> testList =new ArrayList<String>();
        testList.add("one");
        testList.add("two");
        testList.add("three");
        testInterpreter.setBuffer(testList);
        GUIHandler.launchFrame(testInterpreter);*/
    }

    
    public static void parseEntry(Node Entry){
        NodeList children = Entry.getChildNodes();
        String supplierCode="null";
        String article="";
        Map<String, String> params = new TreeMap<>();
        for (int j = 0; j < children.getLength(); j++) {
            if(children.item(j).getNodeName().equals("producerCode")){
                article=children.item(j).getTextContent();
            }
            if(children.item(j).getNodeName().equals("supplierCode")){
                supplierCode = children.item(j).getTextContent();
            }
            if(children.item(j).getNodeName().equals("param")){
                params.put(children.item(j).getAttributes().getNamedItem("name").getTextContent(),children.item(j).getTextContent());
            }
        }
        boolean present = false;
        for (int j = 0; j < interpreters.size(); j++) {
            if(interpreters.get(j).getSupplierCode().equals(supplierCode)){
                present = true;
                interpreters.get(j).interpret(params, article);
                break;
            }
        }
        if(!present){
            Interpreter added = new Interpreter(supplierCode);
            interpreters.add(added);
            added.interpret(params,article);
        }
    }


    public static void parseXML(String path){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        interpreters = new ArrayList<>();
        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
            DocumentBuilder db = dbf.newDocumentBuilder();
            File xmlFile = new File(path);
            Document doc = db.parse(xmlFile);
            NodeList entries = doc.getElementsByTagName("offer");
            for (int i = 0; i < entries.getLength(); i++){
                parseEntry(entries.item(i));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
