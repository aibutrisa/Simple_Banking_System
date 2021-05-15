package banking;

import java.util.Random;

public class Account {
    private String accNumber;
    private String pinCode;
    private int balance;

    public Account(DBWork dbWork) {
        boolean isNew = false;
        String num = "";
        while (!isNew) {
            num = generateAccNumber();
            isNew = !dbWork.findNumber(num);
        }
        this.accNumber = num;
        this.pinCode = generatePin();
        this.balance = 0;
        dbWork.insert(this);
    }

    public Account() {

    }

    public Account(String accNumber, String pinCode, int balance) {
        this.accNumber = accNumber;
        this.pinCode = pinCode;
        this.balance = balance;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getAccNumber() {
        return this.accNumber;
    }

    public String getPinCode() {
        return this.pinCode;
    }

    public int getBalance() {
        return balance;
    }

    static String generateAccNumber() {
        String result = "";
        result = "400000";
        Random random = new Random();
        long randomNumber = Math.abs(random.nextLong());
        String formatted = String.format("%012d", randomNumber);
        result += formatted.substring(0, 9);
        result = checkSum(result);
        return result;
    }

    static String generatePin() {
        Random random = new Random();
        long randomNumber = Math.abs(random.nextInt(9999));
        String result = String.format("%04d", randomNumber);
        return result;
    }


    static String checkSum(String number) {
        char[] chars = number.toCharArray();
        int sum = 0;
        for (int i = 0; i < chars.length; i++) {
            int n = chars[i] - '0';
            n = i % 2 == 0 ? n * 2 : n;
            n = n > 9 ? n - 9 : n;
            sum += n;
        }
        int last = sum % 10 == 0 ? 0 : 10 - sum % 10;
        number += last;
        return number;
    }

    static boolean checkFullSum(String number) {
        char[] chars = number.toCharArray();
        int sum = 0;
        for (int i = 0; i < chars.length - 1; i++) {
            int n = chars[i] - '0';
            n = i % 2 == 0 ? n * 2 : n;
            n = n > 9 ? n - 9 : n;
            sum += n;
        }
        int last = sum % 10 == 0 ? 0 : 10 - sum % 10;
        boolean sumOK = last == chars[chars.length - 1] - '0';
        return sumOK;
    }
}
