package number;


import java.math.BigInteger;

public class BigNumber {


    /** Size of the input **/
    int nbDigits;

    /**
     * Maximal Binary Size (radix 10)
     * nbBits = nbDigits * log(10) / log(2)
     */
    int nbBits;

    /** Number of 32 bits words possible
     * given the nbBits
     */
    int nbWords;

    /**
     * Value of the Big Number
     */
    int[] value;

    /**
     * Primitive used to store the final value into a String
     */
    String strValue="";

    /**
     * Size of a Word
     */
    final int WORD_SIZE = 32;

    /** Number of digits (for radix equals to 10)
     * that can fit into a Java Integer without "going negative", i.e., the
     * highest integer n such that 10**n < 2**31 = 2 147 483 648
     */
    final int DIGITS_PER_INT = 9;

    /**
     * Value used for arithmetic operations
     */
    final long SUPER_RADIX = 1000000000;

    /****************************************************************************/

    /**
     * Pre-calculated values for 2^k
     * Used for Montgomery Multiplication
     * 0 -> null
     * 1 -> null
     * 2 -> 2¹⁰²²
     * 3 -> 2¹⁰²³
     * 4 -> 2¹⁰²⁴
     * 5 -> 2¹⁰²⁵
     * 6 -> 2¹⁰²⁶
     */
    private static int[] preCalculatedR[] = {
            null,
            null,
            {44,942328371,557897693,232629769,725618340,449424473,557664318,357520289,433168951,375240783,177119330,601884005,280028469,967848339,414697442,203604155,623211857,659868531,94441973,356216371,319075554,900311523,529863270,738021251,442209537,670585615,720368478,277635206,809290837,627671146,574559986,811484619,929076208,839082406,56034304},
            {89,884656743,115795386,465259539,451236680,898848947,115328636,715040578,866337902,750481566,354238661,203768010,560056939,935696678,829394884,407208311,246423715,319737062,188883946,712432742,638151109,800623047,59726541,476042502,884419075,341171231,440736956,555270413,618581675,255342293,149119973,622969239,858152417,678164812,112068608},
            {179,769313486,231590772,930519078,902473361,797697894,230657273,430081157,732675805,500963132,708477322,407536021,120113879,871393357,658789768,814416622,492847430,639474124,377767893,424865485,276302219,601246094,119453082,952085005,768838150,682342462,881473913,110540827,237163350,510684586,298239947,245938479,716304835,356329624,224137216},
            {359,538626972,463181545,861038157,804946723,595395788,461314546,860162315,465351611,1926265,416954644,815072042,240227759,742786715,317579537,628833244,985694861,278948248,755535786,849730970,552604439,202492188,238906165,904170011,537676301,364684925,762947826,221081654,474326701,21369172,596479894,491876959,432609670,712659248,448274432},
            {719,77253944,926363091,722076315,609893447,190791576,922629093,720324630,930703222,3852530,833909289,630144084,480455519,485573430,635159075,257666489,971389722,557896497,511071573,699461941,105208878,404984376,477812331,808340023,75352602,729369851,525895652,442163308,948653402,42738345,192959788,983753918,865219341,425318496,896548864}
    };

    /****************************************************************************/


    /**
     * Big Number default Constructor
     */
    public BigNumber()
    {
        this.nbDigits = 0;
        this.value = new int[1];
    }

    /**
     * Big Number Constructor
     * String value given in input
     * Radix 10 !!
     * @param val
     */
    public BigNumber(String val) {

        /** If input empty **/
        if (val.length() == 0)
            throw new NumberFormatException("Zero length !!");
        createStack(val);

    }

