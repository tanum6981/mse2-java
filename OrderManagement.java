
import java.sql.*;
import java.util.Scanner;

public class OrderManagement {

    // Database connection details
    static final String URL =
            "jdbc:mysql://localhost:3306/ecommerce_db";

    static final String USER = "root";
    static final String PASSWORD = "Tanu@6200";

    static Connection con;
    static Scanner sc = new Scanner(System.in);

    // Connect to MySQL database
    public static void connect() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(
                    URL, USER, PASSWORD);

            System.out.println("Database Connected Successfully!");

        } catch (Exception e) {
            System.out.println("Connection Error: " + e.getMessage());
        }
    }

    // (a) Insert three sample orders using PreparedStatement
    public static void insertOrders() {

        String sql = "INSERT INTO Orders VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            // First order
            ps.setInt(1, 101);
            ps.setString(2, "Rahul");
            ps.setString(3, "Laptop");
            ps.setInt(4, 2);
            ps.setDouble(5, 55000);
            ps.setString(6, "Pending");

            try {
                ps.executeUpdate();
                System.out.println("Order 101 inserted successfully!");
            } catch (SQLIntegrityConstraintViolationException e) {
                System.out.println("Order 101 already exists!");
            }

            // Second order
            ps.setInt(1, 102);
            ps.setString(2, "Priya");
            ps.setString(3, "Mobile");
            ps.setInt(4, 5);
            ps.setDouble(5, 20000);
            ps.setString(6, "Shipped");

            try {
                ps.executeUpdate();
                System.out.println("Order 102 inserted successfully!");
            } catch (SQLIntegrityConstraintViolationException e) {
                System.out.println("Order 102 already exists!");
            }

            // Third order
            ps.setInt(1, 103);
            ps.setString(2, "Amit");
            ps.setString(3, "Headphones");
            ps.setInt(4, 3);
            ps.setDouble(5, 2500);
            ps.setString(6, "Cancelled");

            try {
                ps.executeUpdate();
                System.out.println("Order 103 inserted successfully!");
            } catch (SQLIntegrityConstraintViolationException e) {
                System.out.println("Order 103 already exists!");
            }

        } catch (SQLException e) {
            System.out.println("Insert Error: " + e.getMessage());
        }
    }

    // (b) Display orders by status and quantity > 2
    public static void displayOrders() {

        System.out.print("Enter Order Status: ");
        String status = sc.nextLine();

        String sql = "SELECT * FROM Orders "
                   + "WHERE status = ? AND quantity > 2 "
                   + "ORDER BY unit_price DESC";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            System.out.println("\n--- ORDER DETAILS ---");

            while (rs.next()) {

                found = true;

                System.out.println("Order ID: "
                        + rs.getInt("order_id"));

                System.out.println("Customer Name: "
                        + rs.getString("customer_name"));

                System.out.println("Product Name: "
                        + rs.getString("product_name"));

                System.out.println("Quantity: "
                        + rs.getInt("quantity"));

                System.out.println("Unit Price: "
                        + rs.getDouble("unit_price"));

                System.out.println("Status: "
                        + rs.getString("status"));

                System.out.println("------------------------");
            }

            if (!found) {
                System.out.println("No matching orders found!");
            }

            rs.close();

        } catch (SQLException e) {
            System.out.println("Display Error: " + e.getMessage());
        }
    }

    // (c) Increase quantity of an existing order
    public static void increaseQuantity() {

        System.out.print("Enter Order ID: ");
        int id = sc.nextInt();

        System.out.print("Enter Additional Quantity: ");
        int qty = sc.nextInt();
        sc.nextLine();

        if (qty <= 0) {
            System.out.println("Quantity must be greater than zero!");
            return;
        }

        String sql = "UPDATE Orders "
                   + "SET quantity = quantity + ? "
                   + "WHERE order_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, qty);
            ps.setInt(2, id);

            int result = ps.executeUpdate();

            if (result > 0) {
                System.out.println("Quantity Updated Successfully!");
            } else {
                System.out.println("Order ID Not Found!");
            }

        } catch (SQLException e) {
            System.out.println("Update Error: " + e.getMessage());
        }
    }

    // (d) Update order status after validation
    public static void updateStatus() {

        System.out.print("Enter Order ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter New Status: ");
        String status = sc.nextLine().trim();

        if (status.equalsIgnoreCase("Pending")) {
            status = "Pending";
        } else if (status.equalsIgnoreCase("Shipped")) {
            status = "Shipped";
        } else if (status.equalsIgnoreCase("Delivered")) {
            status = "Delivered";
        } else {
            System.out.println("Invalid Status!");
            System.out.println("Allowed: Pending, Shipped, Delivered");
            return;
        }

        String checkSql =
                "SELECT * FROM Orders WHERE order_id = ?";

        try (PreparedStatement ps = con.prepareStatement(checkSql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                System.out.println("\nOrder Found!");
                System.out.println("Customer: "
                        + rs.getString("customer_name"));

                System.out.println("Product: "
                        + rs.getString("product_name"));

                System.out.println("Old Status: "
                        + rs.getString("status"));

                rs.close();

                String updateSql =
                        "UPDATE Orders SET status = ? WHERE order_id = ?";

                try (PreparedStatement ps2 =
                             con.prepareStatement(updateSql)) {

                    ps2.setString(1, status);
                    ps2.setInt(2, id);

                    ps2.executeUpdate();
                }

                // Display updated details
                String showSql =
                        "SELECT * FROM Orders WHERE order_id = ?";

                try (PreparedStatement ps3 =
                             con.prepareStatement(showSql)) {

                    ps3.setInt(1, id);

                    ResultSet rs2 = ps3.executeQuery();

                    if (rs2.next()) {

                        System.out.println("\nUpdated Order Details:");

                        System.out.println("Order ID: "
                                + rs2.getInt("order_id"));

                        System.out.println("Customer: "
                                + rs2.getString("customer_name"));

                        System.out.println("Product: "
                                + rs2.getString("product_name"));

                        System.out.println("Quantity: "
                                + rs2.getInt("quantity"));

                        System.out.println("Unit Price: "
                                + rs2.getDouble("unit_price"));

                        System.out.println("New Status: "
                                + rs2.getString("status"));
                    }

                    rs2.close();
                }

            } else {
                System.out.println("Order ID Not Found!");
                rs.close();
            }

        } catch (SQLException e) {
            System.out.println("Status Update Error: " + e.getMessage());
        }
    }

    // (e) Display total order value
    public static void displayTotalValue() {

        String sql =
                "SELECT SUM(quantity * unit_price) AS total_value "
              + "FROM Orders";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {

                System.out.println("\nTotal Order Value = "
                        + rs.getDouble("total_value"));
            }

        } catch (SQLException e) {
            System.out.println("Total Value Error: " + e.getMessage());
        }
    }

    // (e) Delete order only if status is Cancelled
    public static void deleteOrder() {

        System.out.print("Enter Order ID to Delete: ");
        int id = sc.nextInt();
        sc.nextLine();

        String sql =
                "DELETE FROM Orders "
              + "WHERE order_id = ? AND status = 'Cancelled'";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            int result = ps.executeUpdate();

            if (result > 0) {
                System.out.println("Cancelled Order Deleted Successfully!");
            } else {
                System.out.println(
                    "Cannot Delete! Order does not exist "
                    + "or status is not Cancelled."
                );
            }

        } catch (SQLException e) {
            System.out.println("Delete Error: " + e.getMessage());
        }
    }

    // Main method
    public static void main(String[] args) {

        connect();

        if (con == null) {
            System.out.println("Database connection failed. Exiting...");
            return;
        }

        int choice;

        do {

            System.out.println("\n======================================");
            System.out.println("       ORDER MANAGEMENT SYSTEM");
            System.out.println("======================================");

            System.out.println("1. Insert Sample Orders");
            System.out.println("2. Display Orders by Status");
            System.out.println("3. Increase Order Quantity");
            System.out.println("4. Update Order Status");
            System.out.println("5. Display Total Order Value");
            System.out.println("6. Delete Cancelled Order");
            System.out.println("7. Exit");

            System.out.print("\nEnter Your Choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    insertOrders();
                    break;

                case 2:
                    displayOrders();
                    break;

                case 3:
                    increaseQuantity();
                    break;

                case 4:
                    updateStatus();
                    break;

                case 5:
                    displayTotalValue();
                    break;

                case 6:
                    deleteOrder();
                    break;

                case 7:
                    System.out.println("Thank You! Program Exited.");
                    break;

                default:
                    System.out.println("Invalid Choice! Try Again.");
            }

        } while (choice != 7);

        try {
            con.close();
            sc.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

