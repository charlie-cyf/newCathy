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
        int id;
        try {
            System.out.print("\nplease Enter item ID: \n");
            id = Integer.parseInt(in.readLine());
        }catch (IOException e) {
            System.out.println("IOException!");
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


    }
