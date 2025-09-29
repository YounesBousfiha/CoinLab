package presentation;

import application.dto.FeeLevelStatsDTO;
import application.dto.MemPoolPositionDTO;
import application.dto.TransactionDTO;
import application.dto.WalletDTO;
import domain.enums.Priority;
import domain.repository.WalletAddressGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentation.controller.MempoolController;
import presentation.controller.TransactionController;
import presentation.controller.WalletController;
import infrastructure.strategy.BtcAddressGenerator;
import infrastructure.strategy.EthAddressGenerator;
import presentation.response.*;
import presentation.util.ConsoleInputReader;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsoleUI {

    private final Logger logger = LoggerFactory.getLogger(ConsoleUI.class);

    private final WalletController walletController;
    private final TransactionController transactionController;
    private final MempoolController mempoolController;
    private final static String LINE = "+-------------+----------+--------------+------------------+%n";

    private final ConsoleInputReader inputReader;

    Map<String, WalletAddressGenerator> strategies = new HashMap<>();

    public ConsoleUI(WalletController walletController, TransactionController transactionController, MempoolController mempoolController) {
        this.walletController = walletController;
        this.transactionController = transactionController;
        this.mempoolController = mempoolController;
        this.inputReader = new ConsoleInputReader();

        strategies.put("BITCOIN", new BtcAddressGenerator());
        strategies.put("ETHEREUM", new EthAddressGenerator());
    }

    public void start() throws IllegalAccessException {
        int choice;
        this.mempoolController.startCycle();
        do {
            welcomeMenu();
            choice = inputReader.getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    createWallet();
                    break;
                case 2:
                    makeTransaction();
                    break;
                case 3:
                    myPositionInMemPool();
                    break;
                case 4:
                    feesLevelCompare();
                    break;
                case 5:
                    checkMempool();
                    break;
                case 6:
                    exportCSV();
                    break;
                case 0:
                    this.mempoolController.stopCycle();
                    System.out.println("Bye Bye ...");
                    break;
                default:
                    System.out.println("Invalid Choice !");
            }

        } while (0 != choice);
    }

    private void welcomeMenu() {
        System.out.println("\n Welcome to CoinLab , CryptoWallet Simulator");
        System.out.println("1: Create a wallet");
        System.out.println("2: Make new Transaction");
        System.out.println("3: Check my Position in memPool");
        System.out.println("4: Compare fees Levels");
        System.out.println("5: Check mempool stat");
        System.out.println("6: Export MemPool Status on CSV");
        System.out.println("0: quit");
    }

    private void createWallet() throws IllegalAccessException {
        String type = inputReader.getInput("Choose Wallet type e.g: BTC, ETH : ");
        WalletAddressGenerator generator = strategies.get(type.toUpperCase());

        if(null == generator) {
            logger.error("Address generator  is Null");
            return;
        }

        String address = generator.generate(type);

        WalletCreationResponse walletCreationResponse = this.walletController.create(type, address);

        if(walletCreationResponse.isSuccess()) {
            WalletDTO walletDTO = walletCreationResponse.getWallet();
            System.out.println("Wallet created successfully!");
            System.out.println("Wallet Address: " + walletDTO.getAddress());
            System.out.println("Wallet Type: " + walletDTO.getType());
            System.out.println("Wallet Balance: " + walletDTO.getBalance());
        } else {
            System.err.println("Wallet creation failed: " + walletCreationResponse.getErrorMessage());
        }
    }

    private void makeTransaction() {

        // get the source(sender) address & destination(receiver) address, amount
        String address = inputReader.getInput("Enter your Wallet Address: ");
        String receiverAddress = inputReader.getInput("Enter Receiver Wallet Address: ");
        BigDecimal amount = inputReader.getBigDecimalInput("Enter the Amount: ");

        // choose the level of the transaction (ECONOMIQUE, STANDARD, RAPID)
        Priority priority = Priority.valueOf(inputReader.getInput("Enter the Priority (ECONOMIQUE, STANDARD, RAPID): ").toUpperCase());

        // Call the Transaction Create controller Method
        TransactionCreateResponse transactionCreateResponse = this.transactionController.create(address, receiverAddress, amount, priority);

        // receive a TransactionCreateResponse object
        if(transactionCreateResponse.isSuccess()) {
            System.out.println("Transaction created successfully!");
            System.out.println("Transaction ID: " + transactionCreateResponse.getTransaction().getUuid());
            System.out.println("Transaction Status: " + transactionCreateResponse.getTransaction().getStatus());
            System.out.println("Transaction Fee: " + transactionCreateResponse.getTransaction().getFee());
        } else {
            logger.error("Transaction creation failed: {}", transactionCreateResponse.getErrorMessage());
        }
    }

    private void myPositionInMemPool() {

        String transactionID = inputReader.getInput("Enter your Transaction ID ( uuid )  : ");

        MemPoolPositionResponse memPoolPositionResponse = this.mempoolController.checkMyPosition(transactionID);

        if(memPoolPositionResponse.isSuccess()) {
            System.out.println("Your Position is Calculated SuccessFully");
            System.out.printf("Your Position is %d on total %d%n",
                    memPoolPositionResponse.getMemPoolPositionDTO().getMyCount(),
                    memPoolPositionResponse.getMemPoolPositionDTO().getTotal());
        } else {
            logger.error("Failed to Verify your current Position in the MemPool: {}",  memPoolPositionResponse.getErrorMessage());
        }
    }

    // Don't Forget to Check the EST TIME & SEE how to Make Display Correctly
    private void feesLevelCompare() {
        FeesLevelStatResponse response = this.mempoolController.checkFeesLevel();

        if(response.isSuccess()) {
            if(response.getFeeLevelStatsDTOList().isEmpty()) {
                logger.info("No Pending Transaction");
                return;
            }
            ConsoleUI.printFeeLevelTable(response.getFeeLevelStatsDTOList());
        } else {
            System.err.println("Failed to Fetch the Fees Level Stats " + response.getErrorMessage());
        }
    }


    private void checkMempool() {
        String myAddress = inputReader.getInput("Enter your Wallet address : ");
        MemPoolStatusResponse response = this.mempoolController.checkMemPoolStatus();
        if(response.isSuccess()) {
            if(response.getTransactions().isEmpty()) {
                logger.info("No Pending Transaction");
                return;
            }

            ConsoleUI.printMemPoolStatusTable(response.getTransactions(), myAddress);
         } else {
            logger.error("Failed to Fetch the Fees Level Stats {} " , response.getErrorMessage());
        }
    }


    private void exportCSV() {
        this.transactionController.exportCSV();
    }

    public static void printMemPoolStatusTable(List<TransactionDTO> memPoolStatusDTO, String myAddress) {
        String leftAlignFormat = "│ %-40s │ %-12s │%n";

        System.out.format(LINE);
        System.out.format("│ %-55s │ %-12s │%n", "Transaction (autres utilisateurs)", "Frais");
        System.out.format(LINE);

        for (TransactionDTO s : memPoolStatusDTO) {
            String txLabel = s.getSource().equals(myAddress)
                    ? ">>>> Your Tx : " + s.getUuid().toString()       // full UUID
                    : ">>>> (Anonym) : " + s.getUuid().toString().substring(0, 8) + "************************";

            System.out.format(leftAlignFormat, txLabel, s.getFee().toString());
        }

        System.out.format(LINE);
    }

    public static void printFeeLevelTable(List<FeeLevelStatsDTO> stats) {
        String leftAlignFormat = "| %-11s | %-8d | %-12s | %-16s |%n";

        System.out.format(LINE);
        System.out.format("| Fee Level   | Position | Fees         | Est. Time (min)  |%n");
        System.out.format(LINE);

        for (FeeLevelStatsDTO s : stats) {
            System.out.format(leftAlignFormat,
                    s.getPriority(),
                    s.getPosition(),
                    s.getFees().toString(),
                    s.getEst().toString()
            );
        }

        System.out.format(LINE);
    }
}