/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbms;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Amin
 */
public class ReadFile {

    public LinkedList<String> read(String filename) {
        try {
            LinkedList<String> readList = new LinkedList<String>();
            File f = new File(filename);
            if (!f.exists()) {
                System.err.println("File not found!");
                return null;
            }
            RandomAccessFile rand = new RandomAccessFile(f, "r");
            int c = (int) rand.length();
            String temp = "";
            char x;
            rand.seek(0);
            for (int i = 0; i < c; i++) {
                x = (char) rand.readByte();
                temp = temp.concat(String.valueOf(x));
            }
            char d = temp.charAt(0);
            String delimiter = "";
            delimiter = delimiter.concat(String.valueOf(d));
            delimiter = delimiter.concat(String.valueOf(d));
            temp = temp.concat(delimiter);
            char[] ch = temp.toCharArray();
            temp = "";
            for (int j = 0; j < ch.length; j++) {
                if (ch[j] == d) {
                    if (temp.compareTo("") != 0) {
                        readList.add(temp);
                    }
                    temp = "";
                } else {
                    temp = temp.concat(String.valueOf(ch[j]));
                }
            }

            return readList;
        } catch (Exception ex) {
            Logger.getLogger(ReadFile.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