    /**
     * Shapes the stacks based on the String given in input
     * @param val
     */
    public void createStack(String val) {
        /** Erase spaces **/
        val =val.replaceAll("\\s+", "");

        /** Number of Digits in the input given **/
        this.nbDigits = val.length();

        /** Number of bits required
         * Add one so that the space allocated will never be too small
         */
        this.nbBits = (int) (Math.ceil(((float) (this.nbDigits * Math.log(10) / Math.log(2))))+1);

        /** Number of 32bits words necessary **/
        this.nbWords = (int) (Math.ceil(((float) (this.nbBits) / (float) (WORD_SIZE)))+5);

        /** Words creation **/
        this.value = new int[this.nbWords];

        /** First Stack **/
        int firstStackLength = this.nbDigits % DIGITS_PER_INT;

        if (firstStackLength==0) {
            firstStackLength = DIGITS_PER_INT;
        }
        int index = 0;

        String stack = val.substring(index, index += firstStackLength);
        this.value[0] = Integer.parseInt(stack,10);

        /** Others Stacks **/
        int stackValue;
        long binRadix = (long) Math.pow(2,16);
        int i=1;
        while (index < this.nbDigits) {
            stack = val.substring(index,index += DIGITS_PER_INT);
            stackValue = Integer.parseInt(stack,10);
            value[i++] = stackValue;
        }

        /** Check if size array too big **/
        if (i<nbWords) {
            int[] smaller = new int[i];
            System.arraycopy(value, 0, smaller,0,i);
            this.value = smaller;
        }

        this.strValue = toString(this.value);
    }

    /**
     * Shapes the Array given in input
     * into a Short 16bit Array
     * Short : maxDigits = 4
     * @param val
     * @param wordsize
     */
    public short[] reshapeStackIntoShort(int[] val) {

        String str = toString(val);

        /** Number of Digits in the input given **/
        int nbDigits = str.length();

        /** Number of bits required
         * Add one so that the space allocated will never be too small
         */
        int nbBits = (int) (Math.ceil(((float) (nbDigits * Math.log(10) / Math.log(2))))+1);

        /** Number of words necessary **/
        int nbWords = (int) (Math.ceil(((float) (nbBits) / (float) (16)))+2);

        /** Words creation **/
        short[] value = new short[nbWords];

        /** First Stack **/
        int firstStackLength = nbDigits % 4;

        if (firstStackLength==0) {
            firstStackLength = 4;
        }
        int index = 0;

        String stack = str.substring(index, index += firstStackLength);
        value[0] = Short.parseShort(stack,10);

        /** Others Stacks **/
        int stackValue;
        long binRadix = (long) Math.pow(2,16);
        int i=1;
        while (index < this.nbDigits) {
            stack = str.substring(index,index += 4);
            stackValue = Short.parseShort(stack,10);
            value[i++] = (short) stackValue;
        }

        /** Check if size array too big **/
        if (i<nbWords) {
            short[] smaller = new short[i];
            System.arraycopy(value, 0, smaller,0,i);
            value = smaller;
        }

        return value ;
    }


    /**
     * Big Number Constructor
     * Array of 32 bits Integer given in input
     * Radix 10 !!
     * @param value
     */
    private BigNumber(int[] value) {

        this.value = value;
        this.strValue = toString(this.value);

        /** Recreate the stack to be sure there are 9 digits stacks **/
        createStack(this.strValue);
        this.nbWords = this.value.length;
    }


    /** Constant BigNumbers for Montgomery Multiplication **/
    //public static final BigNumber MGY_N = new BigNumber("123");
    //public static final BigNumber MGY_R = new BigNumber(pickRMontgomeryMul(MGY_N.getValue()));
    //public static final BigNumber MGY_V = new BigNumber();

    /**
     * Sum of two Big Numbers
     * Create a new Big Number whose value is
     * equal to the sum of the two Big Numbers' value
     * stored into the Array of 32 bits Integer
     * @param bigNumber
     * @return
     */
    public BigNumber add(BigNumber bigNumber) {
        return new BigNumber(add(this.value, bigNumber.getValue()));
    }


    /**
     * Modular Addition of two Big Numbers
     * Create a new Big Number whose value is
     * equal to the modular sum of the two Big Numbers'
     * value stored into the Array of 32 bits Integer
     * @param bigNumber
     * @param mod
     * @return
     */
    public BigNumber modularAddition(BigNumber bigNumber, BigNumber mod) {return new BigNumber(modularAdd(this.value, bigNumber.getValue(), mod.getValue()));}

    /**
     * Substract of two Big Numbers
     * Create a new Big Number whose value is
     * equal to the substract of the two Big Numbers' value
     * stored into the Array of 32 bits Integer
     * @param bigNumber
     * @return
     */
    public BigNumber substract(BigNumber bigNumber) { return new BigNumber(substract(this.value, bigNumber.getValue())); }


