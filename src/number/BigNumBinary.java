package number;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/**
 * Big Numbers implementation in base 2^31 as part of ISMIN courses
 * @author Eric
 * @version 1.0
 */

public class BigNumBinary {

    /** Mask used to keep 31 bits **/
    private static final Long MASK = 0x7FFFFFFFl;
    /** Used to obtain the value of an int as if it were unsigned **/
    private static final Long LONG_MASK = 0xffffffffL;
    /** Number of 9 digits words possible **/
    int nbWords;
    /** Value of the Big Number **/
    ArrayList<Integer> value;
    /** Number of digits (for radix equals to 10)
     * that can fit into a Java Integer without "going negative", i.e., the
     * highest integer n such that 10**n < 2**31 = 2 147 483 648
     */
    final int DIGITS_PER_INT = 9;
    /** Word size **/
    final int WORD_SIZE = 31 ;
    /** Value used for arithmetic operations **/
    final long SUPER_RADIX = 1000000000;

/*******************************************************************************************************/


    /**
     * BigNumBinary Constructor
     * String value given in input
     * Radix 10 !!
     * @param val - string input of the number (radix 10)
     * @throws NumberFormatException - when no input given !
     */
    public BigNumBinary(String val) {

        /** If input empty **/
        if (val.length() == 0)
            throw new NumberFormatException("Zero length !!");
        /** If Zero **/
        if (val.length()==1 && Integer.parseInt(val)==0){
            ArrayList<Integer> zero = new ArrayList<>();
            zero.add(0);
            this.value = zero;
        }
        else {
            /** Array 9 digits stack **/
            createStack(val);
            /** Array of 31 bits **/
            this.value = toBinary(this.value);
        }

    }

    /**
     * BigNumBinary constructor
     * @param val - ArrayList<Integer>
     */
    private BigNumBinary(ArrayList<Integer> val) {
        this.value = val;
    }

    /**
     * BigNumBinary to construct precalculated BigNumBinaries
     * @param val - array radix 2^31 !!
     */
    private BigNumBinary(int[] val){
        this.value = new ArrayList<>();
    /** Fill the ArrayList **/
        for (int i=0;i<val.length;i++) {this.value.add(val[i]);}
    }

    /**
     * Shapes the stacks based on the String given in input
     * @param val - string input of the number
     */
    public void createStack(String val) {
        /** Erase spaces **/
        val =val.replaceAll("\\s+", "");
        /** Number of 9 digits words necessary **/
        this.nbWords = (int) (Math.ceil(((float) (val.length()) / (float) (DIGITS_PER_INT))));
        /** Words creation **/
        this.value = new ArrayList<>();
        /** First Stack **/
        int firstStackLength = val.length() % DIGITS_PER_INT;
        if (firstStackLength==0) {
            firstStackLength = DIGITS_PER_INT;
        }
        int index = 0;
        String stack = val.substring(index, index += firstStackLength);
        this.value.add(Integer.parseInt(stack,10));
        /** Others Stacks **/
        int stackValue;
        while (index < val.length()) {
            stack = val.substring(index,index += DIGITS_PER_INT);
            stackValue = Integer.parseInt(stack,10);
            this.value.add(stackValue);
        }
    }

