package com.enigmastudios.rafaelmarkos;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        boolean exitFlag = false;

        while (!exitFlag) {
            System.out.println("Enter state name in all caps.");
            String inputState = sc.next();
            if (inputState.equals("EXIT")) {
                exitFlag = true;
            }
            else if (inputState.equals("KARNATAKA")||inputState.equals("LAKSHADWEEP")||inputState.equals("KERALA")) {
                new Thread(new FindStateTransaction(inputState)).start();
            }
            else {
                System.out.println("Enter a valid state name.");
            }
        }
    }
}
