package com.aftarobot.mlibrary.util;

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
        sb.append(random.nextInt(9));
        sb.append("-");
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        Log.d("ListUtil", "getRandomPolicyNumber: ".concat(sb.toString()));

        return sb.toString();
    }
    public static String getRandomDescription() {
        return "Life Insurance Cover";
    }
    public static String getRandomClaimId() {

        StringBuilder sb = new StringBuilder();
        sb.append("C");
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
        sb.append(random.nextInt(9));
        sb.append("-");
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        Log.d("ListUtil", "getRandomClaimNumber: ".concat(sb.toString()));

        return sb.toString();
    }
    public static String getRandomClaimApprovalId() {

        StringBuilder sb = new StringBuilder();
        sb.append("CA-");
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
        sb.append(random.nextInt(9));
        sb.append("-");
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        Log.d("ListUtil", "getRandomClaimApprovalNumber: ".concat(sb.toString()));

        return sb.toString();
    }
    public static String getRandomFTRid() {

        StringBuilder sb = new StringBuilder();
        sb.append("FTR-");
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
        sb.append(random.nextInt(9));
        sb.append("-");
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        Log.d("ListUtil", "getRandomFTRid: ".concat(sb.toString()));

        return sb.toString();
    }

    public static String getRandomTransferId() {

        StringBuilder sb = new StringBuilder();
        sb.append("TRN-");
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
        sb.append(random.nextInt(9));
        sb.append("-");
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        Log.d("ListUtil", "getRandomFTRid: ".concat(sb.toString()));

        return sb.toString();
    }

    public static String getRandomAccountNumber() {
        StringBuilder sb = new StringBuilder();
        sb.append("AC");
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        sb.append(random.nextInt(9));
        Log.d("ListUtil", "getRandomAccountNumber: ".concat(sb.toString()));

        return sb.toString();
    }

    private static final String[] types = {"Death Benefit", "Disability Benefit", "Pension", "Health Cover","Travel","Education"};
    private static final String[] fNames = {"Thabo", "Jonathan", "Roger", "Andre", "Harold", "John", "Thabiso", "Spikiri", "Stina",
            "Samuel", "Anthony", "Johan", "Peter", "Mpho", "Phumzile", "Veronica", "Maria", "Mary", "Meisie", "Jabulani", "Zorro",
            "Nelson", "Bathabile", "Bhuti", "Boetie", "Davis", "David", "Kwanele", "Hlupheka", "Denzel", "Daisy", "John B",
            "Lesego","Kenneth","Catherine","Nomonde","Nozipho","Wendy","Lindiwe", "Oupa", "Jimmy", "Benjamin", "Jimbo", "Bafana", "Joyce",
            "Billy", "Petrus", "Johannes", "Mmaphefo","Michael", "Jordan", "Nokuhle", "Marianne", "Franklin", "Jones",
            "Xavier", "Hunter", "Nancy", "Vincent", "Malenga", "Dlamani", "Msapa", "Geraldine","Gift", "Junior", "Prince",
            "Siyabonga", "Amogelang", "Lubanzi", "Mlungisi", "Bandile","Ayanda","Nokwanda", "Saviour", "Innocent", "Edgar", "Blessing",
            "Luthando","Princess","Angel", "Amahle","Sipho","Siphokazi", "Boetie", "Frank", "Nancy","Olivia","Olive", "Portia",
            "Khanyisa","Thompson","Derrick","Joseph", "Nkululeko","Kate", "Thomas","Daniel", "Lunga", "Donald",
            "Robert","Samuel","Patrick","John","Tracy","Ainsley","Ashley","Ashleigh","Lee","Tony","Barrick", "Jack", "Bernard", "Fikile",
            "Obama","Paul","George","Lebogang","lesetja", "Baker","Bobby","Robert","Sipho","Thebe","Maria","Kwanele","Andile","Joseph","Bryan",
            "Barack","Trent","Brady","Antonio","Bryce","Boyce","Boysie","Khensani","Tsakane","Mihloti","Raletsatsi",
            "Jennifer", "Susan", "Mmaphefo", "Ntini", "Mmapaseka","Paseka","Lesedi","Thapelo","Clive","Mpho"};
    private static final String[] lNames = {"Adams","Abrams","Macheke","Matime","Ngwenya","Moriri","Moroka", "Khoza", "van der Merwe", "Pieterse", "Maluleke",
            "Nkosi", "Malenga", "Mathibe", "Mthembu", "Shiluvane", "Shibambu", "Gumede", "Ngcobo","Zulu","Mkhize","Mokoena",
            "Khumalo", "Sithole","Ndlovu","Dlamini","Thebe","Sibanyoni","Bhengu","Mthembu","Nkosi",
            "Venter", "Jonathan", "Kelly", "Thebe", "Nhlapho", "Nkruma", "Sibiya", "Sibanyoni",
            "Motlhana", "Nthlese", "Petrus", "Henk", "Khulumani", "Jameson", "Walker", "Welker",
            "Brady", "Brown", "Wentz", "Foles","Peterson", "Agholor", "Nelson", "Frederick", "Franks",
             "Chomane", "Ngwenya", "Grootboom","Jackson", "Samuels", "Daniels", "Samuelson",
            "Bongomuffin", "Johnsen", "Patrick", "Els", "Venter", "Oscar", "Pretorius", "Dumisa",
            "Bethuel", "Johnson", "Smith", "Thulare","Maseko","Patterson", "Moloi", "Singh", "Gupta", "Booysens", "Thomas", "Renken",
            "Davids", "Davidson","Jeremiah", "Kuthile", "Nemavhandou", "Marule", "Baloyi", "Chauke", "Maringa", "Gondwe",
            "Booi", "Samuelson", "Gates", "Johansen", "Molefe","Masekwameng","Mthembu","Ngidi","Ngalalume", "Sibanyoni", "Mathebula", "Jones", "de Klerk", "Botha", "Bodibe"};
}
