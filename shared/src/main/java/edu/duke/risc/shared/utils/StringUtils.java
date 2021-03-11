package edu.duke.risc.shared.utils;

import java.util.regex.Pattern;

/**
 * @author eason
 * @date 2021/3/11 13:14
 */
public class StringUtils {

    public static boolean isNumeric(String input) {
        input = input.trim();
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(input).matches();
    }

}
