package antifraud.app.util;

import java.util.Optional;

/**
 * Roman Pashkov created on 06.09.2022 inside the package - antifraud.app.util
 */
public class CardValidator {

    public static boolean providedCardNumberIsValid(Optional<String> optNum) {
        if (optNum.isEmpty()) {
            return false;
        }
        String number = optNum.get();
        String[] split = number.split("");
        int[] d = new int [split.length];
        int count = 0;
        for (String s : split){
            d[count] = Integer.parseInt(s);
            count++;
        }
        return checkNumber(d);
    }

    private static boolean checkNumber(int[] digits)
    {
        int sum = 0;
        boolean alt = false;
        for(int i = digits.length - 1; i >= 0; i--)
        {
            int temp = digits[i];
            if(alt)
            {
                temp *= 2;
                if(temp > 9)
                {
                    temp -= 9; //równoważne dodaniu cyfr do siebie np. 1+6 = 7, 16-9 = 7
                }
            }
            sum += temp;
            alt = !alt;
        }
        return sum % 10 == 0;
    }
}
