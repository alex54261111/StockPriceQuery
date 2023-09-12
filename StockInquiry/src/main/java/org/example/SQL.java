package java.org.example;

import java.sql.*;
import java.util.Properties;

public class SQL {
    private int id;
    private String co;
    private double price;
    private String oder;

    private String getSQLRespond;


    public SQL(String oder) {
        this.oder = oder;
    }
    public void pushSQL() throws Exception {
        Statement stmt = linkSQL().createStatement();
        stmt.executeUpdate(oder);
        System.out.println("SQL ok");
    }

    public void searchSQL() throws Exception {
        PreparedStatement stmt = linkSQL().prepareStatement(oder);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            id = rs.getInt("id");
            co = rs.getString("co");
            price = rs.getDouble("price");
            getSQLRespond = id + ":" + co + ":" + price;
            System.out.println(getSQLRespond);

        }
    }
    private Connection linkSQL() throws Exception {
        Properties prop = new Properties();
        prop.put("user", "root");
        prop.put("password", "root");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/Stock", prop);
        return conn;
    }
    public int getId() {
        return id;
    }
    public String getCo() {
        return co;
    }
    public double getPrice() {
        return price;
    }
}
