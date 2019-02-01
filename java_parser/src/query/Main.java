package query;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {
    private static Map<String, String> flagMap = new HashMap<>();
    private static Set<String> flags = new HashSet<String>() {{
        add("-s");
        add("-o");
        add("-f");
    }};

    public static void main(String[] args) {
        boolean order = false;
        boolean select = false;
        boolean filter = false;
        if (args.length % 2 != 0) { // if args aren't even it is a bad combo
            throw new RuntimeException("invalid argument combination");
        }
        for (int i = 0; i < args.length; i++) {
            String current = args[i];
            // if we find a flag we like put it and its value in the map
            switch (current) {
                case "-s":
                    select = true;
                    flagMap.put(current, args[i + 1]);
                    break;
                case "-o":
                    order = true;
                    flagMap.put(current, args[i + 1]);
                    break;
                case "-f":
                    filter = true;
                    flagMap.put(current, args[i + 1]);
                    break;
            }
        }





    }
}