    /**
     * Modular Substract of two Big Numbers
     * Create a new Big Number whose value is
     * equal to the modular substract of the two Big Numbers'
     * value stored into the Array of 32 bits Integer
     * @param bigNumber
     * @param mod
     * @return
     */
    public BigNumber modularSubstract(BigNumber bigNumber, BigNumber mod) {return new BigNumber(modularSub(this.value, bigNumber.getValue(), mod.getValue()));}

    /**
     * Multiplication of two Big Numbers
     * Create a new Big Number whose value is equal
     * to the multiplication of the two Big Numbers'
     * value stored into the Array of 32 bits Integer
     * @param bigNumber
     * @return
     */
    public BigNumber mul(BigNumber bigNumber) { return new BigNumber(mul(this.value, bigNumber.getValue())); }


    /**
     * Montgomery Multiplication of two Big Numbers
     * @param bigNumber
     * @param N
     * @param R
     * @param V
     * @return
     */
    public BigNumber mulMontgomery(BigNumber bigNumber, BigNumber N, BigNumber R, BigNumber V ) { return new BigNumber(mulMontgomery(this.value, bigNumber.getValue(),N,R,V));}

/****************************************************************************/


    /**
     * Compare this Big Number to the one given in input
     * @param bigNumber
     * @return -1, 0 or 1 if this is less than, equal to or greater than bigNumber
     */
    public int compareTo (BigNumber bigNumber) { return this.compareValue(bigNumber.getValue());}

    /**
     * Sum of two Array of 32 bits Integer
     * Given the fact radix = 10, the max carry to
     * propagate would be 1
     * @param x
     * @param y
     * @return sum value into Array of 32 bits Integer
     */
    public int[] add(int[] x, int[] y) {

        /** Suppose that x is larger than y **/
        /** If not **/
        if (x.length < y.length) {
            int[] temp = x;
            x = y;
            y = temp;
        }

        /** Initialize some variables **/
        int xIndex = x.length;
        int yIndex = y.length;
        int res[] = new int[xIndex];
        long sum = 0;
        int remain = 0;
        long temp=0;

        while (yIndex > 0){

            sum = (long) x[--xIndex] + y[--yIndex] + (sum / SUPER_RADIX);
            res[xIndex] = (int) (sum % SUPER_RADIX);

        }

        /** Check if carry to propagate **/
        boolean carry = (sum / SUPER_RADIX !=0);
        while (carry && xIndex > 0) {
            carry = ((res[--xIndex] = (int) (x[xIndex] + 1)) == 0);
        }

        /** Fill with the remaining values of x if not reached **/
        while (xIndex > 0) {
            res[--xIndex] = x[xIndex];
        }

        /** Grow result if necessary **/
        if (carry) {
            int bigger[] = new int[res.length + 1];
            System.arraycopy(res, 0, bigger, 1, res.length);
            bigger[0] = 1;
            return bigger;
        }

        return res;
    }

    /**
     * Sum of a Big Number with a 32 bits Integer
     * @param x
     * @param y
     * @return sum value into Array of 32 bits Integer and a 32 bits Integer
     */
    private int[] add(int[] x, int y) {


        /** Initialize some variables **/
        int xIndex = x.length;
        int res[] = new int[xIndex];
        long sum = 0;

        sum = (long) x[--xIndex] + y + (sum / SUPER_RADIX);
        res[xIndex] = (int) (sum % SUPER_RADIX);


        /** Check if carry to propagate **/
        boolean carry = (sum / SUPER_RADIX !=0);
        while (carry && xIndex > 0) {
            carry = ((res[--xIndex] = (int) (x[xIndex] + 1)) == 0);
        }

        /** Fill with the remaining values of x if not reached **/
        while (xIndex > 0) {
            res[--xIndex] = x[xIndex];
        }

        /** Grow result if necessary **/
        if (carry) {
            int bigger[] = new int[res.length + 1];
            System.arraycopy(res, 0, bigger, 1, res.length);
            bigger[0] = 1;
            return bigger;
        }
        return res;
    }

