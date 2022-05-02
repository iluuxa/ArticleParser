package stu.ilexa;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class Interpreter {
    private String supplierCode; //Код поставщика - идентификатор интерпретатора
    private TreeMap<String, Set<String>> map=new TreeMap<>();//Карта ключ - значение, где ключ - название параметра в XML, а значение - множество соответствующих ему названий в XLS

    public Interpreter(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public void interpret(Map<String, String> params, String article){
        ArrayList<String> buffer=new ArrayList<>(); //Буфер для новых значений интерпретатора
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!map.containsValue(entry.getKey())){
                buffer.add(entry.getKey());
            }
        }
        if (buffer.size()>0){
            GUIHandler guiHandler = new GUIHandler();
            guiHandler.launchFrame(this,buffer,article,params);
        }
        else{
            transform(params, article);
        }
    }

    public void transform(Map<String, String> params, String article){
        //String outputPath = "D:\\trash\\IdeaProjects\\ArticleParser\\output";
        //String inputPath = "D:\\trash\\IdeaProjects\\ArticleParser\\TestData\\мандрик_25,01,22 (1).xlsx";
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(Main.outputPath)));
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getCell(6).getStringCellValue().equals(article)){
                    for (int i = 0; i < params.size(); i++) {
                        if (map.containsValue(sheet.getRow(0).getCell(7+i).getStringCellValue())){
                            for (Map.Entry<String, String> entry : map.entrySet()) {
                                if (entry.getValue().equals(sheet.getRow(0).getCell(7+i).getStringCellValue())) {
                                    row.getCell(i).setCellValue(params.get());
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void addKey(String key, String value){
        map.put(key,value);
    }

}
