package stu.ilexa;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

import static stu.ilexa.Main.shouldWait;
import static stu.ilexa.Main.signal;

public class Interpreter {
    private String supplierCode; //Код поставщика - идентификатор интерпретатора
    private TreeMap<String, String> map = new TreeMap<>();//Карта ключ - значение, где ключ - название параметра в XML, а значение - множество соответствующих ему названий в XLS

    public Interpreter(String supplierCode, Map<String, String> params) {
        this.supplierCode = supplierCode;
        modify(params);
    }

    public void modify(Map<String, String> params) { //Метод, для внесения изменений в интерпретатор (карту)
        ArrayList<String> buffer = new ArrayList<>(); //Буфер для новых значений интерпретатора
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!map.containsKey(entry.getKey())) {
                buffer.add(entry.getKey());
            }
        }
        if (buffer.size() > 0) {
            GUIHandler guiHandler = new GUIHandler();
            shouldWait = true;
            guiHandler.launchFrame(this, buffer);
            try {
                synchronized (signal) {
                    while (shouldWait) {
                        signal.wait();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void interpret(Map<String, String> params, String article) { //Метод для интерпретации при помощи сохранённой карты
        //String outputPath = "D:\\trash\\IdeaProjects\\ArticleParser\\output";
        //String inputPath = "D:\\trash\\IdeaProjects\\ArticleParser\\TestData\\мандрик_25,01,22 (1).xlsx";
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(Main.outputPath)));
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                //System.out.println("Article = "+article+"   ;row article = "+row.getCell(6).getStringCellValue());
                if (row.getCell(6).getStringCellValue().equals(article)) {
                    //System.out.println("row number: "+row.getRowNum()+";map: "+map.toString());
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        int index = Parameters.findByName(entry.getKey());
                        if (index >= 0) {
                            //System.out.println("entry = " + entry.toString() + "; params = " + params.get(entry.getKey()) + "; index = " + index);
                            row.createCell(Main.outputIndex + index);
                            row.getCell(Main.outputIndex + index).setCellValue(params.get(entry.getKey()));
                        }
                    }
                        /*if (map.containsValue(sheet.getRow(0).getCell(7+i).getStringCellValue())){
                            for (Map.Entry<String, String> entry : map.entrySet()) {
                                if (entry.getValue().equals(sheet.getRow(0).getCell(7+i).getStringCellValue())) {
                                    row.getCell(i).setCellValue(params.get());
                                }
                            }
                        }*/
                }
            }
            workbook.write(new FileOutputStream(new File(Main.outputPath)));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void addKey(String key, String value) {
        map.put(key, value);
    }

}