    /**
     * Do modular addition between two Big Numbers
     * given a Big Number value as Mod
     * @param x
     * @param y
     * @param mod
     * @return Modular sum < mod
     */
    public int[] modularAdd(int[] x, int[] y, int[] mod) {

        BigNumber bg = new BigNumber(add(x,y));

        /** Sum greater than mod **/
        if (bg.compareValue(mod)==1) {
            while (bg.compareValue(mod)==1) {
                bg.value = substract(bg.value, mod);
            }
            /** Check if need to reduce array's size **/
            int i=0;
            int lenSmall= bg.getValue().length;
            while (bg.getValue()[i++]==0) {
                lenSmall--;
            }
            if (lenSmall!=bg.getValue().length) {
                int[] smaller = new int[lenSmall];
                System.arraycopy(bg.getValue(), i-1, smaller,0,lenSmall);
                bg.value = smaller;
            }
            return bg.getValue();

        }
        /** Sum equals to mod **/
        if (bg.compareValue(mod)==0) {
            return new int[1];
        }

        return bg.getValue();
    }


    /**
     * Substract of two Arrays of 32 bits Integer
     * @param big
     * @param small
     * @return substracted value into Arrays of 32 bits Integer
     */
    public int[] substract(int[] big, int[] small) {

        /** Suppose that x is bigger than y **/
        /** If not **/
        if (compareValue(big,small)==-1) {
            int[] temp = big;
            big = small;
            small = temp;
        }

        int bigIndex = big.length;
        int smallIndex = small.length;
        int res[] = new int[bigIndex];
        long sub = 0;
        long borrow = 0;

        /** Substract common parts **/
        while (smallIndex > 0) {
            sub = big[--bigIndex] - small[--smallIndex] - borrow;
            borrow = 0;
            res[bigIndex] = (int) sub;
            if (res[bigIndex] < 0) {
                borrow = 1;
                res[bigIndex] = (int) (res[bigIndex] + SUPER_RADIX);
            }
        }

        /** Copy remainder of longer number **/
        while (bigIndex > 0)
            res[--bigIndex] = (int) (big[bigIndex] - borrow);
            borrow = 0;

        /** Check if need to reduce array's size **/
        if (trueSize(res)<res.length) {
            int[] smaller = new int[trueSize(res)];
            int tp = res.length-trueSize(res);
            System.arraycopy(res, tp, smaller,0,trueSize(res));
            return smaller;
        }


        return res;
    }

    /**
     * Do modular substract between two Big Numbers
     * given a Big Number value as Mod
     * @param x
     * @param y
     * @param mod
     * @return Modular substract < mod
     */
    public int[] modularSub(int[] x, int[] y, int[] mod) {

        BigNumber bg = new BigNumber(substract(x,y));
        return mod(bg.getValue(), mod);
    }


    /** Not very optimized...
     * Implements mod function
     * @param a
     * @param mod
     * @return a mod n
     */
    public int[] mod(int[] a, int[] mod) {

        /** Sum equals to mod **/
        if (compareValue(a, mod)==0) {
            return substract(a,mod);
        }

        /** Sum greater than mod **/
        while (compareValue(a,mod)==1) {
            a = substract(a, mod);
        }
        /** Check if need to reduce array's size **/
        int i=0;
        int lenSmall= a.length;
        while (a[i++]==0) {
            lenSmall--;
        }
        if (lenSmall!=a.length) {
            int[] smaller = new int[lenSmall];
            System.arraycopy(a, i-1, smaller,0,lenSmall);
            a = smaller;
        }
        return a;

    }


    /**
     * Multiplication of two Arrays of 32 bits Integer
     * @param x
     * @param y
     * @return product value into Arrays of 32 bits Integer
     */
    public int[] mul(int[] x, int[] y) {

        /** Suppose that x is larger than y **/
        /** If not **/
        if (x.length < y.length) {
            int[] temp = x;
            x = y;
            y = temp;
        }

        int[] res = new int[x.length + y.length];
        int xCursor = x.length-1;
        int yCursor = y.length-1;
        long product = 0;


        for (int i = xCursor; i > -1; i--) {

            int[] low = new int[x.length+y.length];

            for (int j = yCursor; j > -1; j--) {

                int[] high = new int[x.length+y.length];
                product = (long) x[i] * y[j];
                if (product > 99999999) {
                    high[i+j] = (int) ((long) product / SUPER_RADIX);
                    high[i+j+1] = (int) ((long) product % SUPER_RADIX);
                }
                else {
                    high[i+j]=(int) product;
                }
                low = add(low,high);
            }
            res = add(res,low);
        }
        return res;
    }

