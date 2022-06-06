package stu.ilexa;

import com.google.gson.Gson;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    public static final int preferredWidth = 900;
    public static final int preferredHeight = 600;
    public static String outputPath = "D:\\trash\\IdeaProjects\\ArticleParser\\TestData\\мандрик_25,01,22 (1).xlsx";
    public static String inputPath = "D:\\trash\\IdeaProjects\\ArticleParser\\TestData";
    public static String savePath = "D:\\trash\\IdeaProjects\\ArticleParser\\TestData\\save.txt";
    public static final String NUL = "";
    public static ArrayList<Interpreter> interpreters;
    public static final Object signal = new Object();
    public static volatile boolean shouldWait = true;
    public static int articleIndex = 6;
    public static int outputIndex = 5;

    public static void prepare(){
        File saveFile = new File(savePath);
        if (saveFile.exists() && !saveFile.isDirectory()) {
            restoreState();
        } else {
            new Parameters();
            interpreters = new ArrayList<>();
        }
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(Main.outputPath));
            XSSFSheet sheet = workbook.getSheetAt(0);
            Row firstRow = sheet.getRow(0);
            Iterator<Cell> cellIterator = firstRow.cellIterator();
            outputIndex=0;
            while (cellIterator.hasNext()){
                outputIndex++;
                Cell cell = cellIterator.next();
                if (cell.getStringCellValue().equals("Артикул")){
                    articleIndex=cell.getColumnIndex();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        GUIHandler.launchMenu();
        try {
            synchronized (signal) {
                while (shouldWait) {
                    signal.wait();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        prepare();


        shouldWait = true;
        File dir = new File(inputPath);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (child.getName().substring(child.getName().lastIndexOf(".") + 1).equals("xml")) {
                    parseXML(child);
                }
            }
        }
        saveState();
    }


    public static void parseEntry(Node Entry) {
        NodeList children = Entry.getChildNodes();
        System.out.println(Entry);
        String supplierCode = NUL;
        String article = NUL;
        Map<String, String> params = new TreeMap<>();
        for (int j = 0; j < children.getLength(); j++) {
            if (children.item(j).getNodeName().equals("producerCode")) {
                article = children.item(j).getTextContent();
                System.out.println(article);
            }
            if (children.item(j).getNodeName().equals("supplierCode")) {
                supplierCode = children.item(j).getTextContent();
            }
            if (children.item(j).getNodeName().equals("param")) {
                params.put(children.item(j).getAttributes().getNamedItem("name").getTextContent(), children.item(j).getTextContent());
            }
        }
        int present = -1;
        for (int j = 0; j < interpreters.size(); j++) {
            if (interpreters.get(j).getSupplierCode().equals(supplierCode)) {
                present = j;
                interpreters.get(j).modify(params);
                interpreters.get(j).interpret(params, article);
                break;
            }
        }
        if (present < 0) {
            Interpreter added = new Interpreter(supplierCode, params);
            interpreters.add(added);
            added.interpret(params, article);
        } else {
            interpreters.get(present).modify(params);
            interpreters.get(present).interpret(params, article);
        }
    }


    public static void parseXML(File file) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            NodeList entries = doc.getElementsByTagName("offer");
            for (int i = 0; i < entries.getLength(); i++) {
                System.out.println("Entry №" + i);
                parseEntry(entries.item(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void restoreState() {
        try {
            FileReader restorer = new FileReader(savePath);
            SaveState saveState = new Gson().fromJson(restorer, SaveState.class);
            interpreters = saveState.getInterpreters();
            Parameters.deletedParameters=saveState.getDeletedParameters();
            Parameters.setParameters(saveState.getParameters());
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(Main.outputPath));
            XSSFSheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(0);
            for (int i = 2; i < Parameters.getParameters().size(); i++) {
                row.createCell(Main.outputIndex+i);
                row.getCell(Main.outputIndex+i).setCellValue(Parameters.getParameters().get(i));
            }
            workbook.write(new FileOutputStream(Main.outputPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void saveState() {
        SaveState saveState = new SaveState(interpreters, Parameters.getParameters(),Parameters.deletedParameters);
        String jsonSaveState = new Gson().toJson(saveState);
        try {
            File saveFile = new File(savePath);
            if (!saveFile.exists() || saveFile.isDirectory()) {
                saveFile.createNewFile();
            }
            FileWriter saveWriter = new FileWriter(savePath);
            saveWriter.write(jsonSaveState);
            saveWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
