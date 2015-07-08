/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbms;

import java.util.LinkedList;
import java.util.Scanner;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Command_Execute exe = new Command_Execute();

        /**
         * LinkedList<String> list = new LinkedList<String>(); ReadFile rf = new
         * ReadFile(); list = rf.read("c:\\read.txt");
         * System.out.println(list.get(17));
         *
         */
        while (true) {
            Scanner sc = new Scanner(System.in);
            if (exe.username == null) {
                System.out.print(">");
            } else {
                System.out.print(exe.username + ">");
            }
            String command = sc.nextLine();
            LinkedList<String> query = StringParse.parse(command);
            exe.Execute(query);
        }


    }
}