    /** (In progress)
     * Montgomery Multiplication of two Arrays of 32 bits Integer
     * Used for performance enhancement
     * Algorithm :
     *
     * Let c = a.b mod N with a,b,N 1024 bits, and C its Montgomery representation
     * Pick R = 2^k with 2^(k-1) <= N <= 2^k
     * Let V = - N^(-1) mod R
     *
     * Let A and B be the Montgomery representations of a and b as this :
     * A = a.R mod N
     * B = b.R mod N
     *
     * C = A.B.R^(-1) mod N = T.R^(-1) mod N = (T + T.V.N)/R mod N
     *
     * Finally,
     * c = C.R^(-1) mod N = (T + T.V.N)/R mod N = a.b mod N
     *
     * /!\ N has a default value !!
     * @param a
     * @param b
     * @return Montgomery Multiplication value into Arrays of 32 bits Integer
     */
    public int[] mulMontgomery(int[] a, int[] b, BigNumber N,BigNumber R, BigNumber V) {

        // A and B Montgomery Representations
        // A = a.R mod N = a montgomeryOperator r² mod n (with r² mod N already precalculated)
        int[] A = mod(mul(a, R.getValue()), N.getValue());
        // B = b.R mod N = b montgomeryOperator r² mod n (with r² mod N already precalculated)
        int[] B = mod(mul(b,R.getValue()), N.getValue());

        // s = A*B
        int[] s = mul(A,B);

        // t = (s*v) mod r
        int[] t = mod(mul(s,V.getValue()),R.getValue());

        // m = (s + t * n)
        int[] m = add(s,mul(t, N.getValue()));

        // u = m/r
        int[] u = leftShift(m,findRExpMontgomeryMul(R.getValue()));

        // if u>=n : return u-n | else : return u
        return (compareValue(u,N.getValue())>=0) ? substract(u,N.getValue()) : u ;

    }

    /**
     * Calculate this montgomeryOperator b = this.b.r^-1 mod N
     * @param b
     * @param N
     * @param R
     * @param V
     * @return
     */
    public BigNumber montgomeryOperator(BigNumber b, BigNumber N,BigNumber R, BigNumber V) {

        // s = this * b
        BigNumber s = this.mul(b);
        // t = (s * v) mod r = (s * v) >> k ????
        BigNumber t = new BigNumber(rightShift(s.mul(V).getValue(),findRExpMontgomeryMul(R.getValue())));
        // m = (s + t * n)
        BigNumber m = (s.add(t.mul(N)));
        // u = m/r
        BigNumber u = new BigNumber(leftShift(m.getValue(),findRExpMontgomeryMul(R.getValue())));
        // if u>=n : return u-n | else : return u
        return (compareValue(u.getValue(),N.getValue())>=0) ? u.substract(N) : u ;
    }

    /**
     * To pick the right value for R
     * in the Montgomery Multiplication
     * Implemented for 1024 bits values for n !
     * @param n
     * @return
     */
    private static int[] pickRMontgomeryMul(int[] n) {
        int i = 2;
        while (compareValue(preCalculatedR[i],n)==-1 && i<7) {
            i++;
        }
        if (compareValue(preCalculatedR[i],n)==-1) {
            throw new ArithmeticException("Mod n might not be a 1024 bits number !!");
        }
        return preCalculatedR[i];
    }

    /**
     * To pick the right exposant for R
     * in the Montgomery Multiplication
     * Implemented for 1024 bits values for n !
     * @param n
     * @return
     */
    private static int findRExpMontgomeryMul(int[] n) {
        int i = 2;
        while (compareValue(preCalculatedR[i],n)==-1 && i<7) {
            i++;
        }
        if (compareValue(preCalculatedR[i],n)==-1) {
            throw new ArithmeticException("Mod n might not be a 1024 bits number !!");
        }
        return i+1020;
    }

