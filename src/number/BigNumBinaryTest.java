package number;

import org.junit.jupiter.api.*;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;


class BigNumBinaryTest {

    static final int NB_ROUND = 100;
    static final int NB_BITS = 1024;

    static BigInteger bi1 = new BigInteger("0");
    static BigInteger bi2 = new BigInteger("0");
    static BigNumBinary b1 = new BigNumBinary("0");
    static BigNumBinary b2 = new BigNumBinary("0");
    BigInteger Ni = new BigInteger("418713614917746285664413241196285227616976490652860137408300418386353935197688405516546842692607677116851921378035841324523686048327507743925671731316270816216157528551064220383712102296264083210945915857583736652773968485519278960697560444980184260615269285148307059096169025138685786665459825500053487097");
    BigInteger Ri = new BigInteger("359538626972463181545861038157804946723595395788461314546860162315465351611001926265416954644815072042240227759742786715317579537628833244985694861278948248755535786849730970552604439202492188238906165904170011537676301364684925762947826221081654474326701021369172596479894491876959432609670712659248448274432");


    static float time_add = 0;
    static float time_add_nat = 0;
    static float time_sub = 0;
    static float time_sub_nat = 0;
    static float time_prod_nat = 0;
    static float time_prod = 0;
    static float time_pow_nat = 0;
    static float time_pow = 0;
    static float time_mgy_nat = 0;
    static float time_mgy = 0;
    static float time_cipher = 0;
    static float time_decipher = 0;




    /** Prepare numbers for tests **/
    @BeforeAll
    public static void setUp() {

        Random rand = new Random();
        bi1 = new BigInteger(NB_BITS,rand);
        bi2 = new BigInteger(NB_BITS,rand);
        while (bi1.compareTo(bi2)==-1) {
            bi1 = new BigInteger(NB_BITS,rand);
            bi2 = new BigInteger(NB_BITS,rand);
        }
        b1 = new BigNumBinary(bi1.toString());
        b2 = new BigNumBinary(bi2.toString());


    }

    @AfterAll
    public static void benchmark() {

        System.out.println("-------- Average Time Execution -------");
        System.out.println(" Addition Native : " + (time_add_nat/NB_ROUND)/1000 + "µs");
        System.out.println(" Addition : " + (time_add/NB_ROUND)/1000 + "µs");
        System.out.println(" Substract Native : " + (time_sub_nat/NB_ROUND)/1000 + "µs");
        System.out.println(" Substract : " + (time_sub/NB_ROUND)/1000 + "µs");
        System.out.println(" Product Native : " + (time_prod_nat/NB_ROUND)/1000 + "µs");
        System.out.println(" Product : " + (time_prod/NB_ROUND)/1000 + "µs");
        System.out.println(" Montgomery Native : " + (time_mgy_nat/NB_ROUND)/1000 + "µs");
        System.out.println(" Montgomery : " + (time_mgy/NB_ROUND)/1000 + "µs");
        System.out.println(" Power Mod Native : " + (time_pow_nat/NB_ROUND)/1000 + "µs");
        System.out.println(" Power Mod: " + (time_pow/NB_ROUND)/1000 + "µs");
        System.out.println(" RSA Cipher : " + (time_cipher/NB_ROUND)/1000 + "µs");
        System.out.println(" RSA decipher: " + (time_decipher/NB_ROUND)/1000 + "µs");
    }

