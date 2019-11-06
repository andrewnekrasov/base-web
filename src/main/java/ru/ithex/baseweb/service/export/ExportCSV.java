package ru.ithex.baseweb.service.export;

import org.apache.commons.csv.CSVPrinter;
import ru.ithex.baseweb.exception.ExportException;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Collection;

import static org.apache.commons.csv.CSVFormat.DEFAULT;

public interface ExportCSV<T> {
    String[] getRow(T data);
    String[] getHeaders();
    char getDelimiter();
    String getCharsetName();

    default byte[] printAllToCsv(Collection<T> data){
        StringWriter sw = new StringWriter();
        try (CSVPrinter printer = new CSVPrinter(sw,
                DEFAULT.withHeader(getHeaders()).withDelimiter(getDelimiter()))) {
            data.forEach(t -> printRecord(printer, t));
            printer.flush();
            return sw.toString().getBytes(Charset.forName(getCharsetName()));
        } catch (IOException e) {
            throw new ExportException("Ошибка формирования CSV файла", e);
        }
    }

    default void printRecord(CSVPrinter printer, T data){
        try{
            printer.printRecord(getRow(data));
        } catch (IOException e){
            throw new ExportException("Ошибка формирования CSV файла", e);
        }
    }
}
