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
public class Command_Execute {

    Users user = new Users();
    ReadFile rf = new ReadFile();
    LinkedList<String> rlist = new LinkedList<String>();
    XMLBuilder XML = new XMLBuilder();
    String username = null;

    public void Execute(LinkedList<String> command) {
        if (command.size() > 0) {
            if (command.size() == 5 && command.get(0).compareTo("create") == 0 && command.get(1).compareTo("user") == 0 && command.get(3).compareTo(",") == 0) {
                user.CreateUser(command.get(2), command.get(4));
                username = user.UserLogin(command.get(2), command.get(4));


                System.out.println("[USER CREATED - USER LOGGINED]");

            } else if (command.size() == 4 && command.get(0).compareTo("login") == 0 && command.get(2).compareTo(",") == 0) {
                username = user.UserLogin(command.get(1), command.get(3));

            } else if (username != null && command.size() == 1 && command.get(0).compareTo("logout") == 0) {

                user.UserLogout(username);
                username = null;
                System.out.println("[LOGOUT]");
            } else if (username != null && command.size() == 5 && command.get(0).compareTo("change") == 0 && command.get(1).compareTo("password") == 0 && command.get(3).compareTo(",") == 0) {
                user.ChangePassword(username, command.get(2), command.get(4));
            } else if (username != null && command.size() >= 2 && command.get(0).compareTo("create") == 0 && command.get(1).compareTo("table") == 0) {

                XML.CreateTable(command, username);
                XML.CreateLog(username, "CREAT TABLE " + command.get(2));
                System.out.println("[TABLE CREATED]");
            } else if (username != null && command.size() == 3 && command.get(0).compareTo("load") == 0) {
                rlist = rf.read("c:\\" + command.get(2) + ".txt");
                XML.load(command.get(1), rlist);
                XML.CreateLog(username, "LOAD TABLE " + command.get(1));
                System.out.println("[TABLE LOAD COMPLETE]");
            } else if (username != null && command.size() == 4 && command.get(0).compareTo("select") == 0 && command.get(1).compareTo("*") == 0 && command.get(2).compareTo("from") == 0) {
                XML.selAll(command.get(3));

            } else if (username != null && command.size() == 8 && command.get(0).compareTo("select") == 0 && command.get(1).compareTo("*") == 0 && command.get(2).compareTo("from") == 0 && command.get(4).compareTo("where") == 0) {
                XML.selWhere(command);
            } else if (username != null && command.size() >= 8 && command.get(0).compareTo("select") == 0 && command.get(1).compareTo("*") != 0) {
                int c = 0;
                LinkedList<String> fields = new LinkedList<String>();
                for (int i = 0; i < command.size(); i++) {
                    if (command.get(i).compareTo(",") == 0) {
                        c++;
                    }
                }
                int fIndex = 1;
                c++;
                for (int j = 0; j < c; j++) {
                    fields.add(command.get(fIndex));
                    fIndex += 2;
                }
                XML.selFieldwhere(command, fields);

            } else if (username != null && command.size() == 2 && command.get(0).compareTo("display") == 0 && command.get(1).compareTo("logfile") != 0 && command.get(1).compareTo("catalogue") != 0) {
                XML.displayTable(command.get(1));
            } else if (username != null && command.size() == 2 && command.get(0).compareTo("display") == 0 && command.get(1).compareTo("catalogue") == 0) {
                XML.displayCatalogue();
            } else if (username != null && command.size() == 2 && command.get(0).compareTo("display") == 0 && command.get(1).compareTo("logfile") == 0) {

                XML.diplayLog();
            } else {
                System.err.println("command not found or first loggin!");

            }
        }

    }
}
