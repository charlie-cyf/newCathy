import java.sql.*;
import java.io.*;
import java.util.Random;
import java.util.Date;

public class Employee extends controller {
    private int id;

    private int branch;

    public Employee() {}

    public void employeeShowMenu() throws IOException, SQLException{
        int choice;
        boolean quit = false;
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

    }

    private void manageMemberShip() throws IOException, SQLException {
        String name;
        String phone;
        PreparedStatement ps;
        ResultSet rs;
        int id = 0;
        int point;
        boolean current = false;
        Random r = new Random();

        while(!current) {
            id = r.nextInt(1000);
            ps = con.prepareStatement("SELECT * FROM MemberShip WHERE memberID = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if(!rs.next())
                current = true;
            ps.close();
        }

        ps = con.prepareStatement("INSERT INTO MemberShip VALUES (?,?,?,0)");
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
    }

    private void processPurchase() throws IOException, SQLException {
        int itemid;
        int lastItem = 0;
        double[] price_id = {0,0};
        double price;
        double totalPrice = 0;
        int receiptNumber = 0;
        boolean current = false;
        boolean finish = false;
        PreparedStatement ps;
        ResultSet rs;
        Random r = new Random();

        while(!current) {
            receiptNumber = branch * 1000 + r.nextInt(100) + 1000000;
            ps = con.prepareStatement("SELECT * FROM Purchase WHERE receiptNumber = ?");
            ps.setInt(1, receiptNumber);
            rs = ps.executeQuery();
            if (!rs.next())
                current = true;
            ps.close();
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        ps = con.prepareStatement("INSERT INTO Purchase VALUES (?,?,1,?,?)");
        ps.setInt(1,receiptNumber);
        ps.setTimestamp(2,timestamp);
        ps.setInt(3,id);
        ps.setInt(4,branch);

        ps.executeUpdate();

        con.commit();
        ps.close();

        while(!finish) {
            showPurchase(receiptNumber, totalPrice);
            System.out.print("\n press 0 to quit");
            System.out.print("\n press 1 to delete item");
            System.out.print("\n press 2 to finish the purchase");
            System.out.print("\nplease Enter item ID: ");
            itemid = Integer.parseInt(in.readLine());

            if(itemid ==0 )
                purchaseQuit(receiptNumber);//do nothing
            else if(itemid == 1)
                totalPrice -= deleteItem(lastItem, receiptNumber, price_id[0]);
            else if(itemid == 2) {
                purchaseFinish(receiptNumber,totalPrice);
                finish = true;
            }
            else {
                price_id = addItem(itemid, receiptNumber);
                totalPrice += price_id[0];
                lastItem = (int)price_id[1];
            }
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
                    if (!rs.next()) {
                        System.out.print("\nAccess denied: Invalid employee ID");
                    }
                    else {
                        this.branch = rs.getInt("branchNumber");
                        this.id = eid;
                        quit = true;
                        System.out.print("Welcome: " + id);
                        employeeShowMenu();
                    }

                    ps.close();

                }

            } catch (IOException e) {
                System.out.println("IOException!");
            } catch (SQLException ex) {
                System.out.println("Message: " + ex.getMessage());
                try
                {
                    con.rollback();
                }
                catch (SQLException ex2)
                {
                    System.out.println("Message: " + ex2.getMessage());
                    System.exit(-1);
                }

            }


    }

    private void showPurchase(int receiptNumber, double totalPrice) throws SQLException{
        String     itemID;
        String     itemName;
        double    itemPrice;
        int itemAmount = 0;
        String     itemType;
        PreparedStatement  ps;
        ResultSet  rs;
        System.out.print("\nItems:");
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

        System.out.print("\nTotal: " + Math.round(totalPrice * 100.0) / 100.0);

    }

    private double[] addItem(int itemid, int receiptNumber) throws SQLException{
        PreparedStatement  ps;
        ResultSet  rs;
        double[] rvalue = new double[2];
        ps = con.prepareStatement("SELECT * FROM item WHERE itemID = ?");
        ps.setInt(1, itemid);
        rs = ps.executeQuery();
        if (!rs.next())
            System.out.println("Invaild itemID");
        else{
            rvalue[0] = rs.getDouble("price");
            rvalue[1] = itemid;
            ps = con.prepareStatement("SELECT * FROM itemsInPurchase WHERE itemID = ? AND receiptNumber = ?");
            ps.setInt(1,itemid);
            ps.setInt(2,receiptNumber);
            rs = ps.executeQuery();
            if(!rs.next()) {
                ps = con.prepareStatement("INSERT INTO itemsInPurchase VALUES (?,?,1)");
                ps.setInt(1, receiptNumber);
                ps.setInt(2, itemid);
                ps.executeUpdate();
            }
            else{
                ps = con.prepareStatement("UPDATE itemsInPurchase SET amount = ? WHERE itemID = ? AND receiptNumber = ?");
                ps.setInt(3, receiptNumber);
                ps.setInt(2, itemid);
                ps.setInt(1,rs.getInt("amount") + 1);
                ps.executeUpdate();
            }
            con.commit();
            ps.close();

        }
        ps.close();
        return rvalue;

    }

    private double deleteItem(int lastItem, int receiptNumber, double price) throws SQLException, IOException{

        double rprice = price;
        int itemid;
        System.out.print("\npress 0 to quit");
        System.out.print("\nPress 1 to delete last item");
        System.out.print("\nEnter itemID to delete ");
        itemid = Integer.parseInt(in.readLine());

        if(itemid == 0)
            ;//do nothing
        else if(itemid == 1){
            deleteLastItem(lastItem,receiptNumber);
        }else{
            rprice = deleteItemHelper(itemid,receiptNumber);
        }

        return rprice;
    }

    private void deleteLastItem(int lastItem, int receiptNumber) throws SQLException{
        PreparedStatement  ps;
        ResultSet  rs;
        int amount;
        double price;
        ps = con.prepareStatement("SELECT * FROM itemsInPurchase WHERE itemID = ? AND receiptNumber = ?");
        ps.setInt(1,lastItem);
        ps.setInt(2,receiptNumber);
        rs = ps.executeQuery();
        rs.next();
        amount = rs.getInt("amount");
        if(amount == 1){
            ps = con.prepareStatement("DELETE FROM ItemsInPurchase WHERE itemID = ? AND receiptNumber = ?");
            ps.setInt(1,lastItem);
            ps.setInt(2,receiptNumber);
            ps.executeUpdate();

        }
        else {
            ps = con.prepareStatement("UPDATE itemsInPurchase SET amount = ? WHERE itemID = ? AND receiptNumber = ?");
            ps.setInt(3, receiptNumber);
            ps.setInt(2, lastItem);
            System.out.println(amount - 1);
            ps.setInt(1,amount - 1);
            ps.executeUpdate();
        }
        con.commit();
        ps.close();

    }

    private double deleteItemHelper (int itemId, int receiptNumber) throws SQLException{
        PreparedStatement  ps;
        ResultSet  rs;
        double price = 0;
        boolean vaildItem = true;
        ps = con.prepareStatement("SELECT * FROM item WHERE itemID = ?");
        ps.setInt(1, itemId);
        rs = ps.executeQuery();
        if(!rs.next())
            vaildItem = false;
        else
            price = rs.getDouble("price");
        ps = con.prepareStatement("SELECT * FROM itemsInPurchase WHERE itemID = ? AND receiptNumber = ?");
        ps.setInt(1,itemId);
        ps.setInt(2,receiptNumber);
        rs = ps.executeQuery();
        if(!rs.next())
            vaildItem = false;
        if(vaildItem)
            deleteLastItem(itemId,receiptNumber);
        else
            System.out.print("\nInvaild itemID or item doesn't in list");
        ps.close();
        return price;
    }

    private void purchaseQuit(int receiptNumber) throws SQLException{
        PreparedStatement ps;
        ps = con.prepareStatement("DELETE FROM Purchase WHERE receiptNumber = ?");
        ps.setInt(1,receiptNumber);
        ps.executeUpdate();
        con.commit();
        ps.close();
        System.out.println("\nPurchase canceled");
    }

    private void purchaseFinish(int receiptNumber, double totalPrice) throws SQLException{
        PreparedStatement ps;
        ps = con.prepareStatement("UPDATE Purchase SET totalPrice = ? WHERE receiptNumber = ?");
        ps.setDouble(1,totalPrice);
        ps.setInt(2,receiptNumber);
        ps.executeUpdate();
        System.out.println("\nPurchase finished");
    }


    }
