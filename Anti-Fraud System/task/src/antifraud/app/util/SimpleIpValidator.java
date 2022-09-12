package antifraud.app.util;

import java.util.Optional;

/**
 * Roman Pashkov created on 06.09.2022 inside the package - antifraud.app.util
 */
public class SimpleIpValidator {
    public static boolean providedCorrectIp(Optional<String> ipopt) {
        if (ipopt.isEmpty()) {
            return false;
        }

        String ip = ipopt.get();
        String[] ipBitsArr = ip.split("\\.");
        if (ipBitsArr.length < 4) {
            return false;
        }
        for (String s : ipBitsArr) {
            int i = Integer.parseInt(s);
            if (i < 0 || i > 255) {
                return false;
            }
        }
        return true;
    }
}