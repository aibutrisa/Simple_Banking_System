package banking;

import java.util.Scanner;

public class Menu {
    private boolean isLogIn;
    private Account activeAccount;
    private boolean isExit;
    private DBWork dbWork;

    public Menu(DBWork dbWork) {
        this.isLogIn = false;
        this.isExit = false;
        this.dbWork = dbWork;
    }

    public void setExit(boolean isExit) {
        this.isExit = isExit;
    }

    public boolean getExit() {
        return this.isExit;
    }

    public void showMenu() {
        if (!this.isLogIn) {
            System.out.println("1. Create an account");
            System.out.println("2. Log into account");
            System.out.println("0. Exit");
        }
        else {
            System.out.println("1. Balance");
            System.out.println("2. Log out");
            System.out.println("0. Exit");
        }
    }

    public void controller(int selected) {
        if (!this.isLogIn) {
            switch (selected) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    logIn();
                    break;
                case 0:
                    exit();
                    break;
                default:
                    System.out.println("Unknown command");
                    break;
            }
        }
        else {
            switch (selected) {
                case 1:
                    showBalance();
                    break;
                case 2:
                    addIncome();
                    break;
                case 3:
                    Transfer();
                    break;
                case 4:
                    closeAccount();
                    break;
                case 5:
                    logOut();
                    break;
                case 0:
                    exit();
                    break;
                default:
                    System.out.println("Unknown command");
                    break;
            }
        }
    }

    private void showBalance() {
        System.out.println("Balance: " + this.activeAccount.getBalance());
    }

    private void exit() {
        System.out.println("Bye!");
        this.isExit = true;
    }

    private void logOut() {
        this.isLogIn = false;
        this.activeAccount = null;
        System.out.println("You have successfully logged out!");
    }

    private void addIncome() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter income:");
        int income = scanner.nextInt();
        activeAccount.setBalance(activeAccount.getBalance() + income);
        dbWork.setBalance(activeAccount.getAccNumber(), income);
        System.out.println("Income was added!");
    }

    private void Transfer() {
        Scanner scanner = new Scanner(System.in);
        boolean isCardOK = false;
        System.out.println("Transfer");
        System.out.println("Enter card number:");
        String receiverCard = scanner.nextLine();
        if (!Account.checkFullSum(receiverCard)) {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
        } else if (!dbWork.findNumber(receiverCard)) {
            System.out.println("Such a card does not exist.");
        } else {
            System.out.println("Enter how much money you want to transfer:");
            int transferSum = Integer.parseInt(scanner.nextLine());
            if (transferSum > activeAccount.getBalance()) {
                System.out.println("Not enough money!");
            } else {
                activeAccount.setBalance(activeAccount.getBalance() - transferSum);
                dbWork.setBalance(activeAccount.getAccNumber(), -transferSum);
                dbWork.setBalance(receiverCard, transferSum);
                System.out.println("Success!");
            }
        }
    }

    private void closeAccount() {
        dbWork.deleteAcc(activeAccount);
        activeAccount = null;
        isLogIn = false;
        System.out.println("The account has been closed!");
    }

    private void logIn() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your card number:");
        String accNum = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String pin = scanner.nextLine();
        Account acc = this.dbWork.findAccount(accNum, pin);
        if (acc == null) {
            System.out.println("Wrong card number or PIN!");
        } else {
            this.isLogIn = true;
            this.activeAccount = acc;
            System.out.println("You have successfully logged in!");
        }

    }

    public void createAccount() {
        Account account = new Account(this.dbWork);
        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(account.getAccNumber());
        System.out.println("Your card PIN:");
        System.out.println(account.getPinCode());
    }
}
