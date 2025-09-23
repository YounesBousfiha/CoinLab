package presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentation.controller.TransactionController;
import presentation.controller.WalletController;

import java.util.Scanner;

public class ConsoleUI {

    private final Logger logger = LoggerFactory.getLogger(ConsoleUI.class);
    private final WalletController walletController;
    private final TransactionController transactionController;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleUI(WalletController walletController, TransactionController transactionController) {
        this.walletController = walletController;
        this.transactionController = transactionController;
    }

    public void start() {
        int choice;
        do {
            welcomeMenu();
            choice = scanner.nextInt();
            scanner.nextLine();

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
                default:
                    logger.warn("Invalid Choice !");
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
    }

    private void createWallet() {
        // TODO: Choose Wallet Type
        // TODO: Automatically Generate the Crypto Address Unique
        // TODO: Wallet will have a zero solde
        // TODO: give a wallet a unique ID ( for the db I think )
        // TODO: Check the Status From the ResponseController
    }

    private void makeTransaction() {
        // TODO: Source Address, Destination Address, Montant
        // TODO: choose the fees level ( ECONOMIQUE, STANDARD, RAPIDE )
        // TODO: Calculate the Fess based on the Cyrpto Type & the priorité
        // TODO: Create a trasanction with PENDING status
        // TODO: Give a UUID to this Transaction
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