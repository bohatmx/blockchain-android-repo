package com.aftarobot.insurancecompany.adapters;

import android.util.Log;

import com.aftarobot.mlibrary.data.Beneficiary;
import com.aftarobot.mlibrary.data.Client;

import java.util.Random;

public class ListUtil {



    private static Random random = new Random(System.currentTimeMillis());

    public static Beneficiary getRandomBeneficiary() {
        Beneficiary beneficiary = new Beneficiary();
        int indexf = random.nextInt(fNames.length - 1);
        beneficiary.setFirstName(fNames[indexf]);
        int indexl = random.nextInt(lNames.length - 1);
        beneficiary.setLastName(lNames[indexl]);
        beneficiary.setEmail(beneficiary.getFirstName()
        .concat(".").concat(beneficiary.getLastName()).toLowerCase().concat("@email.com"));
        beneficiary.setAccountBalance(0.00);
        beneficiary.setIdNumber(getRandomIdNumber());
        return beneficiary;
    }
    public static Client getRandomClient() {
        Client client = new Client();
        int indexf = random.nextInt(fNames.length - 1);
        client.setFirstName(fNames[indexf]);
        int indexl = random.nextInt(lNames.length - 1);
        client.setLastName(lNames[indexl]);
        client.setEmail(client.getFirstName()
                .concat(".").concat(client.getLastName()).toLowerCase().concat("@company.com"));
        client.setAccountBalance(0.00);
        client.setDeceased(false);
        client.setIdNumber(getRandomIdNumber());

        return client;
    }
    public static double getRandomPolicyAmount() {
        int x = random.nextInt(9);
        switch (x) {
            case 0:
                return 500000;
            case 1:
                return 1500000;
            case 2:
                return 2000000;
            case 3:
                return 5000000;
            case 4:
                return 7500000;
            case 5:
                return 2500000;
            case 6:
                return 3000000;
            case 7:
                return 4000000;
            case 8:
                return 55000000;
            case 9:
                return 760000;

        }
        return 100000;
    }
    public static String getRandomIdNumber() {
        StringBuilder sb = new StringBuilder();
        sb.append("1");
        sb.append("9");
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));

        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));

        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        Log.d("ListUtil", "getRandomIdNumber: ".concat(sb.toString()));

        return sb.toString();
    }
    public static String getRandomPolicyNumber() {
        StringBuilder sb = new StringBuilder();
        sb.append("P");
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append("-");
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append("-");
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        Log.d("ListUtil", "getRandomPolicyNumber: ".concat(sb.toString()));

        return sb.toString();
    }
    public static String getRandomDescription() {
        int i = random.nextInt(types.length - 1);
        return types[i];
    }
    private static final String[] types = {"Death Benefit", "Disability Benefit"};
    private static final String[] fNames = {"Thabo", "Jonathan", "Roger", "Andre", "Harold", "John",
            "Samuel", "Anthony", "Johan", "Peter", "Mpho", "Phumzile", "Veronica",
            "Lesego","Kenneth","Catherine","Nomonde","Nozipho","Wendy","Lindiwe",
            "Billy", "Petrus", "Johannes", "Mmaphefo","Michael", "Jordan", "Nokuhle", "Marianne", "Franklin", "Jones",
            "Xavier", "Hunter", "Nancy", "Vincent", "Malenga", "Dlamani", "Msapa", "Geraldine",
            "Jennifer", "Susan", "Mmaphefo", "Ntini"};
    private static final String[] lNames = {"Moroka", "Khoza", "van der Merwe", "Pieterse", "Maluleke",
            "Nkosi", "Malenga", "Mathibe", "Venter", "Jonathan", "Kelly", "Thebe", "Nhlapho", "Nkruma",
            "Bethuel", "Johnson", "Smith", "Patterson", "Moloi", "Singh", "Gupta", "Booysens", "Thomas", "Renken",
            "Davids", "Davidson","Jeremiah", "Kuthile", "Nemavhandou", "Marule", "Baloyi", "Chauke", "Maringa", "Gondwe",
            "Booi", "Samuelson", "Gates", "Johansen", "Molefe", "Sibanyoni", "Mathebula", "Jones", "de Klerk", "Botha", "Bodibe"};
}
