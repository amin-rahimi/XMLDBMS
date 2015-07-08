/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbms;

import java.util.Scanner;

/**
 *
 * @author Amin
 */
public class Users {

    int index;
    String username;
    String password;
    Scanner sc = new Scanner(System.in);
    XMLBuilder XML = new XMLBuilder();

    public void CreateUser(String username, String password) {
        String PASS_HASH = MD5_Hash.MD5(password);
        XML.createBankUsers(username, PASS_HASH);
        XML.CreateLog(username, "USER CREATED");
    }

    public String UserLogin(String username, String password) {

        //return 1=accept 0=denied -1=exception
        int check = XML.CheckLogin(username, password);
        if (check > 0) {
            //user index in xml file
            this.index = check - 1;
            XML.CreateLog(username, "USER LOGGIN");
            //return username of user
            return username;
        }
        System.err.println("username or password not found!");
        return null;

    }

    public void UserLogout(String username) {
        XML.CreateLog(username, "USER LOGOUT");
    }

    public int ChangePassword(String username, String OLDpassword, String NEWpassword) {
        int check = XML.ChangePassword(index, username, OLDpassword, NEWpassword);
        if (check > 0) {
            XML.CreateLog(username, "PASSWORD CHANGED");
            System.out.println("[PASSWORD CHANGED]");
            return 1;
        }
        System.err.println("[PASSWORD NOT CHANGED]");
        return 0;
    }
}
