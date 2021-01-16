package number;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class BigNumberTest {

    /** Addition Big Numbers **/
    @Test
    void checkAddition() {

        /** Java BigInteger Librairy **/
        BigInteger bi1 = new BigInteger("911111220999999999456789123");
        BigInteger bi2 = new BigInteger("123456000123456789987456321");
        BigInteger bi3 = bi1.add(bi2);

        BigNumber b1 = new BigNumber("911111220 999999999 456789123");
        BigNumber b2 = new BigNumber("123456000 123456789 987456321");
        BigNumber b3 = b1.add(b2);

        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));

        assertEquals(bi3,res);

    }

    /** Substract Big Numbers **/
    @Test
    void checkSubstract() {

        /** Java BigInteger Librairy **/
        BigInteger bi1 = new BigInteger("911111220999999999456789123");
        BigInteger bi2 = new BigInteger("123456000123456789987456321");
        BigInteger bi3 = bi1.subtract(bi2);

        BigNumber b1 = new BigNumber("911111220 999999999 456789123");
        BigNumber b2 = new BigNumber("123456000 123456789 987456321");
        BigNumber b3 = b1.substract(b2);

        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));

        assertEquals(bi3,res);

    }

    /** Product of Big Numbers **/
    @Test
    void checkProduct() {

        /** Java BigInteger Librairy **/
        BigInteger bi1 = new BigInteger("911111220999999999456789123");
        BigInteger bi2 = new BigInteger("123456000123456789987456321");
        BigInteger bi3 = bi1.multiply(bi2);

        BigNumber b1 = new BigNumber("911111220 999999999 456789123");
        BigNumber b2 = new BigNumber("123456000 123456789 987456321");
        BigNumber b3 = b1.mul(b2);

        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));

        assertEquals(bi3,res);

    }


    /** Modular Addition Big Numbers **/
    @Test
    void checkModularAdd() {

        /** Java BigInteger Librairy **/
        BigInteger mi = new BigInteger("126000000456789078789123023");
        BigInteger bi1 = new BigInteger("911111220999999999456789123");
        BigInteger bi2 = new BigInteger("123456000123456789987456321");
        BigInteger bi3 = bi1.add(bi2).mod(mi);

        BigNumber m = new BigNumber("126000000 456789078 789123023");
        BigNumber b1 = new BigNumber("911111220 999999999 456789123");
        BigNumber b2 = new BigNumber("123456000 123456789 987456321");
        BigNumber b3 = b1.modularAddition(b2,m);

        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));

        assertEquals(bi3,res);

    }

    /** Modular Substract Big Numbers **/
    @Test
    void checkModularSub() {

        /** Java BigInteger Librairy **/
        BigInteger mi = new BigInteger("126000000456789078789123023");
        BigInteger bi1 = new BigInteger("911111220999999999456789123");
        BigInteger bi2 = new BigInteger("123456000123456789987456321");
        BigInteger bi3 = bi1.subtract(bi2).mod(mi);

        BigNumber m = new BigNumber("126000000 456789078 789123023");
        BigNumber b1 = new BigNumber("911111220 999999999 456789123");
        BigNumber b2 = new BigNumber("123456000 123456789 987456321");
        BigNumber b3 = b1.modularSubstract(b2,m);

        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));

        assertEquals(bi3,res);

    }

    /** GCD Big Numbers **/
    @Test
    void checkGCD() {

        /** Java BigInteger Librairy **/
        BigInteger bi1 = new BigInteger("911111220999999999456789123");
        BigInteger bi2 = new BigInteger("123456000123456789987456321");
        BigInteger bi3 = bi1.gcd(bi2);

        BigNumber b1 = new BigNumber("911111220 999999999 456789123");
        BigNumber b2 = new BigNumber("123456000 123456789 987456321");
        BigNumber b3 = b1.gcd(b2);

        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));

        assertEquals(bi3,res);

    }
}