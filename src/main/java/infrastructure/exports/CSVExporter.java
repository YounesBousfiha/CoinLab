package infrastructure.exports;


import application.dto.TransactionDTO;
import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class CSVExporter {
    private  final Logger logger = LoggerFactory.getLogger(CSVExporter.class);

    public CSVExporter() {
        /* init a empty class to make easy for DI */
    }


    public void export(List<TransactionDTO> transactions, String filePath) throws IOException {

        if (transactions == null || transactions.isEmpty()) {
            logger.warn("No transaction to export");
            return;
        }

        try(CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            Field[] fields = TransactionDTO.class.getDeclaredFields();
            String[] header = Arrays.stream(fields)
                    .map(Field::getName)
                    .toArray(String[]::new);
            writer.writeNext(header);


            for(TransactionDTO tx : transactions) {
                String[] row = Arrays.stream(fields)
                        .map(field -> {
                            field.setAccessible(true);
                            try {
                                Object value = field.get(tx);

                                return value != null ? value.toString() : "";
                            } catch (IllegalAccessException e) {
                                return "";
                            }
                        })
                        .toArray(String[]::new);
                writer.writeNext(row);
            }
        }
    }
}