    /**
     * Returns GCD of this and bg
     * @param bg
     * @return
     */
    public BigNumber gcd(BigNumber bg) {return new BigNumber(gcd(this.value,bg.value));}

    /**
     * Returns GCD of a and b
     * Use Euclid's Algorithm until the numbers are the same length
     * and use binary GCD Algorithm to finc GCD
     * @param a
     * @param b
     * @return
     */
    public int[] gcd(int[] a, int[] b) {return binGCD(a, b);}


    /**
     * Calculate GCD of a and b
     * Algorithm B from "Art of Computer Programming Knuth vol.2" section 4.5.2
     *
     * B1. [Find Power of 2] Set k <- 0, and then repeatedly set k <- k+1, u <- u/2, v <- v/2
     * zero or more times until u and v are not both even.
     *
     * B2. [Initialize] Now the original values of u and v have been divided by 2^k and at
     * least one of their present value is odd. If u is odd, set t <- -v and go to B4.
     * Otherwise set t <- u
     *
     * B3. [Halve t] At this point, t is even and nonzero. Set t <- t/2
     *
     * B4. [Is t even ?] If t is even, go back to B3
     *
     * B5. [Reset max(u,v)]. If t>0, set u <- t; otherwise set v <- -t
     *
     * B6. [Substract] Set t <- u - v. If t!=0, go back to B3. Otherwise the algorithm
     * terminates with u*2^k as the output.
     * @param val
     * @return
     */
    public int[] binGCD(int[] u, int[] v) {
        int tSign = 0;
        int[] maxIntValue = {4,294967295};
        int[] t = new int[Math.max(u.length, v.length)];
        int[] res = new int[1];

        /** Step 1 **/
        int ulbIndex = getLowestBitSet(u);
        int vlbIndex = getLowestBitSet(v);
        int k = (ulbIndex < vlbIndex) ? ulbIndex : vlbIndex;
        if (k!=0) {
            u = rightShift(u,k);
            v = rightShift(v,k);
        }


        /** Step 2 **/
        // if lowestBit Set is at the index 0 -> odd
        boolean uOdd = isOdd(u);
        t = uOdd ? v : u;
        tSign = uOdd ? -1: 1;


        int lbIndex;
        while ((lbIndex = getLowestBitSet(t))>=0) {
            /** Steps 3 & 4 **/
            t = rightShift(t,lbIndex);

            /** Step 5 **/
            if (tSign == 1) {
                u = t;
            } else {
                v = t;
            }

            // Special case value can fit into an Integer
            if (compareValue(u,maxIntValue) < 1 && compareValue(v,maxIntValue) < 1) {
                // Affect the array value into one long
                long x = fitIntoLong(u);
                long y = fitIntoLong(v);
                x  = gcd(x, y);
                res[0] = (int) x;
                if (k > 0)
                    res = leftShift(res,k);
                return res;
            }

            /** Step 6 **/
            if (compareValue(u,v)==0) {break;};
            if (compareValue(u,v)==-1) {
                tSign=-1;
                v = substract(u,v);
                t = v;
            }
            else {
                tSign=1;
                u = substract(u,v);
                t = u;
                }

        }
        if (k >0) {
            //u*2**k
            u = leftShift(u,k);
        }
        return u;
    }

    /**
     * Calculate GCD of integers a and b
     * @param a
     * @param b
     * @return
     */
    public int gcd(long a, long b) {
        if (b!=0) {
            return gcd(b, a%b);
        }
        else {
            return (int) a;
        }
    }

    /**
     * Suppose a < MAX_INTEGER_VALUE
     * @param a
     * @return
     */
    public long fitIntoLong(int[] a) {
        if (a.length==1) {
            return (long) (a[0]);
        }
        else {
            return (long) (a[0]*Math.pow(10,9) + a[1]);
        }

    }

