package number;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class BigNumberTest {

    /** Addition Big Numbers **/
    @Test
    void checkAddition() {

        /** Java BigInteger Librairy **/
        BigInteger bi1 = new BigInteger("911111220999999999999999999");
        BigInteger bi2 = new BigInteger("123456000999999999999999999");
        BigInteger bi3 = bi1.add(bi2);

        BigNumber b1 = new BigNumber("911111220 999999999 999999999");
        BigNumber b2 = new BigNumber("123456000 999999999 999999999");

        System.out.println("----------- Addition ----------");
        Instant start = Instant.now();
        BigNumber b3 = b1.add(b2);
        Instant finish = Instant.now();
        System.out.println("Time Execution : " + Duration.between(start,finish).toNanos() + " ns");
        System.out.println();

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

        System.out.println("----------- Substract ----------");
        Instant start = Instant.now();
        BigNumber b3 = b1.substract(b2);
        Instant finish = Instant.now();
        System.out.println("Time Execution : " + Duration.between(start,finish).toNanos() + " ns");
        System.out.println();

        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));

        assertEquals(bi3,res);

    }

    /** Product of Big Numbers **/
    @Test
    void checkProduct() {

        /** Java BigInteger Librairy **/
        BigInteger bi1 = new BigInteger("999999999999999999999999999999999999999999999999999999999999999999999999");
        BigInteger bi2 = new BigInteger("999999999999999999999999999999999999999999999999999999999999999999999999");
        BigInteger bi3 = bi1.multiply(bi2);

        BigNumber b1 = new BigNumber("999999999 999999999 999999999 999999999 999999999 999999999 999999999 999999999");
        BigNumber b2 = new BigNumber("999999999 999999999 999999999 999999999 999999999 999999999 999999999 999999999");

        System.out.println("----------- Product ----------");
        Instant start = Instant.now();
        BigNumber b3 = b1.mul(b2);
        Instant finish = Instant.now();
        System.out.println("Time Execution : " + Duration.between(start,finish).toNanos() + " ns");
        System.out.println();

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

        System.out.println("----------- Modular Add ----------");
        Instant start = Instant.now();
        BigNumber b3 = b1.modularAddition(b2,m);
        Instant finish = Instant.now();
        System.out.println("Time Execution : " + Duration.between(start,finish).toNanos() + " ns");
        System.out.println();

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

        System.out.println("----------- Modular Sub ----------");
        Instant start = Instant.now();
        BigNumber b3 = b1.modularSubstract(b2,m);
        Instant finish = Instant.now();
        System.out.println("Time Execution : " + Duration.between(start,finish).toNanos() + " ns");
        System.out.println();

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

        System.out.println("----------- GCD ----------");
        Instant start = Instant.now();
        BigNumber b3 = b1.gcd(b2);
        Instant finish = Instant.now();
        System.out.println("Time Execution : " + Duration.between(start,finish).toNanos() + " ns");
        System.out.println();

        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));

        assertEquals(bi3,res);

    }


    /** Montgomery Big Numbers  1024 bit Numbers **/
    //@Test
    void checkMontgomery() {

        /** Java BigInteger Librairy **/

        BigInteger bi1 = new BigInteger("40439259280043854394384724185215749617014043121956053583069354910110521930087897686954214880699127781861079392681714927097809127646849818195762273579332978298089179502487838235347910285752692409785787478420483372749541044551245805611185128588941837082731512683451837605726025272724743514315790484732647013879");
        BigInteger bi2 = new BigInteger("14761283389341084194002215078975668519400256859157264982040334961059740939613021614698223379747222083756600079268157839569913528455569682736184083845992508723353976741692130256045632959847424355650571301381751510405049722072263914344963486309696781267160573814092959220315966655838299897980514271971826055673");
        BigInteger Ni = new BigInteger("7971520080280298644174542803417156517178615435025425186480676981094128399431865856208238015348399292319757865199104520100559227777796315812072702946116913322743005475439558245697967627677298005389221988515950129465862169119384716949302086692761122836341458013094962673949142005642573359772131288211258545447");

        // R = 2¹⁰²⁴
        BigInteger Ri = new BigInteger("179769313486231590772930519078902473361797697894230657273430081157732675805500963132708477322407536021120113879871393357658789768814416622492847430639474124377767893424865485276302219601246094119453082952085005768838150682342462881473913110540827237163350510684586298239947245938479716304835356329624224137216");

        BigInteger bi3 = bi1.multiply(bi2).mod(Ni);



        BigNumber b1 = new BigNumber("40439259280043854394384724185215749617014043121956053583069354910110521930087897686954214880699127781861079392681714927097809127646849818195762273579332978298089179502487838235347910285752692409785787478420483372749541044551245805611185128588941837082731512683451837605726025272724743514315790484732647013879");
        BigNumber b2 = new BigNumber("14761283389341084194002215078975668519400256859157264982040334961059740939613021614698223379747222083756600079268157839569913528455569682736184083845992508723353976741692130256045632959847424355650571301381751510405049722072263914344963486309696781267160573814092959220315966655838299897980514271971826055673");

        // R = 2¹⁰²⁴
        BigNumber R = new BigNumber("179769313486231590772930519078902473361797697894230657273430081157732675805500963132708477322407536021120113879871393357658789768814416622492847430639474124377767893424865485276302219601246094119453082952085005768838150682342462881473913110540827237163350510684586298239947245938479716304835356329624224137216");
        // N < R
        BigNumber N = new BigNumber("7971520080280298644174542803417156517178615435025425186480676981094128399431865856208238015348399292319757865199104520100559227777796315812072702946116913322743005475439558245697967627677298005389221988515950129465862169119384716949302086692761122836341458013094962673949142005642573359772131288211258545447");

        // Use Java Native Librairy to find V from N and R
        BigInteger negN = new BigInteger("-7971520080280298644174542803417156517178615435025425186480676981094128399431865856208238015348399292319757865199104520100559227777796315812072702946116913322743005475439558245697967627677298005389221988515950129465862169119384716949302086692761122836341458013094962673949142005642573359772131288211258545447");
        // V = - N^(-1) mod R
        BigInteger Vi = negN.modInverse(Ri);
        BigNumber V = new BigNumber(Vi.toString());

        //Montgomery
        BigNumber b3 = b1.mulMontgomery(b2,N,R,V);

        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));

        assertEquals(bi3,res);

    }


    @Test
    void invV() {
        BigInteger N = new BigInteger("109");
        BigInteger R = new BigInteger("128");
        BigInteger negN = new BigInteger("-109");
        BigInteger V = negN.modInverse(R);
        assertEquals("27",V.toString());
    }
}