package number;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.jupiter.api.*;
import org.junit.rules.TestRule;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BigNumberTest {

    static BigInteger bi1 = new BigInteger("0");
    static BigInteger bi2 = new BigInteger("0");
    static BigNumber b1 = new BigNumber("0");
    static BigNumber b2 = new BigNumber("0");
    static final BigInteger thresholdTop = new BigInteger("40439259280043854394384724185215749617014043121956053583069354910110521930087897686954214880699127781861079392681714927097809127646849818195762273579332978298089179502487838235347910285752692409785787478420483372749541044551245805611185128588941837082731512683451837605726025272724743514315790484732647013879");
    static final BigInteger thresholBottom = new BigInteger("14761283389341084194002215078975668519400256859157264982040334961059740939613021614698223379747222083756600079268157839569913528455569682736184083845992508723353976741692130256045632959847424355650571301381751510405049722072263914344963486309696781267160573814092959220315966655838299897980514271971826055673");

    BigInteger Ni = new BigInteger("156774238246875915835445233811147609873343164059043855105640486188519457028233998002402904757751807605965928975478596347242014672224727067633004297628621362248433746107510884059233724941256971566153218713448788958681155977990828154489255005329847271334645254216377998443414892683976459272966032042624948290751");
    BigInteger mi = new BigInteger("774238246875915835445233811147609873343164059043855105640486188519457028233998002402904757751807605965928975478596347242014672224727067633004297628621362248433746107510884059233724941256971566153218713448788958681155977990828154489255005329847271334645254216377998443414892683976459272966032042624948290751");
    BigNumber m = new BigNumber("774238246875915835445233811147609873343164059043855105640486188519457028233998002402904757751807605965928975478596347242014672224727067633004297628621362248433746107510884059233724941256971566153218713448788958681155977990828154489255005329847271334645254216377998443414892683976459272966032042624948290751");
    BigNumber T = new BigNumber("1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
    BigInteger Ti = new BigInteger("1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");

    static float time_add = 0;
    static float time_add_nat = 0;
    static float time_sub = 0;
    static float time_sub_nat = 0;
    static float time_prod_nat = 0;
    static float time_prod = 0;
    static float time_mod_add_nat = 0;
    static float time_mod_add = 0;
    static float time_mod_sub_nat = 0;
    static float time_mod_sub = 0;
    static float time_gcd_nat = 0;
    static float time_gcd = 0;
    static float time_mgy_nat = 0;
    static float time_mgy = 0;

    static final int NB_ROUND = 100;




    /** Prepare numbers for tests **/
    @BeforeEach
    public void setUp() {

        Random rand = new Random();

        bi1 = new BigInteger(1024,rand);
        bi2 = new BigInteger(1024,rand);

        while (bi1.compareTo(bi2)==-1) {
            bi1 = new BigInteger(1024,rand);
            bi2 = new BigInteger(1024,rand);
        }

        b1 = new BigNumber(bi1.toString());
        b2 = new BigNumber(bi2.toString());


    }

    @AfterAll
    public static void benchmark() {

        System.out.println("-------- Average Time Execution -------");
        System.out.println(" Addition Native : " + (time_add_nat/100)/1000 + "µs");
        System.out.println(" Addition : " + (time_add/100)/1000 + "µs");
        System.out.println(" Modular Add Native : " + (time_mod_add_nat/100)/1000 + "µs");
        System.out.println(" Modular Add : " + (time_mod_add/100)/1000 + "µs");
        System.out.println(" Substract Native : " + (time_sub_nat/100)/1000 + "µs");
        System.out.println(" Substract : " + (time_sub/100)/1000 + "µs");
        System.out.println(" Modular Sub Native : " + (time_mod_sub_nat/100)/1000 + "µs");
        System.out.println(" Modular Sub : " + (time_mod_sub/100)/1000 + "µs");
        System.out.println(" Product Native : " + (time_prod_nat/100)/1000 + "µs");
        System.out.println(" Product : " + (time_prod/100)/1000 + "µs");
        System.out.println(" GCD Native : " + (time_gcd_nat/100)/1000 + "µs");
        System.out.println(" GCD : " + (time_gcd/100)/1000 + "µs");
        System.out.println(" Montgomery Native : " + (time_mgy_nat/100)/1000 + "µs");
        System.out.println(" Montgomery : " + (time_mgy/100)/1000 + "µs");
    }


    /** Addition Big Numbers **/
    @RepeatedTest(NB_ROUND)
    void checkAddition() {

        Instant starti = Instant.now();
        BigInteger bi3 = bi1.add(bi2);
        Instant finishi = Instant.now();
        time_add_nat+= Duration.between(starti,finishi).toNanos();
        Instant start = Instant.now();
        BigNumber b3 = b1.add(b2);
        Instant finish = Instant.now();
        time_add+= Duration.between(start,finish).toNanos();

        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));

        assertEquals(bi3,res);

    }

    /** Substract Big Numbers **/
    @RepeatedTest(NB_ROUND)
    void checkSubstract() {

        Instant starti = Instant.now();
        BigInteger bi3 = bi1.subtract(bi2);
        Instant finishi = Instant.now();
        time_sub_nat+= Duration.between(starti,finishi).toNanos();

        Instant start = Instant.now();
        BigNumber b3 = b1.substract(b2);
        Instant finish = Instant.now();
        time_sub+= Duration.between(start,finish).toNanos();

        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));

        assertEquals(bi3.abs(),res);

    }

    /** Product of Big Numbers **/
    @RepeatedTest(NB_ROUND)
    void checkProduct() {

        Instant starti = Instant.now();
        BigInteger bi3 = bi1.multiply(bi2);
        Instant finishi = Instant.now();
        time_prod_nat+= Duration.between(starti,finishi).toNanos();

        Instant start = Instant.now();
        BigNumber b3 = b1.mul(b2);
        Instant finish = Instant.now();
        time_prod+= Duration.between(start,finish).toNanos();

        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));

        assertEquals(bi3,res);

    }


    /** Modular Addition Big Numbers **/
    @RepeatedTest(NB_ROUND)
    void checkModularAdd() {

        Instant starti = Instant.now();
        BigInteger bi3 = bi1.add(bi2).mod(mi);
        Instant finishi = Instant.now();
        time_mod_add_nat+= Duration.between(starti,finishi).toNanos();

        Instant start = Instant.now();
        BigNumber b3 = b1.modularAddition(b2,m);
        Instant finish = Instant.now();
        time_mod_add+= Duration.between(start,finish).toNanos();

        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));

        assertEquals(bi3,res);

    }

    /** Modular Substract Big Numbers **/
    @RepeatedTest(NB_ROUND)
    void checkModularSub() {

        Instant starti = Instant.now();
        BigInteger bi3 = bi1.subtract(bi2).mod(mi);
        Instant finishi = Instant.now();
        time_mod_sub_nat+= Duration.between(starti,finishi).toNanos();

        Instant start = Instant.now();
        BigNumber b3 = b1.modularSubstract(b2,m);
        Instant finish = Instant.now();
        time_mod_sub+= Duration.between(start,finish).toNanos();

        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));

        assertEquals(bi3.abs(),res);

    }

    /** GCD Big Numbers **/
    @RepeatedTest(NB_ROUND)
    void checkGCD() {

        Instant starti = Instant.now();
        BigInteger bi3 = bi1.gcd(bi2);
        Instant finishi = Instant.now();
        time_gcd_nat+= Duration.between(starti,finishi).toNanos();

        Instant start = Instant.now();
        BigNumber b3 = b1.gcd(b2);
        Instant finish = Instant.now();
        time_gcd+= Duration.between(start,finish).toNanos();

        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));

        assertEquals(bi3,res);

    }


    /** Montgomery Big Numbers  1024 bit Numbers **/
    @RepeatedTest(NB_ROUND)
    void checkMontgomery() {

        Instant starti = Instant.now();
        BigInteger bi3 = (bi1.multiply(bi2)).mod(Ni);
        Instant finishi = Instant.now();
        time_mgy_nat+= Duration.between(starti,finishi).toNanos();


        Instant start = Instant.now();
        BigNumber b3 = b1.mulMontgomery(b2);
        Instant finish = Instant.now();
        time_mgy+= Duration.between(start,finish).toNanos();

        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));
        assertEquals(bi3,res);

    }

    @RepeatedTest(NB_ROUND)
    void checkmodulo10(){

        Instant starti = Instant.now();
        BigInteger bi3 = (bi1).mod(Ti);
        Instant finishi = Instant.now();
        Instant start = Instant.now();
        BigNumber b3 = b1.mod10(T);
        Instant finish = Instant.now();
        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));
        assertEquals(bi3,res);
    }


    /** Modular Exponential **/
    /** Not functional ! **/
    //@Test
    void checkModularExponential() {

        /** Java BigInteger Librairy **/

        BigInteger ai = new BigInteger("13");
         int ki = 2;
        BigInteger bi3 = (ai.pow(ki)).mod(Ni);


        BigNumber a = new BigNumber("13");
        BigNumber k = new BigNumber("10");
        BigNumber temp = a.mod10(k);

        System.out.println("----------- Modular Exponential ----------");
        Instant start = Instant.now();
        BigNumber b3 = a.pow(k);
        Instant finish = Instant.now();
        System.out.println("Time Execution : " + Duration.between(start,finish).toNanos() / 1000 + " µs");
        System.out.println();

        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));
        assertEquals(bi3,res);

    }

}