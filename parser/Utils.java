package parser;

import java.io.File;

public class Utils {
    private static final String numberPattern = "[0-9]+";
    private static final String floatPattern = "[0-9]+\\.[0-9]+";

    public static boolean fileIsValid(File file) {
        return file != null && file.exists();
    }

    public static boolean isValidInteger(String val) {
        return val.matches(numberPattern);
    }


    public static boolean isValidFloat(String val) {
        return val.matches(floatPattern);
    }

}
