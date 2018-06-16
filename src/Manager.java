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
                    ps.setInt(1, id);


                    rs = ps.executeQuery();

                    if (rs.next()) {
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
        int     choice;
        boolean quit = false;

        try {
            while (!quit) {
                System.out.print("1.  Add new deal\n");
                System.out.print("2.  Display all deals\n");
                System.out.print("3.  Modify deal\n");
                System.out.print("4.  Go back\n>> ");

                choice = Integer.parseInt(in.readLine());

                System.out.println(" ");

                switch (choice) {
                    case 1:
                        addNewDeal();
                        break;
                    case 2:
                        showAllDeals();
                        break;
                    case 3:
                       // modifyDeal();
                }
            }
        }  catch (IOException e) {
            System.out.println("IOException!");
        }
    }

    private void showAllDeals(){
        String     dealName;
        String     duration;
        int itemID;
        double percentage;

        Statement  stmt;
        ResultSet  rs;
        try
        {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT d.dealName AS dealName, d.duration as duration, id.itemId as itemId, id.percentage as persentage FROM Deal d, ItemsInDeal id WHERE d.dealName = id.dealName");
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
                itemID = rs.getInt("itemId");
                System.out.printf("%-10.10s", itemID);
                dealName = rs.getString("dealName");
                System.out.printf("%-20.20s", dealName);
                duration = rs.getString("duration");
                System.out.printf("%-20.20s", duration);
                percentage = rs.getDouble("persentage");
                System.out.printf("%-15.15s\n", percentage);
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

    private boolean checkValidDur(String duration){
        // TODO: check duration valid
        return true;
    }
    private void addNewDeal(){
        String dealName = null;
        String dur;

        int itemID;
        double percent;

        boolean canExit = false;
        PreparedStatement  ps;

        while (!canExit){
            try
            {
                ps = con.prepareStatement("INSERT INTO Deal VALUES (?,?)");
                System.out.print("\nDeal name: ");
                dealName = in.readLine();
                ps.setString(1, dealName);
                System.out.print("\nDuration: ");
                dur = in.readLine();
                ps.setString(2, dur);
                while (!checkValidDur(dur)){
                    System.out.println("invalid duration format, please try again.");
                    dur = in.readLine();
                }
                ps.executeUpdate();
                // ps.close();
            }
            catch (IOException e)
            {
                System.out.println("IOException!");
            }
            catch (SQLException ex)
            {
                System.out.println("Message: " + ex.getMessage());
                System.out.println("Insertion failed!");
                try
                {
                    // undo the insert
                    con.rollback();
                    System.exit(-1);
                }
                catch (SQLException ex2)
                {
                    System.out.println("Message: " + ex2.getMessage());
                    System.exit(-1);
                }
            }

            try{
                System.out.println("Do you want to add item in this deal? enter Y for yes");
                String ans = in.readLine();
                if(ans.equals("Y") || ans.equals("y")){
                    System.out.print("please enter item id: ");
                    itemID = Integer.parseInt(in.readLine());
                    Statement s = con.createStatement();
                    ResultSet res = s.executeQuery("SELECT * FROM Item WHERE itemID = " + itemID);
                    if(!res.next()){
                        System.out.println("no such item please try again! ");
                    }else {
                        System.out.println("item found, please enter persentage");
                        percent = Double.parseDouble(in.readLine());
                        ps = con.prepareStatement("INSERT INTO ItemsInDeal VALUES (?,?,?)");
                        ps.setInt(1, itemID);
                        ps.setString(2, dealName);
                        ps.setDouble(3, percent);
                        ps.executeUpdate();
                        ps.close();
                    }
//                    con.commit();
                    s.close();
                }
                System.out.println("do you want to add another deal? enter Y for yes");
                canExit = !in.readLine().equals("Y");
            }catch (SQLException se) {
                // TODO
            } catch (IOException ie){
                System.out.println("IOException!");
                try {
                    con.close();
                    System.exit(-1);
                } catch (SQLException ex) {
                    System.out.println("Message: " + ex.getMessage());
                }
            }

        }

    }
    private void getReport(){
            // TODO
    }
}