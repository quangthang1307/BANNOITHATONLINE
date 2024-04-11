package poly.edu.utils;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class validUtil {

    public static boolean containsNumber(String str) {
        return str.matches(".*\\d.*");
    }

    public static boolean containsSpecialCharacters(String str) {
        return str.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
    }

}
