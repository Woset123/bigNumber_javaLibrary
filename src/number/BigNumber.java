package number;


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
     * To get the int value of a long. A bit lower than it could be but to make sure
     * the result will not be negative
     */
    final long LONG_MASK = 0xFFFFFFFFL;

    /**
     * Value used for arithmetic operations
     */
    final long SUPER_RADIX = 1000000000;


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


        /** Erase spaces **/
        val =val.replaceAll("\\s+", "");

        /** Check if all digits are figures **/
        /** To Do **/

        /** Erase the leading zeros if are **/
        /** To Do **/

        /** Number of Digits in the input given **/
        this.nbDigits = val.length();

        /** Number of bits required
         * Add one so that the space allocated will never be too small
         */
        this.nbBits = (int) (Math.ceil(((float) (this.nbDigits * Math.log(10) / Math.log(2))))+1);

        /** Number of 16bits words necessary **/
        this.nbWords = (int) (Math.ceil(((float) (this.nbBits) / (float) (WORD_SIZE))));

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

        this.strValue = toString(this.value);
    }


    /**
     * Big Number Constructor
     * Array of 32 bits Integer given in input
     * Radix 10 !!
     * @param value
     */
    private BigNumber(int[] value) {

        this.nbWords = value.length;
        this.value = value;
        this.strValue = toString(this.value);
    }


    /** Constant BigNumbers for Montgomery Multiplication **/
    public static final BigNumber MGY_V = new BigNumber("123");

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
     * @return
     */
    public BigNumber mulMontgomery(BigNumber bigNumber) { return new BigNumber(mulMontgomery(this.value, bigNumber.getValue()));}


    /**
     * Division of two Big Numbers
     * Value stored into the Array of 32 bits Integer
     * @param bigNumber
     * @return
     */
    public BigNumber div(BigNumber bigNumber) { return new BigNumber(div(this.value, bigNumber.getValue())); }


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

            sum = (x[--xIndex] & LONG_MASK) + (y[--yIndex] & LONG_MASK) + (sum >>> WORD_SIZE);
            res[xIndex] = (int) (sum & LONG_MASK);

        }

        /** Check if carry to propagate **/
        boolean carry = (sum >>> 32 !=0);
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

        sum = (x[--xIndex] & LONG_MASK) + (y & LONG_MASK) + (sum >>> WORD_SIZE);
        res[xIndex] = (int) (sum & LONG_MASK);


        /** Check if carry to propagate **/
        boolean carry = (sum >>> 32 !=0);
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
        if ((big.length < small.length) || (big.length == small.length && (big[0]< small[0]))) {
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
            sub = (big[--bigIndex] & LONG_MASK) - (small[--smallIndex] & LONG_MASK) - borrow;
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


    /**
     * Implements mod function
     * @param a
     * @param mod
     * @return a mod n
     */
    public int[] mod(int[] a, int[] mod) {

        BigNumber bg = new BigNumber(a);

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
        int[] temp = new int[x.length + y.length-1];
        int xCursor = x.length-1;
        int yCursor = y.length-1;
        long product = 0;


        for (int i = xCursor; i > -1; i--) {

            for (int j = yCursor; j > -1; j--) {

                product = (long) (((long) x[i] & LONG_MASK) * ((long) y[j] & LONG_MASK));

                if (product > Integer.MAX_VALUE && (i+j-1 > -1)) {
                    temp[i+j-1] = (int) (temp[i+j-1] + Math.ceil(product / SUPER_RADIX));
                    temp[i+j] += (int) (product % SUPER_RADIX);
                }
                else {
                    temp[i+j] += (int) product;
                }
            }
        }
        return temp;
    }

    /** (In progress)
     * Montgomery Multiplication of two Arrays of 32 bits Integer
     * Used for performance enhancement
     * Algorithm :
     * 1. s = A * B
     * 2. t = (s * v) mod r
     * 3. m = (s + (t * n))
     * 4. u = m/r
     * 5. if u >= n then return u - n | else return u
     * @param a
     * @param b
     * @return Montgomery Multiplication value into Arrays of 32 bits Integer
     */
    public int[] mulMontgomery(int[] a, int[] b) {

        /** Defining variables **/
        BigNumber r = new BigNumber(); // paste the value from magmacalculator for example
        BigNumber n = new BigNumber(); // paste the value from magmacalculator for example
        BigNumber u = new BigNumber();

        /** Step 1 **/
        int[] s = mul(a,b);

        /** Step 2 **/
        int[] t = modularSub(s, MGY_V.value,r.getValue());

        /** Step 3 **/
        int[] m = add(s,mul(t,n.getValue()));

        /** Step 4 **/
        //Implements division
        //u.value =

        /** Step 5 **/
        // u >= n
        if (u.compareValue(n.getValue())>-1) {
            return substract(u.value, n.value);
        }
        else {
            return u.value;
        }
    }

    /** (In progress)
     * Division of two arrays of 32 bits Integer
     * @param a
     * @param b
     * @return
     */
    public int[] div(int[] a, int[] b) {

        return new int[1];
    }


    /**
     * Compare value array of two Big Numbers
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
                if ((valThis & LONG_MASK) < (yValue & LONG_MASK)) {
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
            currentStack = (int) (((long) res[i]) & LONG_MASK) + remainder;
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
