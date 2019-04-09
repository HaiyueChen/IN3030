/**
 * Sequential
 */
public class Sequential {

    public static int[] sort(int[] unsorted, int useBits){
        int[] a = unsorted;
        int[] b = new int[a.length];

        int max = 0;
        for (int i = 0; i < a.length; i++) {
            if(a[i] > max) max = a[i];
        }

        int numBits = 0;
        while(max > 1L << numBits) numBits ++;

        int numDigits = Math.max(1, numBits/useBits);
        int[] bits = new int[numDigits];
        int bitsPerDigit = numBits/numDigits;
        int rest = numBits % numDigits;
        for (int i = 0; i < bits.length - 1; i++) {
            bits[i] = bitsPerDigit;
        }
        bits[bits.length - 1] = bitsPerDigit + rest;
        
        int shift = 0;
        int[] temp;
        for (int i = 0; i < bits.length; i++) {
            radix(a, b, bits[i], shift);
            shift += bits[i];
            temp = a;
            a = b;
            b = temp;
        }
        return a;
    }

    public static void radix(int[] a, int[] b, int maskLen, int shift){
        int mask = (1 << maskLen) - 1;
        int[] digitFreq = new int[mask + 1];

        for (int i = 0; i < a.length; i++) {
            digitFreq[(a[i] >>> shift) & mask] ++; 
        }

        int sum = 0;
        int[] digitPointers = new int[digitFreq.length];
        for (int i = 0; i < digitFreq.length; i++) {
            digitPointers[i] = sum;
            sum += digitFreq[i];
        }

        for (int i = 0; i < a.length; i++) {
            b[digitPointers[(a[i] >>> shift) & mask] ++] = a[i];
        }
    }



}