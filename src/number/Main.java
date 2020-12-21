package number;


import java.math.BigInteger;

public class Main {

    public static void main(String[] args) {

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
        BigNumber bg1 = new BigNumber("1 000000000");
        displayString(bg1);
        BigNumber bg2 = bg.modularSubstract(bg1, bg1);
        System.out.println("----- Output -----");
        displayString(bg2);
        System.out.println(bg.compareTo(bg1));
    }

    /**
     * Test methods for BigNumber implementation
     **/
    public static void displayString(BigNumber bg) {

        System.out.println(bg.getStrValue());
    }
}