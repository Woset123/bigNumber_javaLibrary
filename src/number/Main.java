package number;


import java.math.BigInteger;

import static java.lang.Math.log;

public class Main {

    public static void main(String[] args) {
        String st = "719077253944926363091722076315609893447190791576922629093720324630930703222003852530833909289630144084480455519485573430635159075257666489971389722557896497511071573699461941105208878404984376477812331808340023075352602729369851525895652442163308948653402042738345192959788983753918865219341425318496896548864";
        //System.out.println(st.length());
         int nbBits = (int) (st.length() * log(10) / log(2));
         //System.out.println(nbBits);
        //BigInteger bi = new BigInteger("100000001000000000000000000000000000000000100000000",2);
        //System.out.println(bi.toString());
        test();
    }


    /**
     * To test BigNumber implementation (not functional yet !)
     **/
    public static void test() {
        System.out.println("----- Number 1 -----");
        BigNumber bg = new BigNumber("2 234567890");

        displayString(bg);
        System.out.println("----- Number 2 -----");
        BigNumber huge = new BigNumber("1234 567891011");
        displayString(huge);
        BigNumber bg2 = bg.add(huge);
        System.out.println("----- Output -----");
        displayString(bg2);
    }

    /**
     * Test methods for BigNumber implementation
     **/
    public static void displayString(BigNumber bg) {

        System.out.println(bg.getStrValue());
    }
}