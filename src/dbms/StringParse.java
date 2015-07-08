/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbms;

import java.util.LinkedList;

/**
 *
 * @author Amin
 */
public class StringParse {

    public static LinkedList<String> parse(String input) {
        input = input.concat(" ");
        char[] chars = input.toCharArray();
        LinkedList<String> split = new LinkedList<String>();
        String temp = "";
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ' ' || chars[i] == ',') {
                if (temp.compareTo("") != 0) {
                    split.add(temp);
                }
                if (chars[i] == ',') {
                    split.add(String.valueOf(','));
                }
                temp = "";
            } else {
                temp = temp.concat(String.valueOf(chars[i]));
            }
        }
        return split;
    }
}
