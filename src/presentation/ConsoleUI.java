package presentation;

import application.dto.WalletDTO;
import domain.entity.Wallet;
import domain.repository.WalletAddressGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentation.controller.TransactionController;
import presentation.controller.WalletController;
import infrastructure.strategy.BtcAddressGenerator;
import infrastructure.strategy.EthAddressGenerator;
import presentation.response.WalletCreationResponse;
import presentation.util.ConsoleInputReader;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class ConsoleUI {

    private final Logger logger = LoggerFactory.getLogger(ConsoleUI.class);

    private final WalletController walletController;
    private final TransactionController transactionController;

    private final ConsoleInputReader inputReader;

    Map<String, WalletAddressGenerator> strategies = new HashMap<>();

    public ConsoleUI(WalletController walletController, TransactionController transactionController) {
        this.walletController = walletController;
        this.transactionController = transactionController;
        this.inputReader = new ConsoleInputReader();

        strategies.put("BITCOIN", new BtcAddressGenerator());
        strategies.put("ETHEREUM", new EthAddressGenerator());
    }

    public void start() throws IllegalAccessException {
        int choice;
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
                case 0:
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
        // get the source(sender) address & destination(receiver) address
        // Check if both Addresses exist,
        // choose the level of the transaction (ECONOMIQUE, STANDARD, RAPID)
        // based on the level Calculate the fees & set the Priority
        // Call the Transaction Create controller Method
        // receive a TransactionCreateResponse object

        // service

        //  receive the args from controller
        // Transfer those args to DTO
        // generate a UUID
        // call the Transaction repository for the save
        // call domain-service for both sender and receiver for consistency
        // return database Resultset
        // Map it to DTO again and return it back to controller
    }

    private void myPositionInMemPool() {
        // TODO: Ask for my Position in the Queue
        // TODO: Calculate Postion based on the fais ( reverse the fees Calculation Formula)
        // TODO: displya a message "Your Transaction is in Position (X, Y)"
        // TODO: Estimate the time based on position
    }

    private void feesLevelCompare() {
        // TODO: Compare the 3 level of fees
        // TODO: calculate the position in the mempool for each fees level
        // TODO: display the cost & the speed
        // TODO: display a table contain all those kind of information in concise way
    }


    private void checkMempool() {
        // TODO: display a list of the transactions ( PENDING)
        // TODO: display the feed for Each Transaction also
        // TODO: Mention my Trasaction as display  ">>> Your TX : Wallet Address"
    }
}