    @RepeatedTest(NB_ROUND)
    void checkAddition() {

        Instant start = Instant.now();
        BigNumBinary b3 = b1.add(b2);
        Instant finish = Instant.now();
        time_add+= Duration.between(start,finish).toNanos();
        Instant starti = Instant.now();
        BigInteger bi3 = bi1.add(bi2);
        Instant finishi = Instant.now();
        time_add_nat+= Duration.between(starti,finishi).toNanos();
        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));
        assertEquals(bi3,res);

    }

    @RepeatedTest(NB_ROUND)
    void checkSubstract() {

        Instant start = Instant.now();
        BigNumBinary b3 = b1.substract(b2);
        Instant finish = Instant.now();
        time_sub+= Duration.between(start,finish).toNanos();
        Instant starti = Instant.now();
        BigInteger bi3 = bi1.subtract(bi2);
        Instant finishi = Instant.now();
        time_sub_nat+= Duration.between(starti,finishi).toNanos();
        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));
        assertEquals(bi3,res);

    }

    @RepeatedTest(NB_ROUND)
    void checkProduct() {

        Instant starti = Instant.now();
        BigInteger bi3 = bi1.multiply(bi2);
        Instant finishi = Instant.now();
        time_prod_nat+= Duration.between(starti,finishi).toNanos();
        Instant start = Instant.now();
        BigNumBinary b3 = b1.multiply(b2);
        Instant finish = Instant.now();
        time_prod+= Duration.between(start,finish).toNanos();
        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));
        assertEquals(bi3,res);

    }

    @RepeatedTest(NB_ROUND)
    void checkShiftLeft() {
        BigInteger bi3 = bi1.shiftLeft(35);
        BigNumBinary b3 = b1.shiftLeft(35);
        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));
        assertEquals(bi3,res);
    }

    @RepeatedTest(NB_ROUND)
    void checkShiftRight() {
        BigInteger bi3 = bi1.shiftRight(1024);
        BigNumBinary b3 = b1.shiftRight(1024);
        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));
        assertEquals(bi3,res);
    }

    @RepeatedTest(NB_ROUND)
    void checkMontgomery() {

        Instant starti = Instant.now();
        BigInteger bi3 = bi1.multiply(bi2).mod(Ni);
        Instant finishi = Instant.now();
        time_mgy_nat+= Duration.between(starti,finishi).toNanos();
        Instant start = Instant.now();
        BigNumBinary b3 = b1.multiMontgomery(b2);
        Instant finish = Instant.now();
        time_mgy+= Duration.between(start,finish).toNanos();
        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));
        assertEquals(bi3,res);

    }

    @RepeatedTest(NB_ROUND)
    void checkPow() {
        Instant starti = Instant.now();
        BigInteger bi3 = bi1.pow(1234).mod(Ni);
        Instant finishi = Instant.now();
        time_pow_nat+= Duration.between(starti,finishi).toNanos();
        BigNumBinary k = new BigNumBinary("1234");
        Instant start = Instant.now();
        BigNumBinary b3 = b1.pow(k);
        Instant finish = Instant.now();
        time_pow+= Duration.between(start,finish).toNanos();
        BigInteger res = new BigInteger(b3.getStrValue().replaceAll("\\s+", ""));
        assertEquals(bi3,res);
    }

    @Test
    void rsa() {

        /* Native */
        BigInteger pi = new BigInteger("2959364845584404774681874866699930264745188640319649975173889615270725917698040085342812166572004848989924744681946756278670542661612783092439235914425599");
        BigInteger qi = new BigInteger("141487662645752696996593879975567182731928867224648958671751227658898258903155261738089670161811174537412981979867855443409827861294529455093856978109703");
        BigInteger ei = new BigInteger("1009");
        BigInteger phi_i = pi.subtract(BigInteger.ONE).multiply(qi.subtract(BigInteger.ONE));
        BigInteger di = ei.modInverse(phi_i);


        BigNumBinary p = new BigNumBinary("2959364845584404774681874866699930264745188640319649975173889615270725917698040085342812166572004848989924744681946756278670542661612783092439235914425599");
        BigNumBinary q = new BigNumBinary("141487662645752696996593879975567182731928867224648958671751227658898258903155261738089670161811174537412981979867855443409827861294529455093856978109703");
        BigNumBinary phi = (p.substract(new BigNumBinary("1"))).multiply(q.substract(new BigNumBinary("1")));
        BigNumBinary e = new BigNumBinary("1009");
        BigNumBinary d = new BigNumBinary(di.toString());

        /** Cipher **/
        BigNumBinary m = new BigNumBinary("999999999999999999999999999999999999999999999999999999999999999999999999999");
        Instant start = Instant.now();
        BigNumBinary cipher = m.pow(e);
        Instant finish = Instant.now();
        time_cipher+= Duration.between(start,finish).toNanos();

        Instant starti = Instant.now();
        BigNumBinary decipher = cipher.pow(d);
        Instant finishi = Instant.now();
        time_decipher+= Duration.between(starti,finishi).toNanos();

        System.out.println("---- RSA ----");
        System.out.println("Msg = "+ m.getStrValue());
        System.out.println("Cipher = "+ cipher.getStrValue());
        System.out.println("Decipher = " + decipher.getStrValue());
    }
}
