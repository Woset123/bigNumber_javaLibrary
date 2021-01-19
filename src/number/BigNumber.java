package number;
import java.util.ArrayList;
import java.util.function.BiFunction;

/**
 * @author Eric
 * @version 1.0
 */

public class BigNumber {


    /** Size of the input **/
    int nbDigits;

    /**
     * Maximal Binary Size (radix 10)
     * nbBits = nbDigits * log(10) / log(2)
     */
    int nbBits;

    /** Number of 32 bits words possible given the nbBits **/
    int nbWords;

    /** Value of the Big Number **/
    int[] value;

    /** Primitive used to store the final value into a String **/
    String strValue="";

    /** Size of a Word **/
    final int WORD_SIZE = 32;

    /** Number of digits (for radix equals to 10)
     * that can fit into a Java Integer without "going negative", i.e., the
     * highest integer n such that 10**n < 2**31 = 2 147 483 648
     */
    final int DIGITS_PER_INT = 9;

    /** Value used for arithmetic operations **/
    final long SUPER_RADIX = 1000000000;


    /****************************************************************************/


    /**
     * Big Number default Constructor
     */
    public BigNumber()
    {
        this.value = ONE.getValue();
        this.strValue = toString(this.value);
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
        this.nbWords = (int) (Math.ceil(((float) (this.nbBits) / (float) (WORD_SIZE)))+100);

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
     * Big Number Constructor
     * Array of 32 bits Integer given in input
     * Radix 10 !!
     * @param value
     */
    public BigNumber(int[] value) {

        this.value = value;
        this.strValue = toString(this.value);

        /** Recreate the stack to be sure there are 9 digits stacks **/
        createStack(this.strValue);
        this.nbWords = this.value.length;
    }

    /**
     * Safe Big Number Constructor
     * Does not check if stacks are correct
     * Array of 32 bits Integer given in input
     * Radix 10 !!
     * @param value
     * @param bool
     */
    private BigNumber(int[] value,boolean bool) {

        this.value = value;
        this.strValue = toString(this.value);
        this.nbWords = this.value.length;
    }


    /** Constant BigNumbers for Montgomery Multiplication **/
    // N
    public static final BigNumber MGY_N = new BigNumber(new int[]{156,774238246,875915835,445233811,147609873,343164059,43855105,640486188,519457028,233998002,402904757,751807605,965928975,478596347,242014672,224727067,633004297,628621362,248433746,107510884,59233724,941256971,566153218,713448788,958681155,977990828,154489255,5329847,271334645,254216377,998443414,892683976,459272966,32042624,948290751},true);


    // R so that N < R
    public static final BigNumber MGY_R = new BigNumber(new int[]{1000,000000000,000000000,000000000,000000000,000000000,000000000,000000000,000000000,000000000,000000000,000000000,000000000,000000000,000000000,000000000,000000000,000000000,000000000,000000000,000000000,000000000,000000000,000000000,000000000,000000000,000000000,000000000,000000000,000000000,000000000,000000000,000000000,000000000,000000000},true);

    // R mod N
    public static final BigNumber MGY_RmodN = new BigNumber(new int[]{59,354570518,744504987,328597133,114340759,941015645,736869366,157082868,883257830,596011985,582571453,489154364,204426147,128421916,547911966,651637594,201974214,228271826,509397523,354934695,644597650,352458170,603080687,719307266,247913064,132055031,73064469,968020916,371992128,474701732,9339510,643896141,244362203,807744250,310255494},true);

    // R² mod N
    public static final BigNumber MGY_R2modN = new BigNumber(new int[]{92,798925215,233083812,673353035,55163940,314047691,642629923,513530301,947487382,655125673,44188687,546889317,664932548,994412820,492643745,381141618,263739211,910740182,544323134,862426706,103941302,404142957,103082814,349023973,51723872,383480128,214438229,607259635,988317976,363986289,766043178,811423125,396681631,497769849,35074342},true);


    // V = - N^-1 mod R
    public static final BigNumber MGY_V = new BigNumber(new int[]{147,23683612,895283756,207020407,708841813,901693073,581443767,279168651,495753676,708816823,421540743,275385867,627569778,881477607,143849212,681550052,866268668,936159852,722149471,612780495,424587630,129672095,835032476,977598301,540446435,595609594,990402834,732036243,59778302,592270889,714616348,891031730,521372416,880983684,615853249},true);

    // ONE
    public static final BigNumber ONE = new BigNumber(new int[]{1});

    // ZERO
    public static final BigNumber ZERO = new BigNumber(new int[]{0});


    /**
     * Sum of this with bigNumber
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
     * Modular Addition of two Big Numbers mod mod
     * Create a new Big Number whose value is
     * equal to the modular sum of the two Big Numbers'
     * value stored into the Array of 32 bits Integer
     * @param bigNumber
     * @param mod
     * @return
     */
    public BigNumber modularAddition(BigNumber bigNumber, BigNumber mod) {return new BigNumber(modularAdd(this.value, bigNumber.getValue(), mod.getValue()));}

    /**
     * Substract of this with bigNumber
     * Create a new Big Number whose value is
     * equal to the substract of the two Big Numbers' value
     * stored into the Array of 32 bits Integer
     * @param bigNumber
     * @return
     */
    public BigNumber substract(BigNumber bigNumber) { return new BigNumber(substract(this.value, bigNumber.getValue())); }


    /**
     * Modular Substract of this with bigNumber mod mod
     * Create a new Big Number whose value is
     * equal to the modular substract of the two Big Numbers'
     * value stored into the Array of 32 bits Integer
     * @param bigNumber
     * @param mod
     * @return
     */
    public BigNumber modularSubstract(BigNumber bigNumber, BigNumber mod) {return new BigNumber(modularSub(this.value, bigNumber.getValue(), mod.getValue()));}

    /**
     * Product of this with bigNumber
     * Create a new Big Number whose value is equal
     * to the multiplication of the two Big Numbers'
     * value stored into the Array of 32 bits Integer
     * @param bigNumber
     * @return
     */
    public BigNumber mul(BigNumber bigNumber) { return new BigNumber(multiply(this.value, bigNumber.getValue())); }


    /**
     * Modular Multiplication of this with bigNumber mod MGY_N
     * @param bigNumber
     * @return
     */
    public BigNumber mulMontgomery(BigNumber bigNumber) { return this.multiMontgomery(bigNumber);};

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

        /** If equals to 0 **/
        if (isZero(x) && isZero(y)) {
            return ZERO.getValue();
        }
        if (compareValue(x,ZERO.getValue())==0) {
            return y;
        }
        if (compareValue(y, ZERO.getValue())==0) {
            return x;
        }

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
     * Do modular addition between two Big Numbers
     * given a Big Number value as Mod
     * @param x
     * @param y
     * @param mod
     * @return Modular sum < mod
     */
    public int[] modularAdd(int[] x, int[] y, int[] mod) {

        /** If equals to 0 **/
        if (compareValue(x, ZERO.getValue())==0 && compareValue(y,ZERO.getValue())==0) {
            return ZERO.getValue();
        }

        BigNumber bg = new BigNumber(add(x,y));

        /** Sum equals to mod **/
        if (bg.compareValue(mod)==0) {
            return ZERO.getValue();
        }

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

        return bg.getValue();
    }


    /**
     * Substract of two Arrays of 32 bits Integer
     * @param big
     * @param small
     * @return substracted value into Arrays of 32 bits Integer
     */
    public int[] substract(int[] big, int[] small) {

        /** If equals to 0 **/
        if (isZero(big) && isZero(small)) {
            return ZERO.getValue();
        }
        if (compareValue(big,ZERO.getValue())==0) {
            return small;
        }
        if (compareValue(small, ZERO.getValue())==0) {
            return big;
        }

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
            return ZERO.getValue();
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
     * Multiplication of two 32 bits Integer Array
     * "Grade School" Algoritm
     * @param a
     * @param b
     * @return product
     */
    public int[] multiply(int[] a, int[] b) {

        /** If equals to 0 **/
        if (isZero(a) || isZero(b)) {
            return ZERO.getValue();
        }

        /** Lengths **/
        int len1 = a.length;
        int len2 = b.length;

        ArrayList<Integer> result = new ArrayList<>();

        /** Intermediate result array **/
        long interResult[] = new long[len1+len2];

        int index1 = 0;
        int index2 = 0;

        for (int i =len1-1; i>=0;i--){
            long carry = 0;
            index2 = 0;

            for (int j=len2-1; j>=0; j--){

                long product = (long) a[i] * b[j] + interResult[index1+index2] + carry;
                carry = product / SUPER_RADIX;

                interResult[index1+index2] = product % SUPER_RADIX;
                index2++;
            }
            if (carry>0) {
                interResult[index1+index2] += carry;
            }
            index1++;
        }
        int index = interResult.length-1;
        while(index >=0 && interResult[index]==0) {
            index--;
        }
        if (index == -1) {
            return new int[0];
        }

        while(index >=0) {
            result.add((int) interResult[index--]);
        }
        int[] temp = result.stream().mapToInt(Integer::intValue).toArray();
        return result.stream().mapToInt(Integer::intValue).toArray();
    }


    /**
     * Return this mod mod
     * In the case where mod = 10^k !!
     * This condition is not checked !
     * @param mod
     * @return
     */
    public BigNumber mod10(BigNumber mod) {

        /** If equals mod **/
        if (this.compareTo(mod)==0) {
            return ZERO;
        }
        if (this.compareTo(mod)==-1) {
            return this;
        }

        /** Find k **/
        int k = (mod.getValue().length-1)*9 + String.valueOf(mod.getValue()[0]).length() - 1;
        int len = this.getValue().length;

        int threshold = (int) (Math.ceil((float) k/9));
        StringBuilder sb = new StringBuilder();

        /** Nb of digits to keep for the higher word **/
        int nb = k - (threshold-1)*9;
        StringBuilder str = new StringBuilder(String.valueOf(this.getValue()[len-threshold]));
        str.delete(0,str.length()-nb);
        sb.append(str);

        /** Add the digits from the stacks remaining **/
        for (int i=len-threshold+1;i<len;i++){
            sb.append(this.stackToString(i));
        }
    return new BigNumber(sb.toString());
    }

    /**
     * Do this divided by 10^k
     * @param div
     * @return
     */
    public BigNumber div10(BigNumber div) {

        /** Find k **/
        int k = (div.getValue().length-1)*9 + String.valueOf(div.getValue()[0]).length() - 1;
        int len = this.getValue().length;

        int threshold = (int) (Math.ceil((float) k/9));
        StringBuilder sb = new StringBuilder();

        /** Add the digits from the stacks ahead **/
        for (int i=0;i<len - threshold;i++){
            sb.append(this.stackToString(i));
        }

        /** Add the nb digits from the stack limit **/
        /** Nb of digits to unkeep for the word **/
        int nb = k - (threshold-1)*9;
        StringBuilder str = new StringBuilder(String.valueOf(stackToString(len-threshold)));
        str.delete(str.length()- nb,str.length());
        sb.append(str);

        return new BigNumber(sb.toString());
    }

    /**
     * Montgomery Multiplication of this with b
     * Used for performance enhancement
     * Algorithm :
     *
     * Let c = a.b mod N with a,b,N 1024 bits, and C its Montgomery representation
     * Pick R = 10^k with 10^(k-1) <= N <= 10^k
     * Let V = - N^(-1) mod R
     *
     * Let A and B be the Montgomery representations of a and b as this :
     * A = a.R mod N = A montgomeryOperator R² mod N
     * B = b.R mod N = B montgomeryOperator R² mod N
     *
     * C = A montgomeryOperator B
     * Finally,
     * c = a.b mod N = C montgomeryOperator 1
     *
     * /!\ N,R,V and R² mod have a precalculated value !!
     * @param b
     * @return Montgomery Multiplication value into Arrays of 32 bits Integer
     */
    public BigNumber multiMontgomery(BigNumber b) {

        /** this and B Montgomery Representations **/

        // A = a.R mod N = a montgomeryOperator r² mod n (with r² mod N already precalculated)
        BigNumber A = this.montgomeryOperator(MGY_R2modN, MGY_N, MGY_R, MGY_V);
        // B = b.R mod N = b montgomeryOperator r² mod n (with r² mod N already precalculated)
        BigNumber B = b.montgomeryOperator(MGY_R2modN, MGY_N, MGY_R, MGY_V);

        // Let c = a*b mod N and C = phi(c) its Montgomery representation then
        // phi (a*b mod N) = phi(c) = C = phi(a) montgomeryOperator phi(b)
        BigNumber C = A.montgomeryOperator(B,MGY_N, MGY_R, MGY_V);

        // phi(c) montgomeryOperator 1 = phi(c)*r^-1 mod N = c*r*r^-1 mod N = c
        BigNumber c = C.montgomeryOperator(ONE,MGY_N, MGY_R, MGY_V);

        return c;
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
        // t = (s * v) mod r  : given r = 10^k we take the k first digits from right to left in this representation
        BigNumber t = (s.mul(V)).mod10(R);
        // m = (s + t * n)
        BigNumber temp = t.mul(N);
        BigNumber m = (s.add(t.mul(N)));
        // u = m/r
        BigNumber u = m.div10(R);
        // if u>=n : return u-n | else : return u
        return (compareValue(u.getValue(),N.getValue())>=0) ? u.substract(N) : u ;
    }


    /**
     * Return this^k
     * Use the algorithm Square And Multiply combined with Montgomery's reduction
     * @param k
     * @return
     */
    public BigNumber pow(BigNumber k) {

        // A = phi(this)
        BigNumber A = this.montgomeryOperator(MGY_R2modN, MGY_N, MGY_R, MGY_V);

        // P = A ^ montgomeryOperator k
        BigNumber P = A.squareAndMultiply(k);

        //a^k = P.r^-1 mod N = P montgomeryOperator 1
        BigNumber res = P.montgomeryOperator(ONE,MGY_N, MGY_R, MGY_V);

        return res;
    }


    /**
     * Return this^k
     * @param k
     * @return
     */
    public BigNumber squareAndMultiply(BigNumber k) {


            /** Init **/
            BigNumber P = this;
            BigNumber i = ONE;

            while (i.compareTo(k)==-1) {
                // P montgomeryOperator P = P.P.R^-1 mod N
                P = P.montgomeryOperator(P,MGY_N, MGY_R, MGY_V);
                // P = A² for the first loop and so on
                // P montgomeryOperator R = (P.P.R^-1 mod N) * R² . R^-1 mod N = P² mod N
                P = P.montgomeryOperator(MGY_R2modN, MGY_N, MGY_R, MGY_V);
                i = i.add(ONE);
            }
            return P;
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
     * Return the string of the stack with the index i of this
     * Add the leading zeros !
     * @param index
     * @return
     */
    public String stackToString(int index) {
        String str = "";

        str = String.valueOf(this.getValue()[index]);
        if (str.length() < 9) {
            str = makeZeros(DIGITS_PER_INT - str.length()) + str;
        }
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


    /** Getters **/

    public int[] getValue() {return value;}
    public String getStrValue() {return strValue;}
    public int getNbBits() {return nbBits;}

}
