package presentation;

import java.util.Scanner;

public class Inputs implements KeyboardInputs {


    /**
     * @param defaultOption User will pick this option if invalid input
     * @param maxOption Max number in the options
     * @return chosen option
     */
    public int chooseOption(int defaultOption, int maxOption) {
        Scanner sc = new Scanner(System.in);
        try {
            defaultOption = sc.nextInt();
            if (defaultOption > maxOption)
                defaultOption = maxOption;
        }
        catch (Exception e) {
            System.err.println("Not a valid integer!");
        }
        return defaultOption;
    }


    public String getString() {
        Scanner sc = new Scanner(System.in);
        return sc.next();
    }

    public int getInt() {
        return chooseOption(0,Integer.MAX_VALUE);
    }

    public double getDouble() {
        Scanner sc = new Scanner(System.in);
        double result = 0;
        try {
            result = sc.nextDouble();
        }
        catch (Exception e) {
            System.err.println("Not a valid double");
        }
        return result;
    }

    public boolean getBoolean() {
        Scanner sc = new Scanner(System.in);
        boolean result = false;
        try {
            String s = sc.next();
            if (s.equals("s"))
                result = true;
        }
        catch (Exception e) {
            System.out.println("Not a valid boolean");
        }
        return result;
    }

    //NEW
    public int getNif(UILanguage lang){
        System.out.println(lang.getInsertYourNif());
        int nif = getInt();
        return nif;
    }
    //NEW
    public String getNome(UILanguage lang){
        System.out.println(lang.getInsertName());
        String nome = getString();
        return nome;
    }
    //NEW
    public String getEmail(UILanguage lang){
        System.out.println(lang.getInsertEmail());
        String email = getString();
        return email;
    }
    //NEW
    public String getPassword(UILanguage lang){
        System.out.println(lang.getInsertYourPassword());
        String password = getString();
        return password;
    }
    //NEW
    public double getSaldo(UILanguage lang){
        System.out.println(lang.getInsertSaldo());
        double saldo = getDouble();
        return saldo;
    }
}
