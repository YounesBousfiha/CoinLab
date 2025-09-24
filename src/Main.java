import application.service.TransactionService;
import application.service.WalletService;
import domain.repository.TransactionRepository;
import domain.repository.WalletRepository;
import infrastructure.config.Database;
import infrastructure.config.GlobalValue;
import infrastructure.persistance.TransactionMemRepoImpl;
import infrastructure.persistance.TransactionRepoImpl;
import infrastructure.persistance.WalletMemRepoImpl;
import infrastructure.persistance.WalletRepoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentation.ConsoleUI;
import presentation.controller.TransactionController;
import presentation.controller.WalletController;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {
            CoinLab app = new CoinLab();
            app.start();
    }
}

class CoinLab {
    public void start() throws IllegalAccessException {
        ApplicationContext  context = ApplicationContext.create();
        ConsoleUI ui = context.getBean(ConsoleUI.class);
        ui.start();
    }
}

class ApplicationContext {
    private final Map<Class<?>, Object> beans = new HashMap<>();

    private final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);

    public static ApplicationContext create() {
        ApplicationContext context = new ApplicationContext();
        context.initialize();
        return context;
    }

    private void initialize() {
        boolean useDatabase = GlobalValue.USE_DATABASE;
        Connection db = null;
        try {
            db = Database.getInstance().getConnection();
        } catch (SQLException e) {
            logger.error("Error in Database Getting Instance");
        }

        ConsoleUI ui = getConsoleUI(useDatabase, db);

        beans.put(ConsoleUI.class, ui);
    }

    private static ConsoleUI getConsoleUI(boolean useDatabase, Connection db) {
        WalletRepository walletRepository =  useDatabase ? new WalletRepoImpl(db) : new WalletMemRepoImpl();
        TransactionRepository transactionRepository = useDatabase ? new TransactionRepoImpl(db) : new TransactionMemRepoImpl();


        WalletService walletService = new WalletService(walletRepository);
        TransactionService transactionService = new TransactionService(transactionRepository);

        WalletController walletController = new WalletController(walletService);
        TransactionController transactionController = new TransactionController(transactionService);

        return new ConsoleUI(walletController, transactionController);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        return (T) beans.get(clazz);
    }
}