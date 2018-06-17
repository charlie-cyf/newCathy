import java.sql.*;
import java.io.*;
import java.util.Random;

public class Employee extends controller {
    private int id;
    private int branch;

    public Employee() {
    }


    public void employeeShowMenu() {
        int choice;
        boolean quit = false;
        try {
            while (!quit) {
                System.out.print("\n\nPlease choose one of the following: \n");
                System.out.print("1.  processPurchase\n");
                System.out.print("2.  manageMembership\n");
                System.out.print("3.  Quit\n");
                choice = Integer.parseInt(in.readLine());

                System.out.println(" ");

                switch (choice) {
                    case 1:
                        processPurchase();
                        break;
                    case 2:
                        manageMemberShip();
                        break;
                    case 3:
                        quit = true;
                }
            }
        }catch (IOException e){
            System.out.println("IOException!");
        }
    }

    private void manageMemberShip() {
        String name;
        String phone;
        PreparedStatement ps;
        ResultSet rs;
        int id = 0;
        int point;
        boolean current = false;
        Random r = new Random();

        try {
            while(!current) {
                id = r.nextInt(1000);
                ps = con.prepareStatement("SELECT * FROM MemberShip WHERE memberID = ?");
                ps.setInt(1, id);
                rs = ps.executeQuery();
                if(!rs.next())
                    current = true;
                ps.close();
            }

            ps = con.prepareStatement("INSERT INTO MemberShip VALUES (?,?,?,1)");
            ps.setInt(1,id);
            System.out.print("\nplease Enter Member name : \n");
            name = in.readLine();
            ps.setString(2,name);
            System.out.print("\nplease Enter Member phone number : \n");
            phone = in.readLine();
            ps.setString(3,phone);
            ps.executeUpdate();

            con.commit();
            ps.close();

        }catch(IOException e){
            System.out.println("IOException!");
        }
        catch (SQLException ex)
        {
            try
            {
                // undo the insert
                con.rollback();
            }
            catch (SQLException ex2)
            {
                System.out.println("Message: " + ex2.getMessage());
                System.exit(-1);
            }
            System.out.println("Message: " + ex.getMessage());
        }
    }

    private void processPurchase() {
        int itemid;
        double totalPrice = 0;
        int receiptNumber = 0;
        boolean current = false;
        boolean finish = false;
        PreparedStatement ps;
        ResultSet rs;
        Random r = new Random();

        try {
            while(!current) {
                receiptNumber = branch * 1000 + r.nextInt(100) + 1000000;
                ps = con.prepareStatement("SELECT * FROM Purchase WHERE receiptNumber = ?");
                ps.setInt(1, receiptNumber);
                rs = ps.executeQuery();
                if (!rs.next())
                    current = true;
                ps.close();
            }

            ps = con.prepareStatement("INSERT INTO Purchase VALUES (?,1,1,1,?,?)");
            ps.setInt(1,receiptNumber);
            ps.setInt(2,id);
            ps.setInt(3,branch);

            ps.executeUpdate();

            con.commit();
            ps.close();

            while(!finish) {
                totalPrice += showPurchase(receiptNumber);
                System.out.print("\n\nTotal: " + totalPrice);
                System.out.print("\n press 0 to quit");
                System.out.print("\n press 1 to delete last item");
                System.out.print("\n press 2 to finish the purchase");
                System.out.print("\nplease Enter item ID: \n");
                itemid = Integer.parseInt(in.readLine());
                if(itemid ==0 )
                    System.exit(0);
                else if(itemid == 1)
                    deleteItem();
                else if(itemid == 2)
                    finish = true;
                else
                    addItem(itemid, receiptNumber);

            }


        }catch (IOException e) {
            System.out.println("IOException!");
        }
         catch (SQLException ex)
            {
                try
                {
                    // undo the insert
                    con.rollback();
                }
                catch (SQLException ex2)
                {
                    System.out.println("Message: " + ex2.getMessage());
                    System.exit(-1);
                }
                System.out.println("Message: " + ex.getMessage());
            }
    }

    public void validateID() {
        int eid;
        PreparedStatement ps;
        ResultSet rs;
        boolean quit = false;

            try {
                String connectURL = "jdbc:oracle:thin:@dbhost.ugrad.cs.ubc.ca:1522:ug";
                con = DriverManager.getConnection(connectURL,"ora_a1q1b", "a24581167");
                while (!quit) {
                    ps = con.prepareStatement("SELECT branchNumber FROM Clerk WHERE clerkID = ?");
                    System.out.print("\nPlease enter your employee ID or press 0 to quit: ");
                    eid = Integer.parseInt(in.readLine());
                    if (eid == 0)
                        System.exit(0);
                    ps.setInt(1, eid);

                    rs = ps.executeQuery();
                    if (!rs.next())
                        System.out.print("\nAccess denied: Invalid employee ID");
                    else {
                        this.branch = rs.getInt("branchNumber");
                        this.id = eid;
                        quit = true;
                    }

                    ps.close();
                }

            } catch (IOException e) {
                System.out.println("IOException!");
            } catch (SQLException ex) {
                System.out.println("Message: " + ex.getMessage());

            }



        employeeShowMenu();
    }

    private double showPurchase(int receiptNumber){
        String     itemID;
        String     itemName;
        double    itemPrice = 0;
        int itemAmount = 0;
        String     itemType;
        PreparedStatement  ps;
        ResultSet  rs;
        System.out.print("\nItems:");
        try{
            ps = con.prepareStatement("SELECT * FROM Item i, ItemsInPurchase ip WHERE ip.receiptNumber = ? and i.itemID = ip.itemID");
            ps.setInt(1,receiptNumber);
            rs = ps.executeQuery();

            System.out.println(" ");


            System.out.printf("%-10s", "ITEMID");
            System.out.printf("%-30s", "NAME");
            System.out.printf("%-15s", "PRICE");
            System.out.printf("%-20s", "TYPE");
            System.out.printf("%-15s", "AMOUNT");

            System.out.println(" ");

            while(rs.next())
            {

                itemID = rs.getString("itemID");
                System.out.printf("%-10.10s", itemID);

                itemName = rs.getString("name");
                System.out.printf("%-30.20s", itemName);

                itemPrice = rs.getDouble("price");
                System.out.printf("%-15.20s", itemPrice);

                itemType = rs.getString("type");
                System.out.printf("%-20.15s", itemType);

                itemAmount = rs.getInt("amount");
                System.out.printf("%-15.15s\n",itemAmount);

            }
            ps.close();
        }
        catch (SQLException ex){
            System.out.println("Message: " + ex.getMessage());
        }

        return itemPrice;
    }

    private void addItem(int itemid, int receiptNumber){
        PreparedStatement  ps;
        ResultSet  rs;

        try {
            ps = con.prepareStatement("SELECT * FROM item WHERE itemID = ?");
            ps.setInt(1, itemid);
            rs = ps.executeQuery();
            //ps.close();
            if (!rs.next())
                System.out.println("Invaild itemID");
            else{
                ps = con.prepareStatement("INSERT INTO itemsInPurchase VALUES (?,?,1)");
                ps.setInt(1,receiptNumber);
                ps.setInt(2, itemid);
                ps.executeUpdate();

                con.commit();
            }
            ps.close();
        }catch (SQLException ex){
            try
            {
                // undo the insert
                con.rollback();
            }
            catch (SQLException ex2)
            {
                System.out.println("Message: " + ex2.getMessage());
                System.exit(-1);
            }
            System.out.println("Message: " + ex.getMessage());
        }
    }

    private void deleteItem(){

    }

    }