    /**
     * Returns val rightshifted by k
     * @param val
     * @return
     */
    public int[] rightShift(int[] val, int k) {
        int[] res = new int[val.length];
        int[] carry = new int[val.length];

        if(k!=0) {
            if (trueSize(val)==1){
                for (int i=0;i<val.length;i++) {
                    if (val[i]!=0) {res[i] += val[i]>>k;}
                    else {res[i] += 0;}
                }
            }
            else {
                for (int i=0;i<val.length;i++) {
                    int index = Integer.numberOfTrailingZeros(val[i]);
                    if ((index > k) || (i==val.length-1)) {
                        res[i] += val[i]>>k;
                    }
                    else {
                        res[i] += val[i]>>k;
                        //Get the decimal part
                        double t = (double) ((val[i] / Math.pow(2,k)));
                        String str = String.format("%f",t);
                        str = removeZeros(str);
                        String[] te = str.split("\\.");
                        t = Integer.parseInt(te[1]);
                        int tp = (int) ((t*Math.pow(10,9-te[1].length())));
                        carry[i+1] = tp;
                    }
                }
                /** Apply the carries **/
                res = add(res,carry);
            }

            /** Check if need to reduce array's size **/
            if (trueSize(res)<res.length) {
                int[] smaller = new int[trueSize(res)];
                int tp = res.length-trueSize(res);
                System.arraycopy(res, tp, smaller,0,trueSize(res));
                return smaller;
            }

            return res;
        }
        else {
            return val;
        }

    }

    /**
     * Returns val left shifted by k
     * @param val
     * @return
     */
    public int[] leftShift(int[] val, int k) {
        int[] res = val;
        for (int i = 0; i < k; i++) {
            res = unitaryLeftShift(res);
        }
    return res;
    }

    /**
     * Returns val left shifted by 1
     * Fragmented so that the long will not be overflowed !!
     * @param val
     * @return
     */
    public int[] unitaryLeftShift(int[] val) {

        int[] res = new int[val.length];
        long temp = 0;
        for (int i=val.length-1;i>-1;i--) {
            temp = val[i]<<1;
            /** More than 9 digits **/
            if (Long.toString(temp).length()>9) {
                /** Need for bigger array **/
                if(i==0) {
                    int bigger[] = new int[res.length + 1];
                    System.arraycopy(res, 0, bigger, 1, res.length);
                    bigger[0] += (int) (temp / SUPER_RADIX);
                    bigger[1] += (int) (temp % SUPER_RADIX);
                    return new BigNumber(bigger).value;
                }
                else {
                    res[i-1] += (int) (temp / SUPER_RADIX);
                    res[i] += (int) (temp % SUPER_RADIX);
                }
            }
            else {
                res[i] += (int) temp;
            }
        }
        return res;
    }

    /**
     * Return the index of the lowest bit set in val
     * If val = 0 , -1 is returned
     * @param val
     * @return
     */
    public int getLowestBitSet(int[] val) {

        if (isZero(val)) {return -1;}
        int j,b;
        int tp = trueSize(val);
        for (j=trueSize(val)-1;(j>0) && (val[j]==0);j--);
        b = val[j];
        if (b==0) {
            return -1;
        }
        // numberOfTrailingZeros : Number of '0' at the right of the lowest bit set for b
        // <<5 because 32 bits per tile of the array !
        return ((trueSize(val)-1-j)<<5) + Integer.numberOfTrailingZeros(b);
    }

    /**
     * Return non empty size of the Array
     * @param a
     * @return
     */
    public int trueSize(int[] a) {
        int size = a.length;
        int i=0;
        while (a[i]==0) {
            i++;
            size--;
        }
        return size;
    }

    /**
     * Remove right side zeros
     * @param str
     * @return
     */
    public String removeZeros(String str) {
        StringBuilder sb = new StringBuilder(str);
        int i = sb.length()-1;
        int compt = 0;
        while (sb.charAt(i--)=='0' && i>0) {compt++;}
        if(sb.charAt(i+1)=='.') {
            return str;
        }
        else {
            sb.delete(sb.length() - compt,sb.length());
            return sb.toString();
        }

    }


    /**
     * Returns True if a is Odd
     * Looks at the last array's case and convert the Integer
     * stored into its Binary representation to check if the bit
     * relative to 2⁰ is set or not.
     * @param a
     * @return
     */
    public boolean isOdd(int[] a) {
        String bin = Integer.toBinaryString(a[a.length-1]);
        if (bin.charAt(bin.length()-1)=='1') { return true;}
        return false;
    }


