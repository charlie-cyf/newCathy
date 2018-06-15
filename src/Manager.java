// We need to import the java.sql package to use JDBC
import java.sql.*;

// for reading from the command line
import java.io.*;

public class Manager extends controller{

    private int managerID;
    private int branch;

    public void validateID () {
        int               input;
        int               id;
        boolean           quit = false;
        ResultSet         rs;
        PreparedStatement ps;

        System.out.println(connect("ora_a1q1b", "a24581167"));
        try {
            while (!quit) {
                System.out.print("\n\nPlease enter your manager id or press enter 0 to quit: \n");

                input = Integer.parseInt(in.readLine());

                if (input != 0) {
                    id = input;
                    ps = con.prepareStatement("SELECT * FROM Clerk WHERE clerkID = ? AND type = 'Manager'");
                    ps.setInt(1, 1252);


                    rs = ps.executeQuery();
                    if (rs.next()) {
                        System.out.print("Access granted: Welcome.");
                        managerID = id;
                        branch = rs.getInt("branchNumber");
                        showMenu();
                        quit = true;
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
                System.out.print("\n");
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
                        System.exit(0);
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
            ps = con.prepareStatement("UPDATE Clerk SET wage = ? WHERE clerkID = ? AND branchNumber = ?");

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

            ps.setInt(3, branch);
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
                System.out.print("2.  Manage item price\n");
                //System.out.print("3.  Manage item price\n");
                System.out.print("3.  Go back\n>> ");

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
        int                id;
        int             amount;
        PreparedStatement  ps;
        try {
            ps = con.prepareStatement("UPDATE ItemStorage SET amount = ? WHERE itemID = ? AND branchNumber = ?");

            System.out.print("\nItem ID: ");
            id = Integer.parseInt(in.readLine());
            //displayItemInfo(id);
            ps.setInt(2, id);
            ps.setInt(3, branch);

            System.out.print("\nSet new storage amount: ");
            amount = Integer.parseInt(in.readLine());
            while (amount < 0) {
                System.out.print("\nPrice cannot be negative, please try again: ");
                amount = Integer.parseInt(in.readLine());
            }
            ps.setInt(1, amount);
            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println("\nItem " + id + " does not exist!");
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

    private void manageItemPrice () {
        int                id;
        double             price;
        PreparedStatement  ps;
        try {
            ps = con.prepareStatement("UPDATE Item SET price = ? WHERE itemID = ? ");

            System.out.print("\nItem ID: ");
            id = Integer.parseInt(in.readLine());
            //displayItemInfo(id);
            ps.setInt(2, id);

            System.out.print("\nSet new price: ");
            price = Double.parseDouble(in.readLine());
            while (price < 0) {
                System.out.print("\nPrice cannot be negative, please try again: ");
                price = Double.parseDouble(in.readLine());
            }
            ps.setDouble(1, price);
            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println("\nItem " + id + " does not exist!");
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


    private void displayItemInfo(int itemID)
    {
        String     iname;
        int        id = itemID;
        Double     price;
        String     itype;
        Statement  stmt;
        ResultSet  rs;

        try
        {
            stmt = con.createStatement();

            rs = stmt.executeQuery("SELECT * FROM Item WHRER itemID = id WHERE branchNumber = ?");

            // get info on ResultSet
            ResultSetMetaData rsmd = rs.getMetaData();



            // get number of columns
            int numCols = rsmd.getColumnCount();

            System.out.println(" ");

            // display column names;
            for (int i = 0; i < numCols; i++)
            {
                // get column name and print it

                System.out.printf("%-15s", rsmd.getColumnName(i+1));
            }

            System.out.println(" ");

            while(rs.next())
            {
                // for display purposes get everything from Oracle
                // as a string

                // simplified output formatting; truncation may occur

                id = rs.getInt("itemID");
                System.out.printf("%-10.10s", id);

                iname = rs.getString("name");
                System.out.printf("%-20.20s", iname);

                itype = rs.getString("type");
                if (rs.wasNull())
                {
                    System.out.printf("%-20.20s", " ");
                }
                else
                {
                    System.out.printf("%-20.20s", itype);
                }


                price = rs.getDouble("price");
                if (rs.wasNull())
                {
                    System.out.printf("%-15.15s\n", " ");
                }
                else
                {
                    System.out.printf("%-15.15s\n", price);
                }
            }

            // close the statement;
            // the ResultSet will also be closed
            stmt.close();
        }
        catch (SQLException ex)
        {
            System.out.println("Message: " + ex.getMessage());
        }
    }


    private void manageMembership(){
        // TODO:
    }
    private void manageDeal(){
        //TODO
    }
    private void getReport(){
//        String     bid;
//        String     bname;
//        String     baddr;
//        String     bcity;
//        String     bphone;
//        Statement  stmt;
//        ResultSet  rs;

//        int receiptNumber;
//        String purchaseTime;
//        String purchaseDate;
//        double totalPrice;
//        int clerkID;
//        int branchNumber;
//        Statement  stmt;
//        ResultSet  rs;
//
//        try
//        {
//            stmt = con.createStatement();
//
//            rs = stmt.executeQuery("SELECT * FROM Purchase WHERE isEarlier() ");
//
//            // get info on ResultSet
//            ResultSetMetaData rsmd = rs.getMetaData();
//
//            // get number of columns
//            int numCols = rsmd.getColumnCount();
//
//            System.out.println(" ");
//
//            // display column names;
//            for (int i = 0; i < numCols; i++)
//            {
//                // get column name and print it
//
//                System.out.printf("%-15s", rsmd.getColumnName(i+1));
//            }
//
//            System.out.println(" ");
//
//            while(rs.next())
//            {
//                // for display purposes get everything from Oracle
//                // as a string
//
//                // simplified output formatting; truncation may occur
//
//                bid = rs.getString("branch_id");
//                System.out.printf("%-10.10s", bid);
//
//                bname = rs.getString("branch_name");
//                System.out.printf("%-20.20s", bname);
//
//                baddr = rs.getString("branch_addr");
//                if (rs.wasNull())
//                {
//                    System.out.printf("%-20.20s", " ");
//                }
//                else
//                {
//                    System.out.printf("%-20.20s", baddr);
//                }
//
//                bcity = rs.getString("branch_city");
//                System.out.printf("%-15.15s", bcity);
//
//                bphone = rs.getString("branch_phone");
//                if (rs.wasNull())
//                {
//                    System.out.printf("%-15.15s\n", " ");
//                }
//                else
//                {
//                    System.out.printf("%-15.15s\n", bphone);
//                }
//            }
//
//            // close the statement;
//            // the ResultSet will also be closed
//            stmt.close();
//        }
//        catch (SQLException ex)
//        {
//            System.out.println("Message: " + ex.getMessage());
//        }
//    }
//
//    private boolean isEarlier(String d1, String d2) {
//        int month1 = Integer.parseInt(d1.substring(0,2));
//        int month2 = Integer.parseInt(d2.substring(0,2));
//        int day1 = Integer.parseInt(d1.substring(3));
//        int day2 = Integer.parseInt(d2.substring(3));
//
//        return month1 < month2 && day1 < day2;
      }
}