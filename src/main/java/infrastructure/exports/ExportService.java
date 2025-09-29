package infrastructure.exports;

import application.dto.TransactionDTO;
import application.service.TransactionService;
import domain.entity.Transaction;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportService {

    private final TransactionService transactionService;
    private final CSVExporter csvExporter;


    public ExportService(TransactionService transactionService, CSVExporter csvExporter) {
        this.transactionService = transactionService;
        this.csvExporter = csvExporter;
    }

    public void exportTransactionsToCSV() throws IOException {
        List<TransactionDTO> transactions = transactionService.getAllTransaction();

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filePath = "transactions_export_" + timestamp + ".csv";


        csvExporter.export(transactions, filePath);
    }
}
