package Services;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class ExcelDataService {
    private final String filePath;

    public ExcelDataService(String filePath) {
        this.filePath = filePath;
    }

    public Map<String, String> getQuestionAnswerMap() {
        Map<String, String> qaMap = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
             
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // skip header

                Cell questionCell = row.getCell(0);
                Cell answerCell = row.getCell(1);

                if (questionCell != null && answerCell != null) {
                    qaMap.put(questionCell.getStringCellValue().trim(),
                            answerCell.getStringCellValue().trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return qaMap;
    }
}
