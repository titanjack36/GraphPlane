//----------------------------FRACTION CLASS----------------------------------//
//@author TitanJack
//@project MathTools
//Useful functions for operations with numbers. For now, its main functionality
//is to format the function so that it removes imperfections in the number
//created by precision limitations

package NumberFormats;

import java.text.DecimalFormat;

public class Numbers {

    //Function: Format Num
    //@param num    the number to be formatted into string
    //@return       the formatted string representation of the number
    //If number is whole, return string integer
    //If rational, return string fraction approximation
    public static String formatNum(double num) {
        //Check for errors like: 1.0000000000001 and get rid of decimal place
        if (abs(round(num) - num) < 0.0000001) num = round(num);
        if (num == round(num)) return (int)num + ""; //return integer string
        else {
            Fraction fracValue = new Fraction(num, 100);
            return fracValue.toString(); //return fraction string
        }
    }

    //Function: Trim Digits
    //@param num                the number to be trimmed
    //       numOfDigits        how many decimal places to be trimmed to
    //@return                   the trimmed number
    //Removes digits from the number which are larger than the specified number
    //of digits
    @SuppressWarnings("SameParameterValue")
    public static String trimDigits(double num, int numOfDigits) {

        if (numOfDigits == 0) {
            return (int)(num + 0.5) + "";
        } else {
            String format = "#.";
            for (int i = 0; i < numOfDigits; i++) format = format.concat("#");
            int exponential = num == 0 ? 0 : (int)Math.log10(abs(num));
            if (abs(exponential) >= numOfDigits) num /= Math.pow(10,
                    exponential);
            DecimalFormat df = new DecimalFormat(format);
            return df.format(num) + (abs(exponential) >= numOfDigits ?
                    "e" + exponential : "");
        }
    }

    public static double abs(double num) {
        return num >= 0 ? num : -num;
    }

    public static int round(double num) {
        return num % 1 < 0.5 ? (int)num : (int)num + 1;
    }
}
