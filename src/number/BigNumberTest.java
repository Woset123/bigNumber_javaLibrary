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
        BigInteger bi1 = new BigInteger("40439259280043854394384724185215749617014043121956053583069354910110521930087897686954214880699127781861079392681714927097809127646849818195762273579332978298089179502487838235347910285752692409785787478420483372749541044551245805611185128588941837082731512683451837605726025272724743514315790484732647013879");
        BigInteger bi2 = new BigInteger("14761283389341084194002215078975668519400256859157264982040334961059740939613021614698223379747222083756600079268157839569913528455569682736184083845992508723353976741692130256045632959847424355650571301381751510405049722072263914344963486309696781267160573814092959220315966655838299897980514271971826055673");
        BigInteger bi3 = bi1.add(bi2);

        BigNumber b1 = new BigNumber("40439259280043854394384724185215749617014043121956053583069354910110521930087897686954214880699127781861079392681714927097809127646849818195762273579332978298089179502487838235347910285752692409785787478420483372749541044551245805611185128588941837082731512683451837605726025272724743514315790484732647013879");
        BigNumber b2 = new BigNumber("14761283389341084194002215078975668519400256859157264982040334961059740939613021614698223379747222083756600079268157839569913528455569682736184083845992508723353976741692130256045632959847424355650571301381751510405049722072263914344963486309696781267160573814092959220315966655838299897980514271971826055673");

        System.out.println("----------- Addition ----------");
        Instant start = Instant.now();
        BigNumber b3 = b1.add(b2);
        Instant finish = Instant.now();
        System.out.println("Time Execution : " +  Duration.between(start,finish).toNanos() / 1000 + " µs");
        System.out.println();

        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));

        assertEquals(bi3,res);

    }

    /** Substract Big Numbers **/
    @Test
    void checkSubstract() {

        /** Java BigInteger Librairy **/
        BigInteger bi1 = new BigInteger("40439259280043854394384724185215749617014043121956053583069354910110521930087897686954214880699127781861079392681714927097809127646849818195762273579332978298089179502487838235347910285752692409785787478420483372749541044551245805611185128588941837082731512683451837605726025272724743514315790484732647013879");
        BigInteger bi2 = new BigInteger("14761283389341084194002215078975668519400256859157264982040334961059740939613021614698223379747222083756600079268157839569913528455569682736184083845992508723353976741692130256045632959847424355650571301381751510405049722072263914344963486309696781267160573814092959220315966655838299897980514271971826055673");
        BigInteger bi3 = bi1.subtract(bi2);

        BigNumber b1 = new BigNumber("40439259280043854394384724185215749617014043121956053583069354910110521930087897686954214880699127781861079392681714927097809127646849818195762273579332978298089179502487838235347910285752692409785787478420483372749541044551245805611185128588941837082731512683451837605726025272724743514315790484732647013879");
        BigNumber b2 = new BigNumber("14761283389341084194002215078975668519400256859157264982040334961059740939613021614698223379747222083756600079268157839569913528455569682736184083845992508723353976741692130256045632959847424355650571301381751510405049722072263914344963486309696781267160573814092959220315966655838299897980514271971826055673");

        System.out.println("----------- Substract ----------");
        Instant start = Instant.now();
        BigNumber b3 = b1.substract(b2);
        Instant finish = Instant.now();
        System.out.println("Time Execution : " + Duration.between(start,finish).toNanos() / 1000 + " µs");
        System.out.println();

        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));

        assertEquals(bi3,res);

    }

    /** Product of Big Numbers **/
    @Test
    void checkProduct() {

        /** Java BigInteger Librairy **/
        BigInteger bi1 = new BigInteger("40439259280043854394384724185215749617014043121956053583069354910110521930087897686954214880699127781861079392681714927097809127646849818195762273579332978298089179502487838235347910285752692409785787478420483372749541044551245805611185128588941837082731512683451837605726025272724743514315790484732647013879");
        BigInteger bi2 = new BigInteger("14761283389341084194002215078975668519400256859157264982040334961059740939613021614698223379747222083756600079268157839569913528455569682736184083845992508723353976741692130256045632959847424355650571301381751510405049722072263914344963486309696781267160573814092959220315966655838299897980514271971826055673");

        BigInteger bi3 = bi1.multiply(bi2);

        BigNumber b1 = new BigNumber("40439259280043854394384724185215749617014043121956053583069354910110521930087897686954214880699127781861079392681714927097809127646849818195762273579332978298089179502487838235347910285752692409785787478420483372749541044551245805611185128588941837082731512683451837605726025272724743514315790484732647013879");
        BigNumber b2 = new BigNumber("14761283389341084194002215078975668519400256859157264982040334961059740939613021614698223379747222083756600079268157839569913528455569682736184083845992508723353976741692130256045632959847424355650571301381751510405049722072263914344963486309696781267160573814092959220315966655838299897980514271971826055673");

        System.out.println("----------- Product ----------");
        Instant start = Instant.now();
        BigNumber b3 = b1.mul(b2);
        Instant finish = Instant.now();
        System.out.println("Time Execution : " + Duration.between(start,finish).toNanos() / 1000 + " µs");
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
        System.out.println("Time Execution : " + Duration.between(start,finish).toNanos() / 1000 + " µs");
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
        System.out.println("Time Execution : " + Duration.between(start,finish).toNanos() / 1000 + " µs");
        System.out.println();

        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));

        assertEquals(bi3,res);

    }

    /** GCD Big Numbers **/
    @Test
    void checkGCD() {

        /** Java BigInteger Librairy **/
        BigInteger bi1 = new BigInteger("40439259280043854394384724185215749617014043121956053583069354910110521930087897686954214880699127781861079392681714927097809127646849818195762273579332978298089179502487838235347910285752692409785787478420483372749541044551245805611185128588941837082731512683451837605726025272724743514315790484732647013879");
        BigInteger bi2 = new BigInteger("14761283389341084194002215078975668519400256859157264982040334961059740939613021614698223379747222083756600079268157839569913528455569682736184083845992508723353976741692130256045632959847424355650571301381751510405049722072263914344963486309696781267160573814092959220315966655838299897980514271971826055673");
        BigInteger bi3 = bi1.gcd(bi2);

        BigNumber b1 = new BigNumber("40439259280043854394384724185215749617014043121956053583069354910110521930087897686954214880699127781861079392681714927097809127646849818195762273579332978298089179502487838235347910285752692409785787478420483372749541044551245805611185128588941837082731512683451837605726025272724743514315790484732647013879");
        BigNumber b2 = new BigNumber("14761283389341084194002215078975668519400256859157264982040334961059740939613021614698223379747222083756600079268157839569913528455569682736184083845992508723353976741692130256045632959847424355650571301381751510405049722072263914344963486309696781267160573814092959220315966655838299897980514271971826055673");

        System.out.println("----------- GCD ----------");
        Instant start = Instant.now();
        BigNumber b3 = b1.gcd(b2);
        Instant finish = Instant.now();
        System.out.println("Time Execution : " + Duration.between(start,finish).toNanos() / 1000 + " µs");
        System.out.println();

        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));

        assertEquals(bi3,res);

    }


    /** Montgomery Big Numbers  1024 bit Numbers **/
    @Test
    void checkMontgomery() {

        /** Java BigInteger Librairy **/

        BigInteger bi1 = new BigInteger("4440439259280043854394384724185215749617014043121956053583069354910110521930087897686954214880699127781861079392681714927097809127646849818195762273579332978298089179502487838235347910285752692409785787478420483372749541044551245805611185128588941837082731512683451837605726025272724743514315790484732647013879");
        BigInteger bi2 = new BigInteger("14789761283389341084194002215078975668519400256859157264982040334961059740939613021614698223379747222083756600079268157839569913528455569682736184083845992508723353976741692130256045632959847424355650571301381751510405049722072263914344963486309696781267160573814092959220315966655838299897980514271971826055673");
        BigInteger Ni = new BigInteger("156774238246875915835445233811147609873343164059043855105640486188519457028233998002402904757751807605965928975478596347242014672224727067633004297628621362248433746107510884059233724941256971566153218713448788958681155977990828154489255005329847271334645254216377998443414892683976459272966032042624948290751");
        BigInteger bi3 = (bi1.multiply(bi2)).mod(Ni);

        BigNumber b1 = new BigNumber("4440439259280043854394384724185215749617014043121956053583069354910110521930087897686954214880699127781861079392681714927097809127646849818195762273579332978298089179502487838235347910285752692409785787478420483372749541044551245805611185128588941837082731512683451837605726025272724743514315790484732647013879");
        BigNumber b2 = new BigNumber("14789761283389341084194002215078975668519400256859157264982040334961059740939613021614698223379747222083756600079268157839569913528455569682736184083845992508723353976741692130256045632959847424355650571301381751510405049722072263914344963486309696781267160573814092959220315966655838299897980514271971826055673");

        //Montgomery
        System.out.println("----------- Montgomery ----------");
        Instant start = Instant.now();
        BigNumber b3 = b1.mulMontgomery(b2);
        Instant finish = Instant.now();
        System.out.println("Time Execution : " + Duration.between(start,finish).toNanos() / 1000 + " µs");
        System.out.println();

        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));
        assertEquals(bi3,res);

    }


    /** Modular Exponential **/
    //@Test
    void checkModularExponential() {

        /** Java BigInteger Librairy **/

        BigInteger ai = new BigInteger("13");
        BigInteger Ni = new BigInteger("156774238246875915835445233811147609873343164059043855105640486188519457028233998002402904757751807605965928975478596347242014672224727067633004297628621362248433746107510884059233724941256971566153218713448788958681155977990828154489255005329847271334645254216377998443414892683976459272966032042624948290751");
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