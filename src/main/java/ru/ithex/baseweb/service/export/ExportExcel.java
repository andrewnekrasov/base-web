package ru.ithex.baseweb.service.export;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import ru.ithex.baseweb.exception.ExportException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.IntStream;

public interface ExportExcel<T> {
    String[] getRow(T data);
    String[] getHeaders();

    default byte[] printAllToExcel(Collection<T> data){
        String[] headersExcel = getHeaders();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet();
        Row headers = sheet.createRow(0);
        IntStream.range(0, headersExcel.length).forEach(i -> {
            Cell cell = headers.createCell(i);
            cell.setCellValue(headersExcel[i]);
        });
        int rowNum = 1;
        for (T d : data){
            Row row = sheet.createRow(rowNum++);
            printRow(row, d);
        }
        try {
            wb.write(os);
            os.close();
            return os.toByteArray();
        } catch (IOException e) {
            throw new ExportException("Ошибка формирования Excel файла", e);
        }
    }

    default void printRow(Row row, T data){
        String[] rowData = getRow(data);
        for (int dataIndex = 0; dataIndex < rowData.length; dataIndex++){
            row.createCell(dataIndex).setCellValue(rowData[dataIndex] != null ? rowData[dataIndex] : "");
        }
    }
}
