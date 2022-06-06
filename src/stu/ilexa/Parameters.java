package stu.ilexa;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

public class Parameters {
    private static ArrayList<String> parameters = new ArrayList<>();

    public static TreeSet<String> deletedParameters=new TreeSet<>();

    public static ArrayList<String> getParameters() {
        return parameters;
    }

    public static void addParameters(ArrayList<String> params){
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(Main.outputPath));
            XSSFSheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(0);
            for (int i = 0; i < params.size(); i++) {
                row.createCell(Main.outputIndex+i+parameters.size()-2);
                row.getCell(Main.outputIndex+parameters.size()-2+i).setCellValue(params.get(i));
            }
            workbook.write(new FileOutputStream(Main.outputPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        parameters.addAll(params);
    }

    public static int findByName(String name){
        for (int i = 0; i < parameters.size(); i++) {
            if(parameters.get(i).equals(name)){
                return i;
            }
        }
        return -1;
    }

    public Parameters() {
        parameters.add("Добавить");
        parameters.add("Удалить");
    }

    public static void setParameters(ArrayList<String> parameters) {
        Parameters.parameters = parameters;
    }
}
