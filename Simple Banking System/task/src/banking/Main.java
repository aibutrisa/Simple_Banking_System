package banking;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        DBWork dbWork = null;
        if (args[0].equals("-fileName")) {
            dbWork = new DBWork("jdbc:sqlite:" + args[1]);
        }
        dbWork.createNewDatabase();
        dbWork.createTable();
        Menu menu = new Menu(dbWork);
        Scanner scanner = new Scanner(System.in);
        while (!menu.getExit()) {
            menu.showMenu();
            int selected = Integer.parseInt(scanner.nextLine());
            menu.controller(selected);
        }
    }

}