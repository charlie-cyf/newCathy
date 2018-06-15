// We need to import the java.sql package to use JDBC
import java.sql.*;

// for reading from the command line
import java.io.*;

public class Manager extends Controller{

    private int managerID;
    private int branch;

    private void validateID () {
        int               input;
        int               id;
        boolean           quit = false;
        ResultSet         rs;
        PreparedStatement ps;

        try {
            while (!quit) {
                System.out.print("\n\nPlease enter your manager id or press enter 0 to quit: \n");

                input = Integer.parseInt(in.readLine());

                if (input != 0) {
                    id = input;
                    ps = con.prepareStatement("SELECT * FROM Clerk WHERE clerkID = ? AND type = 'Manager'");
                    ps.setInt(1, id);

                    rs = ps.executeQuery("SELECT * FROM Clerk WHERE clerkID = ? AND type = 'Manager'");

                    if (rs != null) {
                        System.out.print("Access granted: Welcome.");
                        managerID = id;
                        showMenu();
                    } else {
                        System.out.print("Access denied: Invalid manager ID.");
                    }

                    // close the statement;
                    // the ResultSet will also be closed
                    ps.close();
                } else { // User quits the system
                    quit = true;
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            System.out.println("IOException!");
        } catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());

            try {
                con.rollback();
            } catch (SQLException ex2) {
                System.out.println("Message: " + ex2.getMessage());
                System.exit(-1);
            }
        }
    }

    public void showMenu(){
        int     choice;
        boolean quit = false;
        try {
            while (!quit) {
                System.out.print("1.  Manage employee\n");
                System.out.print("2.  Manage item\n");
                System.out.print("3.  Manage membership\n");
                System.out.print("4.  Manage deal\n");
                System.out.print("5.  Generate transaction report\n");
                System.out.print("6.  Quit\n>> ");

                choice = Integer.parseInt(in.readLine());

                System.out.println(" ");

                switch (choice) {
                    case 1:
                        manageEmployeeWage();
                        break;
                    case 2:
                        manageItem();
                        break;
                    case 3:
                        manageMembership();
                        break;
                    case 4:
                        manageDeal();
                        break;
                    case 5:
                        getReport();
                        break;
                    case 6:
                        quit = true;
                }
            }
        } catch (IOException e) {
            System.out.println("IOException!");
        }
    }

    private void manageEmployeeWage() {
        int                id;
        int                wage;
        PreparedStatement  ps;
        try {
            ps = con.prepareStatement("UPDATE Clerk SET wage = ? WHERE clerkID = ?");

            System.out.print("\nClerk ID: ");
            id = Integer.parseInt(in.readLine());
            ps.setInt(2, id);

            System.out.print("\nSet new wage: ");
            wage = Integer.parseInt(in.readLine());
            while (wage < 0) {
                System.out.print("\nWage cannot be negative, please try again: ");
                wage = Integer.parseInt(in.readLine());
            }
            ps.setInt(1, wage);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println("\nEmployee " + id + " does not exist!");
            }

            con.commit();

            ps.close();
        } catch (IOException e) {
            System.out.println("IOException!");
        } catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());

            try {
                con.rollback();
            } catch (SQLException ex2) {
                System.out.println("Message: " + ex2.getMessage());
                System.exit(-1);
            }
        }
    }

    private void manageItem(){

        int     choice;
        boolean quit = false;

        try {
            while (!quit) {
                System.out.print("1.  Manage item storage\n");
                System.out.print("2.  Display item storage\n");
                System.out.print("3.  Manage item price\n");
                System.out.print("4.  Go back\n>> ");

                choice = Integer.parseInt(in.readLine());

                System.out.println(" ");

                switch (choice) {
                    case 1:
                        manageItemStorage();
                        break;
                    case 2:
                        manageItemPrice();
                        break;
                    case 3:
                        showMenu();
                }
            }
        }  catch (IOException e) {
            System.out.println("IOException!");
        }
    }

    private void manageItemStorage() {
            // TODO:
    }

    private void manageItemPrice () {
            // TODO
    }

    private void displayItemInfo() {
            // TODO
    }

    private void manageMembership(){
            // TODO:
    }
    private void manageDeal(){
            //TODO
    }
    private void getReport(){
            // TODO
    }
}