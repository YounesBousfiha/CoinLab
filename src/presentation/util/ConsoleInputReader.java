package presentation.util;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleInputReader {

    private final Scanner scanner;

    public ConsoleInputReader() {
        this.scanner = new Scanner(System.in);
    }

    public String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int getIntInput(String prompt) {
        System.out.print(prompt);
        int input = scanner.nextInt();
        scanner.nextLine();
        return input;
    }

    public BigDecimal getBigDecimalInput(String prompt) {
        System.out.print(prompt);
        BigDecimal input = scanner.nextBigDecimal();
        scanner.nextLine();
        return input;
    }

    public void close() {
        scanner.close();
    }
}