    /**
     * Returns True is the Integer Array given is equals to zero, and False is not
     * @param val
     * @return
     */
    public boolean isZero(int[] val) {
        int len = val.length;
        for (int i=0;i<len;i++) {
            if (val[i]!=0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compare value array of this Big Number with
     * the 32 bits Integer Array y
     * @param y
     * @return -1, 0 or 1 if this is less than,
     * equal to or greater than y array
     */
    public int compareValue(int[] y) {

        int lenThis = this.value.length;
        int yLen = y.length;

        if (lenThis > yLen) {
            return 1;
        }
        if (lenThis < yLen) {
            return -1;
        }
        for (int i = 0; i < lenThis; i++) {
            int valThis = this.value[i];
            int yValue = y[i];

            if (valThis != yValue) {
                if (valThis < yValue) {
                    return -1;
                }
                else {
                    return 1;
                }
            }
        }
        return 0;
    }

    /**
     * Compare value of two 32 bits Integer Arrays
     * @param x
     * @param y
     * @return -1, 0 or 1 if x is less than,
     * equal to or greater than y array
     */
    public static int compareValue(int[] x, int[] y) {

        int lenThis = x.length;
        int yLen = y.length;

        if (lenThis > yLen) {
            return 1;
        }
        if (lenThis < yLen) {
            return -1;
        }
        for (int i = 0; i < lenThis; i++) {
            int valThis = x[i];
            int yValue = y[i];

            if (valThis != yValue) {
                if (valThis < yValue) {
                    return -1;
                }
                else {
                    return 1;
                }
            }
        }
        return 0;
    }


    /**
     * Return a concatenate String of the value based
     * on the Array of 32 bits Integer given in input
     * @param res
     * @return String value
     */
    public String toString(int[] res) {

        String strTemp = "";
        String str = "";
        long remainder = 0;
        long currentStack = 0;


        for (int i=res.length-1; i > 0; i--) {
            currentStack = (int) (res[i]) + remainder;
            if (currentStack > SUPER_RADIX) {
                remainder = currentStack/SUPER_RADIX;
            }
            strTemp = String.valueOf(currentStack%SUPER_RADIX);

            if (strTemp.length() < 9) {
                strTemp = makeZeros(DIGITS_PER_INT - strTemp.length()) + strTemp;
            }
            str = strTemp + " " + str;
        }
        str = (String.valueOf(res[0] + remainder)) + " " + str;
        return str;
    }


    /**
     * Create String "0" whose length is
     * equals to the int given in input
     * @param size
     * @return String of size * "0"
     */
    public String makeZeros(int size) {
        String str ="";
        for (int i = 0; i < size; i++) {
            str = str + "0";
        }
        return str;
    }

    /**
     * Estimation of the number of digits of a multiplication
     * @param x
     * @param y
     * @return
     */
    public int numberDigitsMultiplication(int x, int y) {
        return (int) Math.floor(Math.log10(x) + Math.log10(y)) +1;
    }


    /**
     * Returns a new BigNumber made of the n lower int of the "this"
     * Used for Karatsuba Multiplication
     * @param n
     * @return
     */
    public BigNumber getLower(int n) {
        int len = value.length;

        if (len < n) {
            return this;

        }
        int lowerInts[] = new int[n];
        System.arraycopy(value, len-n, lowerInts,0,n);

        return new BigNumber(lowerInts);
    }

    /**
     * Returns a new BigNumber made of the n upper int of the "this"
     * Used for Karatsuba Multiplication
     * @param n
     * @return
     */
    public BigNumber getUpper(int n) {
        int len = value.length;

        if (len < n) {
            return this;
        }
        int upperInts[] = new int[n];
        System.arraycopy(value,0,upperInts,0, len-n);

        return new BigNumber(upperInts);

    }


    /** Getters **/

    public int getNbDigits() {
        return nbDigits;
    }

    public int getNbBits() {
        return nbBits;
    }

    public int getNbWords() {
        return nbWords;
    }

    public int[] getValue() {
        return value;
    }

    public String getStrValue() {
        return strValue;
    }
}
