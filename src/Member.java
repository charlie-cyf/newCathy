import java.sql.*;

// for reading from the command line
import java.io.*;

// for the login window
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Member extends controller {
    int memID;

    public void memberShowMenu() {
        connect("ora_a1q1b", "a24581167");

        //enter system
        checkPoint();


        int choice;
        boolean quit;

        quit = false;

        try {
            while (!quit) {

                System.out.print("\n\nPlease choose one of the following: \n");
                System.out.print("1.  Check another membership point\n");
                System.out.print("2.  Quit\n");

                choice = Integer.parseInt(in.readLine());

                System.out.println(" ");

                switch (choice) {
                    case 1:
                        checkPoint();
                        break;
                    case 2:
                        quit = true;
                }
            }

            System.out.println("\nGood Bye!\n\n");
            System.exit(0);
        } catch (IOException e) {
            System.out.println("IOException!");

            try {
                con.close();
                System.exit(-1);
            } catch (SQLException ex) {
                System.out.println("Message: " + ex.getMessage());
            }
        }
    }

    private void checkPoint() {

        boolean enterSucc = false;
        Statement statement;
        ResultSet result;
        int enteredID;

        while (!enterSucc) {
            String memName = null;
            int point = 0;
            try {
                System.out.println("please enter membership id. ");
                enteredID = Integer.parseInt(in.readLine());
                statement = con.createStatement();
                result = statement.executeQuery("SELECT * FROM Membership WHERE memberID = " + enteredID);
                result.next();
                result.getInt("memberID");
                enterSucc = true;
                memName = result.getString("name");
                point = result.getInt("points");


            } catch (IOException e) {
                System.out.println("IOException!");

                try {
                    con.close();
                    System.exit(-1);
                } catch (SQLException ex) {
                    System.out.println("Message: " + ex.getMessage());
                }
            } catch (SQLException ex) {
                System.out.println("Message: " + ex.getMessage());
                enterSucc = false;
                System.out.println("invalid membership id! ");
                System.out.println("enter 1 to exit");
                System.out.println("enter 2 to try again");
                try {
                    int exitMem = Integer.parseInt(in.readLine());
                    if (exitMem == 1) {
                        System.out.println("GoodBye!");
                        System.exit(0);
                    }
                } catch (IOException ie) {
                    System.out.println("io exception " + ie.getMessage());
                }

            }


            System.out.println("hello " + memName);
            System.out.println("your membership point is " + point);


        }

    }
}