    /** The BigNumBinary constant Zero **/
    public static final BigNumBinary ZERO = new BigNumBinary(new ArrayList<Integer>(Collections.nCopies(1,0)));
    /** The BigNumBinary constant One **/
    public static final BigNumBinary ONE = new BigNumBinary(new ArrayList<Integer>(Collections.nCopies(1,1)));
    /** Constant BigNumBinary N for Montgomery Multiplication **/
    public static final BigNumBinary MGY_N = new BigNumBinary(new int[]{10003716,1491196112,414886606,1167952122,1694238255,640297731,982769063,414804193,1093981874,1286150263,895784412,1542604043,1311810001,1603887403,94801236,103605104,36533097,2061487095,1085097348,621953484,1946677379,1795652458,676751585,500149201,1092513734,1269595439,215359060,460849662,1355250570,401888270,112565291,1847600491,475971065});
    /** Constant BigNumber R = 2¹⁰²⁵ for Montgomery Multiplication **/
    public static final BigNumBinary MGY_R = new BigNumBinary(new int[]{4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
    /** Constant BigNumber R² mod N for Montgomery Multiplication **/
    public static final BigNumBinary MGY_R2modN = new BigNumBinary(new int[]{792518,1693036003,1677773903,1129898352,755697718,1451576348,844941527,1349741317,182744808,258600595,1261594476,160685081,661006668,1769241208,1129547922,507724589,1467197228,1690677089,759956681,1518700876,736745748,1436914308,468831006,805325184,986802795,994496909,954299935,2109707428,1725218273,1991487393,1455715451,1403709107,1361428340});
    // V = - N^-1 mod R
    /** Constant BigNumber V for Montgomery Multiplication **/
    public static final BigNumBinary MGY_V = new BigNumBinary(new int[]{1821272998,300153737,263891684,714753978,1784474316,2143710275,796937023,1129383527,1708583327,1831726867,2137499823,146783559,164760009,464917459,284047094,1470482310,908766833,199414702,1807019539,1946161133,1901705231,1890955702,139212725,717563492,878495964,1766297057,52221969,1751195509,708863919,338718958,528111774,255219200,1210092471});

    /** Operations **/

    /**
     * Returns a BigNumBinary whose value is this + bg
     * @param bg - a BigNumBinary
     * @return a BigNumBinary from this + bg
     */
    public BigNumBinary add(BigNumBinary bg) {

        /** Variables **/
        ArrayList<Integer> a = this.value;
        ArrayList<Integer> b = bg.value;
        ArrayList<Integer> result = new ArrayList<>();

        /** If a is shorter, swap ! **/
        if (b.size()>a.size()) {
            ArrayList<Integer> temp = a;
            a = b;
            b = temp;
        }
        int indexA = a.size();
        int indexB = b.size();
        int[] res = new int[indexA];
        long sum = 0;
        if (indexB==1) {
            sum = a.get(--indexA) + b.get(0);
            res[indexA] = (int) (sum & MASK);
        }
        else {
            /** Add common parts **/
            while (indexB>0){
                sum = a.get(--indexA) + b.get(--indexB) + (abs(sum>>31));
                res[indexA] = (int) (sum & MASK);
            }
        }
        /** Propagate carry if not null **/
        boolean carry = (sum >>31 != 0);
        while (indexA>0 && carry) {
            sum = a.get(--indexA) + 1;
            res[indexA] = (int) (sum & MASK);
            carry = (sum >>31 != 0);
        }
        /** Copy values remaining from a **/
        while (indexA>0){
            res[--indexA] = a.get(indexA);
        }
        /** Grow if necessary **/
        if (carry) {
            int bigger[] = new int[res.length + 1];
            System.arraycopy(res, 0, bigger, 1, res.length);
            bigger[0] = 1;
            /** Fill the ArrayList **/
            for (int i=0;i<bigger.length;i++) {result.add(bigger[i]);}
            return new BigNumBinary(result);

        }
        /** Fill the ArrayList **/
        for (int i=0;i<res.length;i++) {result.add(res[i]);}
        return new BigNumBinary(result);
    }

    /**
     * Returns a BigNumBinary whose value is the max(this,little) - min(this,little)
     * @param little - a BigNumBinary
     * @return if this > little return this- little, else little - this
     */
    public BigNumBinary substract(BigNumBinary little){

        /** Variables **/
        ArrayList<Integer> big = this.value;
        ArrayList<Integer> small = little.value;
        int bigIndex = big.size();
        int smallIndex = small.size();
        ArrayList<Integer> result = new ArrayList<>();
        int[] res = new int[bigIndex];

        /** If big is smaller, swap ! **/
        if (compareArrayList(big,small)==-1) {
            ArrayList<Integer> temp = big;
            big = small;
            small = temp;
        }
        long sub = 0;
        /** Substract common parts **/
        while (smallIndex > 0){
            sub = big.get(--bigIndex) - small.get(--smallIndex) + (sub>>31);
            res[bigIndex] = (int) (sub & MASK);
        }
        /** Subtract remainder of longer number while borrow propagates **/
        boolean borrow = (sub >> 31 != 0);
        while (bigIndex > 0 && borrow)
            borrow = ((res[--bigIndex] = big.get(bigIndex) - 1) == -1);

        /** Copy remainder of longer number **/
        while (bigIndex > 0)
            res[--bigIndex] = big.get(bigIndex);
        /** Check if need to reduce array's size **/
        if (trueSize(res)<res.length) {
            int[] smaller = new int[trueSize(res)];
            int tp = res.length-trueSize(res);
            System.arraycopy(res, tp, smaller,0,trueSize(res));
            /** Fill the ArrayList **/
            for (int i=0;i<smaller.length;i++) {result.add(smaller[i]);}
            return new BigNumBinary(result);
        }
        /** Fill the ArrayList **/
        for (int i=0;i<res.length;i++) {result.add(res[i]);}
        return new BigNumBinary(result);
    }

    /**
     * Return a new BigNumBinary equals to this * p
     * "Grade School" Algorithm
     * Need to finish to implement Karatsuba to optimize time execution
     * @param p - a BigNumBinary
     * @return this * p
     */
    public BigNumBinary multiply(BigNumBinary p) {

       // if (this.value.size() < 2) {

            /** Grade School Algorithm **/
            /** Lengths **/
            int len1 = this.value.size();
            int len2 = p.value.size();
            ArrayList<Integer> result = new ArrayList<>();

            /** Intermediate result array **/
            long interResult[] = new long[len1+len2];
            int indexUp = 0;
            int indexDown = 0;
            for (int i =len1-1; i>=0;i--){
                long carry = 0;
                indexDown = 0;
                for (int j=len2-1; j>=0; j--){
                    long product = (long) (this.value.get(i) & LONG_MASK) * (p.value.get(j) & LONG_MASK) + (interResult[indexUp+indexDown] & LONG_MASK) + carry;
                    carry = product>>31;
                    interResult[indexUp+indexDown] = product & MASK;
                    indexDown++;
                }
                if (carry>0) {
                    interResult[indexUp+indexDown] += carry;
                }
                indexUp++;
            }
            int index = interResult.length-1;

            while(index >=0 && interResult[index]==0) {
                index--;
            }
            if (index == -1) {
                return ZERO;
            }
            /** Add to the result array **/
            while(index >=0) {
                result.add((int) interResult[index--]);
            }
            return new BigNumBinary(result);
        }
        //else {
           // /** Karatsuba Multiplication **/
           // return this.productKaratsuba(p);
        //}
    //}

    /** (Not Operational yet)
     * Returns a BigNumBinary whose value is this * product
     * "Grade School" Algorithm
     * @param product - a BigNumBinary
     * @return this * val
     */
    public BigNumBinary productKaratsuba(BigNumBinary p) {

        int len1 = this.value.size();
        int len2 = p.value.size();

        /** Number of ints in each half of the number **/
        int half = (max(len1,len2)+1)>>1;

        /** Upper and Lower halves of this and p **/
        BigNumBinary xLow = this.getLower(half);
        BigNumBinary xUp = this.getUpper(half);
        BigNumBinary yLow = this.getLower(half);
        BigNumBinary yUp = this.getUpper(half);

        /** p1 = xUp * yUp **/
        BigNumBinary p1 = xUp.multiply(yUp);
        /** p2 = xLow * yLow **/
        BigNumBinary p2 = xLow.multiply(yLow);

        /** p3 = (xUp+xLow) * (yUp+yLow) **/
        BigNumBinary p3 = (xUp.add(xLow)).multiply((yUp.add(yLow)));

        /** res = p1 * 2^(31) + (p3 - p1 - p2) * 2^(half) + p2 **/
        BigNumBinary res = p1.shiftLeft(31).add(p3.substract(p1).substract(p2)).shiftLeft(half).add(p2);

        return res;
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
     * @param b - BigNumBinary to do the modular multiplication with
     * @return a BigNumBinary equals to this * b mod MGY_N
     */
    public BigNumBinary multiMontgomery(BigNumBinary b) {

        /** this and B Montgomery Representations **/

        // A = a.R mod N = a montgomeryOperator r² mod n (with r² mod N already precalculated)
        BigNumBinary A = this.montgomeryOperator(MGY_R2modN, MGY_N, MGY_R, MGY_V);
        // B = b.R mod N = b montgomeryOperator r² mod n (with r² mod N already precalculated)
        BigNumBinary B = b.montgomeryOperator(MGY_R2modN, MGY_N, MGY_R, MGY_V);

        // Let c = a*b mod N and C = phi(c) its Montgomery representation then
        // phi (a*b mod N) = phi(c) = C = phi(a) montgomeryOperator phi(b)
        BigNumBinary C = A.montgomeryOperator(B,MGY_N, MGY_R, MGY_V);

        // phi(c) montgomeryOperator 1 = phi(c)*r^-1 mod N = c*r*r^-1 mod N = c
        BigNumBinary c = C.montgomeryOperator(ONE,MGY_N, MGY_R, MGY_V);

        return c;
    }

    /**
     * Calculate this montgomeryOperator b = this.b.r^-1 mod N
     * @param b - BigNumBinary
     * @param N - Constant BigNumBinary precalculated
     * @param R - Constant BigNumBinary precalculated
     * @param V - Constant BigNumBinary precalculated
     * @see #multiMontgomery(BigNumBinary);
     * @return a BigNumBinary equals to this * b * R^-1 mod N
     */
    public BigNumBinary montgomeryOperator(BigNumBinary b, BigNumBinary N,BigNumBinary R, BigNumBinary V) {

        // s = this * b
        BigNumBinary s = this.multiply(b);
        // t = (s * v) mod r  : given r = 10^k we take the k first digits from right to left in this representation
        BigNumBinary t = (s.multiply(V)).mod2p1025();
        // m = (s + t * n)
        BigNumBinary m = (s.add(t.multiply(N)));
        // u = m/r
        BigNumBinary u = m.shiftRight(1025);
        // if u>=n : return u-n | else : return u
        if (compareArrayList(u.value,N.value)!=-1) {
            return u.substract(N);
        }
        else {
            return u;
        }
    }

    /**
     * Return a new BigNumBinary equals to this^k mod N
     * @param k - exponent
     * @return this^k mod N
     */
    public BigNumBinary pow(BigNumBinary k) {

        // A = phi(this)
        BigNumBinary A = this.montgomeryOperator(MGY_R2modN, MGY_N, MGY_R, MGY_V);

        // P = A ^ montgomeryOperator k
        BigNumBinary P = A.squareAndMultiply(k);

        //a^k = P.r^-1 mod N = P montgomeryOperator 1
        BigNumBinary res = P.montgomeryOperator(ONE,MGY_N, MGY_R, MGY_V);

        return res;
    }

    /**
     * Return this^k mod N
     * Note : this must have its Montgomery Representation
     * This condition is not checked !
     * @param k - BigNumBinary equals to the exponent
     * @return a BigNumBinary equals to this^k mod N
     */
    public BigNumBinary squareAndMultiply(BigNumBinary k) {

        /** Init **/
        BigNumBinary P = MGY_R.substract(MGY_N);
        long i = getNbBits(k)-1;

        while (i>=0) {
         // P montgomeryOperator P = P.P.R^-1 mod N
         P = P.montgomeryOperator(P,MGY_N, MGY_R, MGY_V);
         if (k.getBit(i)=='1') {
             P = P.montgomeryOperator(this, MGY_N, MGY_R, MGY_V);
         }
         i--;
         }
        return P;
    }

    /**
     * Get the bit at the position index
     * @param index - long
     * @return char equals to bit at index
     */
    private char getBit(long index) {
        int tile = (int) (this.value.size() - (index/31) -1);
        String str = Integer.toBinaryString(this.value.get(tile));
        String bin = makeZeros(31-str.length()) + str;
        char bit = bin.charAt(bin.length() - (int) (index%31) -1);
        return bit;
    }

    /**
     * Return number of bits
     * @param b - a BigNumBinary
     * @return number of bits of b
     */
    private long getNbBits(BigNumBinary b) {
        long len = b.value.size();
        long nb = 31*(len-1);
        /**Check higher stack**/
        String str = Integer.toBinaryString(b.value.get(0));
        nb += str.length();
        return nb;
    }


    /**
     * Return a new BigNumBinary
     * @return this mod 2^1025
     */
    public BigNumBinary mod2p1025() {

        if (compareArrayList(this.value, MGY_R.value)==-1) {return this;}

        /** Index of the last tile of the array to keep **/
        int index = (int) (this.value.size() - Math.floor(1025/31))-1;
        /** Index of the last bit to mask in the index tile **/
        int position = 1025 % 31;
        ArrayList<Integer> result = new ArrayList<>();
        /** First tile **/
        result.add((int) (this.value.get(index) & createMask(position)));
        for (int i=index+1;i<this.value.size();i++) {
            result.add(this.value.get(i));
        }
        return new BigNumBinary(result);
    }


    /**
     * Return a new BigNumBinary equals this << n
     * @param n - shift to do
     * @return this << n
     */
    public BigNumBinary shiftLeft(int n) {

        int i = 0;
        int len = this.value.size();
        int[] val = this.value.stream().mapToInt(j -> j).toArray();
        ArrayList<Integer> result = new ArrayList<>();

        if (n == 0) {
            return this;
        }
        else {
            for (int shift = 0; shift < n; shift++) {
                val = unitaryShiftLeft(val);
            }
            /** Fill the ArrayList **/
            for (int k = 0; k < val.length; k++) {result.add(val[k]);}
            return new BigNumBinary(result);
        }
    }

    /**
     * Used to fragment the shiftleft
     * @param val - int array
     * @return val << 1
     */
    private int[] unitaryShiftLeft(int[] val){
        int[] res = new int[val.length];
        int[] carry = new int[val.length];
        boolean done = true;
        boolean small = true;
        for (int k = 0; k < val.length; k++) {
            done = true;
            long temp = (val[k] & LONG_MASK) << 1;
            if ((temp & LONG_MASK) >> 31 != 0 && k==0) {
                /** Need for bigger array **/
                int bigger[] = new int[res.length + 1];
                System.arraycopy(res, 0, bigger, 1, res.length);
                bigger[0] = (int) abs((temp & LONG_MASK) >> 31);
                bigger[1] = (int) (temp & MASK);
                res = bigger;
                done = false;
                small = false;
            }
            if ((temp & LONG_MASK) >> 31 != 0 & done & small) {
                /** Propagate **/
                res[k] = (int) (temp & MASK);
                res[k-1] += (int) abs((temp & LONG_MASK) >> 31);
                done = false;
            }
            if ((temp & LONG_MASK) >> 31 != 0 & done ) {
                /** Propagate **/
                res[k+1] = (int) (temp & MASK);
                res[k] += (int) abs((temp & LONG_MASK) >> 31);
                done = false;
            }
            if(done) {
                if (!small) {
                    res[k+1] = (int) (temp & LONG_MASK);
                }
                else {
                    res[k] = (int) (temp & LONG_MASK);
                }

            }
        }
        return res;
    }

    /**
     * Return a new BigNumBinary
     * @param n - shift to do
     * @return this >> n
     */
    public BigNumBinary shiftRight(int n) {


        int len = this.value.size();
        int[] val = this.value.stream().mapToInt(j -> j).toArray();
        ArrayList<Integer> result = new ArrayList<>();

        if (n == 0) {
            return this;
        }
        else {
            for (int shift = 0; shift < n; shift++) {
                val = unitaryShiftRight(val);
            }
            /** Fill the ArrayList **/
            for (int k = 0; k < val.length; k++) {result.add(val[k]);}
            return new BigNumBinary(result);
        }
    }

    private int[] unitaryShiftRight(int[] val) {

        for (int k=val.length-1;k>-1;k--) {
            if (k==val.length-1) {
                val[k] = val[k]>>1;
            }
            else {
                if ((val[k] & 0x1) !=0) {
                    val[k+1] = val[k+1] + 0x40000000;
                }
                val[k] = val[k]>>1;
            }
        }
        /** Check if need to reduce array's size **/
        int i=0;
        int lenSmall= val.length;
        if (lenSmall==1) {return val;}
        while (val[i++]==0) {
            lenSmall--;
        }
        if (lenSmall!=val.length) {
            int[] smaller = new int[lenSmall];
            System.arraycopy(val, i-1, smaller,0,lenSmall);
            val = smaller;
        }
        return val;
    }


    /**
     * Create a mask based on the bit length to keep
     * @param shift - bitlength
     * @return a long primitive equals to the mask
     */
    private long createMask(int shift) {
        String str = "";
        for (int i=0; i<shift;i++) {str +='1';}
        long mask = Long.parseLong(str,2);
        return mask;
    }

    /**
     * Get the lowest bit set to 1
     * @param b - BigNumBinary
     * @return index of the LSB, -1 if none
     */
    public int getLowestBitSet(BigNumBinary b) {
        int k;
        for (k=b.value.size()-1;k>-1 && b.value.get(k)==0;k--) {;}
        int val = b.value.get(k);
        if (val==0) {return -1;}
        // numberOfTrailingZeros : Number of '0' at the right of the lowest bit set for b
        // <<5 because 32 bits per tile of the array !
        return ((b.value.size()-1-k)*31) + Integer.numberOfTrailingZeros(val);
    }

    /**
     * Return a new BigNumBinary representing the lower half of this
     * Used for Karatsuba
     * @param n - value of the half
     * @return a BigNumBinary corresponding to the lower half of this
     */
    public BigNumBinary getLower(int n) {
        int len = this.value.size();
        if (len <= n) {return this;}

        int[] val = this.value.stream().mapToInt(i-> i).toArray();
        int[] lowerInt = new int[n];
        System.arraycopy(val,len-n,lowerInt,0,n);
        ArrayList<Integer> res = new ArrayList<>();
        /** Fill the ArrayList **/
        for (int i=0;i<lowerInt.length;i++) {res.add(lowerInt[i]);}
        return new BigNumBinary(res);

    }

    /**
     * Return a new BigNumBinary representing the upper half of this
     * Used for Karatsuba
     * @param n - value of the half
     * @return a BigNumBinary corresponding to the upper half of this
     */
    public BigNumBinary getUpper(int n) {
        int len = this.value.size();
        if (len <= n) {return ZERO;}

        int[] val = this.value.stream().mapToInt(i-> i).toArray();
        int upperLen = len - n;
        int[] upperInt = new int[upperLen];
        System.arraycopy(val,0,upperInt,0,upperLen);
        ArrayList<Integer> res = new ArrayList<>();
        /** Fill the ArrayList **/
        for (int i=0;i<upperInt.length;i++) {res.add(upperInt[i]);}
        return new BigNumBinary(res);
    }


    /**
     * Return a concatenate String of the value based
     * on the Array of 32 bits Integer given in input
     * @return String value of the res array
     */
    private String toStringChar() {

        ArrayList<Integer> val = this.value;
        val = toRadix10(val);

        String strTemp = "";
        String str = "";
        long currentStack = 0;

        for (int i=val.size()-1; i > 0; i--) {
            currentStack = val.get(i);
            strTemp = String.valueOf(currentStack%SUPER_RADIX);

            if (strTemp.length() < 9) {
                strTemp = makeZeros(DIGITS_PER_INT - strTemp.length()) + strTemp;
            }
            str = strTemp + " " + str;
        }
        str = (val.get(0)) + " " + str;
        return str;
    }


    /**
     * Display String value in radix 10
     * @return the string value radix 10 of this
     */
    public String getStrValue() {
        return this.toStringChar();
    }


    /**
     * Create String "0" whose length is
     * equals to the int given in input
     * @param size - int
     * @return String of size * "0"
     */
    private String makeZeros(int size) {
        String str ="";
        for (int i = 0; i < size; i++) {str = str + "0";}
        return str;
    }


    /**
     * Return non empty size of the Array
     * Ex : {0,12} -> length = 2 but trueSize = 1
     * @param a - int array
     * @return number of stacks with a value
     */
    private int trueSize(ArrayList<Integer> a) {
        int size = a.size();
        if (size==1) {return 1;}
        int i=0;
        while (a.get(i)==0) {i++;size--;}
        return size;
    }

    /**
     * Compare two ArrayList<Integer>
     * @param l1
     * @param l2
     * @return -1, 0, 1 if l1 is less, equal or greater than l2
     */
    private int compareArrayList(ArrayList<Integer> l1, ArrayList<Integer> l2) {
        if (l1==l2) {return 0;}
        if (l1.size()>l2.size()){return 1;}
        if (l2.size()>l1.size()){return -1;}
        for (int i = 0; i < l1.size(); i++) {
            int l1Value = l1.get(i);
            int l2Value = l2.get(i);
            if (l1Value != l2Value) {
                if (l1Value < l2Value) {return -1;}
                else {return 1;}
            }
        }
        return 0;

    }

    /**
     * Remove right side zeros
     * @param str - String
     * @return a String without ride side zeros
     */
    private String removeZeros(String str) {
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








    /********** Only Used for Radix conversion *****************/
    /**
     * Sum of two Array of 32 bits Integer
     * Given the fact radix = 10, the max carry to
     * propagate would be 1
     * @param x - int array
     * @param y - int array
     * @return sum value into Array of 32 bits Integer
     */
    private int[] add10(int[] x, int[] y) {

        /** If equals to 0 **/
        if (isZero(x) && isZero(y)) {return new int[0];}
        if (isZero(x)) {return y;}
        if (isZero(y)) {return x;}

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
        int[] res = new int[xIndex];
        long sum = 0;

        while (yIndex > 0){

            sum = (long) x[--xIndex] + y[--yIndex] + (sum / SUPER_RADIX);
            res[xIndex] = (int) (sum % SUPER_RADIX);

        }
        /** Check if carry to propagate **/
        boolean carry = (sum / SUPER_RADIX !=0);
        while (carry && xIndex > 0) {
            carry = ((res[--xIndex] = x[xIndex] + 1) == 0);
        }
        /** Fill with the remaining values of x if not reached **/
        while (xIndex > 0) {
            res[--xIndex] = x[xIndex];
        }
        /** Grow result if necessary **/
        if (carry) {
            int[] bigger = new int[res.length + 1];
            System.arraycopy(res, 0, bigger, 1, res.length);
            bigger[0] = 1;
            return bigger;
        }
        return res;
    }

    /**
     * Return val left shifted by k
     * @param val - int array to be leftshifted
     * @param k - int equals to the number of shift to be done
     * @return a int array equals to val<<k
     */
    private int[] leftShift(int[] val, int k) {
        int[] res = val;
        for (int i = 0; i < k; i++) {
            res = unitaryLeftShift(res);
        }
        return res;
    }

    /**
     * Return val left shifted by 1
     * Fragmented so that the long will not be overflowed !!
     * @param val - int array to be leftshifted
     * @return a int array equals to val<<1
     */
    private int[] unitaryLeftShift(int[] val) {

        int[] res = new int[val.length];
        long temp = 0;
        for (int i=val.length-1;i>-1;i--) {
            temp = val[i]<<1;
            /** More than 9 digits **/
            if (Long.toString(temp).length()>9) {
                /** Need for bigger array **/
                if(i==0) {
                    int[] bigger = new int[res.length + 1];
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
     * Return non empty size of the Array
     * Ex : {0,12} -> length = 2 but trueSize = 1
     * @param a - int array
     * @return number of stacks with a value
     */
    private int trueSize(int[] a) {
        int size = a.length;
        if (size==1) {return 1;}
        int i=0;
        while (a[i]==0) {i++;size--;}
        return size;
    }

    /**
     * Return True is the Integer Array given is equals to zero, and False is not
     * @param val - int array
     * @return true if val.value = 0, false if not
     */
    private boolean isZero(int[] val) {
        int len = val.length;
        for (int i=0;i<len;i++) {
            if (val[i]!=0) {
                return false;
            }
        }
        return true;
    }


    /**
     * Conversion from radix 10 to binary
     * @param value
     * @return
     */
    public ArrayList<Integer> toBinary(ArrayList<Integer> value) {

        StringBuilder sb = new StringBuilder();
        String str = "";
        ArrayList<Integer> zero = new ArrayList<>();
        ArrayList<Integer> res = new ArrayList<>();
        zero.add(0);

        while (compareArrayList(value,zero)!=0) {
            str+=String.valueOf(value.get(value.size()-1) % 2);
            value = array10RightShift(value,1);
        }
        sb.append(str);
        sb.reverse();
        str = sb.toString();
        /** Convert the binary string into ArrayList<Integer> **/
        /** First Stack **/
        int firstStackLength = str.length() % WORD_SIZE;
        if (firstStackLength==0) {firstStackLength = WORD_SIZE;}
        int index = 0;
        String stack = str.substring(index, index += firstStackLength);
        res.add(Integer.parseInt(stack,2));
        /** Others Stacks **/
        int stackValue;
        while (index < str.length()) {
            stack = str.substring(index,index += WORD_SIZE);
            stackValue = Integer.parseInt(stack,2);
            res.add(stackValue);
        }
        return res;
    }

    /**
     * Convert binary into radix 10
     * @param bin
     * @return
     */
    public ArrayList<Integer> toRadix10(ArrayList<Integer> bin) {

        ArrayList<Integer> radixTen = new ArrayList<>();
        int[] temp = new int[bin.size()];
        int[] one = new int[]{1};
        int shift = 0;
        for (int word = bin.size()-1; word >-1;word--){
            StringBuilder sb = new StringBuilder(Integer.toBinaryString(bin.get(word)));
            sb.reverse();
            for (int ch = 0;ch<sb.length();ch++){
                if (sb.charAt(ch)=='1'){
                    temp = add10(temp,leftShift(one,ch+shift));
                }
            }
            shift+=31;
        }
        /** Check if need to reduce array's size **/
        if (trueSize(temp)<temp.length) {
            int[] smaller = new int[trueSize(temp)];
            int tp = temp.length - trueSize(temp);
            System.arraycopy(temp, tp, smaller, 0, trueSize(temp));
            for (int i = 0; i < smaller.length; i++) {
                radixTen.add(smaller[i]);
            }
            return radixTen;
        }
        /** Fill the ArrayList **/
        for (int i=0;i<temp.length;i++) {radixTen.add(temp[i]);}
        return radixTen;
    }

    /**
     * Return val rightshifted by k
     * @param val - int array to be rightshifted
     * @param k - int equals to the number of shift to be done
     * @return a int array equals to val>>k
     */
    public ArrayList<Integer> array10RightShift(ArrayList<Integer> val, int k) {

        int[] res = new int[val.size()];
        int[] carry = new int[val.size()];
        ArrayList<Integer> result = new ArrayList<>();

        if(k!=0) {
            if (trueSize(val)==1){
                for (int i=0;i<val.size();i++) {
                    if (val.get(i)!=0) {res[i] += val.get(i)>>k;}
                    else {res[i] += 0;}
                }
            }
            else {
                for (int i=0;i<val.size();i++) {
                    int index = Integer.numberOfTrailingZeros(val.get(i));
                    if ((index > k) || (i==val.size()-1)) {
                        res[i] += val.get(i)>>k;
                    }
                    else {
                        res[i] += val.get(i)>>k;
                        //Get the decimal part
                        double t = (val.get(i) / Math.pow(2,k));
                        String str = String.format("%f",t);
                        str = removeZeros(str);
                        String[] te = str.split("\\.");
                        t = Integer.parseInt(te[1]);
                        int tp = (int) ((t*Math.pow(10,9-te[1].length())));
                        carry[i+1] = tp;
                    }
                }
                /** Apply the carries **/
                res = add10(res,carry);
            }

            /** Check if need to reduce array's size **/
            if (trueSize(res)<res.length) {
                int[] smaller = new int[trueSize(res)];
                int tp = res.length-trueSize(res);
                System.arraycopy(res, tp, smaller,0,trueSize(res));
                for (int i=0;i<smaller.length;i++) {
                    result.add(smaller[i]);
                }
                return result;
            }
            for (int i=0;i<res.length;i++) {
                result.add(res[i]);
            }
            return result;
        }
        else {return val;}

    }


    /**
     * Return val left shifted by k
     * @param k - int equals to the number of shift to be done
     * @return a int array equals to val<<k
     */
    public void leftShift(int k) {

        ArrayList<Integer> res = this.value;

        for (int i = 0; i < k; i++) {
            res = unitaryLeftShift(res);
        }
        this.value = res;
    }

    /**
     * Return val left shifted by 1
     * Fragmented so that the long will not be overflowed !!
     * @param val - int array to be leftshifted
     * @return a int array equals to val<<1
     */
    private ArrayList<Integer> unitaryLeftShift(ArrayList<Integer> val) {

        int[] res = new int[val.size()];
        int[] bigger = new int[val.size()+ 1];
        ArrayList<Integer> result = new ArrayList<>();
        boolean big = false;
        long temp = 0;
        for (int i=val.size()-1;i>-1;i--) {
            temp = val.get(i)<<1;
            /** More than 9 digits **/
            if (Long.toString(temp).length()>9) {
                /** Need for bigger array **/
                if(i==0) {
                    System.arraycopy(res, 0, bigger, 1, res.length);
                    bigger[0] += (int) (temp / SUPER_RADIX);
                    bigger[1] += (int) (temp % SUPER_RADIX);
                    big = true;
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
        /** Fill the ArrayList **/
        if (big) {
            for (int i=0;i<bigger.length;i++) {
                result.add(bigger[i]);
            }
        }
        else {
            for (int i=0;i<res.length;i++) {
                result.add(res[i]);
            }
        }
        return result;
    }

}
