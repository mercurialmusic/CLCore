package de.craftlancer.core.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandUtils {

    public static String[] parseArgumentStrings(String[] args) {
        List<String> tmp = new ArrayList<>();
        
        // count " at the start and end of each string
        int[] open = new int[args.length];
        int[] close = new int[args.length];
        
        for (int i = 0; i < args.length; i++) {
            for (int j = 0; j < args[i].length() && args[i].charAt(j) == '\"'; j++)
                open[i]++;
            
            for (int j = args[i].length() - 1; j >= 0 && args[i].charAt(j) == '\"'; j--)
                close[i]++;
        }
        
        // iterate over the input strings with a string pointer
        int stringPtr = 0;
        while (stringPtr < args.length) {
            // if it doesn't start with a ", leave it as it is
            if (open[stringPtr] <= 0) {
                tmp.add(args[stringPtr]);
                stringPtr++;
                continue;
            }
            
            // otherwise count the difference between the opening/closing count of " for the string
            // and subsequent input strings until it is <= 0. Join the strings between stringPtr and j,
            // trim the starting and ending " and set the stringPtr to the value of j. 
            int count = 0;
            for (int j = stringPtr; j < args.length; j++) {
                count += open[j];
                count -= close[j];
                
                if (count <= 0) {
                    String joined = String.join(" ", Arrays.copyOfRange(args, stringPtr, j + 1));
                    tmp.add(joined.substring(1, joined.length() - 1));
                    stringPtr = j; // we dealt with those strings already
                    break;
                }
            }

            // If the count never reaches <= 0, leave the current input string as it is.
            if (count > 0)
                tmp.add(args[stringPtr]);
            
            stringPtr++;
        }
        
        return tmp.toArray(new String[tmp.size()]);
    }
}
