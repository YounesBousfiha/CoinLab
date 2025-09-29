package com.jartiste;

import application.service.MemPoolService;
import application.service.TransactionService;
import application.service.WalletService;
import domain.repository.MempoolRepository;
import domain.repository.TransactionRepository;
import domain.repository.WalletRepository;
import infrastructure.config.Database;
import infrastructure.config.GlobalValue;
import infrastructure.exports.CSVExporter;
import infrastructure.exports.ExportService;
import infrastructure.exports.ExportService;
import infrastructure.persistance.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentation.ConsoleUI;
import presentation.controller.MempoolController;
import presentation.controller.TransactionController;
import presentation.controller.WalletController;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IllegalAccessException, SQLException {
            CoinLab app = new CoinLab();
            app.start();
    }
}

class CoinLab {
    public void start() throws IllegalAccessException, SQLException {
        ApplicationContext  context = ApplicationContext.create();
        ConsoleUI ui = context.getBean(ConsoleUI.class);
        ui.start();
    }
}

class ApplicationContext {
    private final Map<Class<?>, Object> beans = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);

    public static ApplicationContext create() throws SQLException {
        ApplicationContext context = new ApplicationContext();
        context.initialize();
        logger.info("Context Initialized");
        return context;
    }

    private void initialize() throws SQLException {
        boolean useDatabase = GlobalValue.USE_DATABASE;
        Connection db = Database.getInstance().getConnection();
        if (db == null) {
            throw new IllegalStateException("Database connection could not be established");
        }

        ConsoleUI ui = getConsoleUI(useDatabase, db);

        beans.put(ConsoleUI.class, ui);
    }

    private static ConsoleUI getConsoleUI(boolean useDatabase, Connection db) {
        WalletRepository walletRepository =  useDatabase ? new WalletRepoImpl(db) : new WalletMemRepoImpl();
        TransactionRepository transactionRepository = useDatabase ? new TransactionRepoImpl(db) : new TransactionMemRepoImpl();
        MemPoolService memPoolService = new MemPoolService(transactionRepository, walletRepository);

        WalletService walletService = new WalletService(walletRepository);
        TransactionService transactionService = new TransactionService(transactionRepository, walletRepository);

        CSVExporter csvExporter = new CSVExporter();
        ExportService exportService = new ExportService(transactionService, csvExporter);

        WalletController walletController = new WalletController(walletService);
        TransactionController transactionController = new TransactionController(transactionService, exportService);
        MempoolController mempoolController = new MempoolController(memPoolService);

        return new ConsoleUI(walletController, transactionController, mempoolController);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        return (T) beans.get(clazz);
    }
}