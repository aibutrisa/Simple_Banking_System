package banking;

import java.sql.*;

public class DBWork {
     private String url;

    public DBWork (String url) {
        this.url = url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    public void createNewDatabase() {
        try (Connection conn = DriverManager.getConnection(this.url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS card (\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "	number text,\n"
                + "	pin text,\n"
                + "	balance integer DEFAULT 0\n"
                + ");";
        try (Connection conn = DriverManager.getConnection(this.url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert(Account account) {
        String sql = "INSERT INTO card(number, pin) VALUES(?,?)";

        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, account.getAccNumber());
            pstmt.setString(2, account.getPinCode());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean findNumber(String accNumber) {
        boolean res = false;
        String sql = "SELECT number FROM card WHERE number = ?";
        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accNumber);
            ResultSet rs  = pstmt.executeQuery();
            res = rs.next() ? true : false;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return res;
    }

    public Account findAccount(String accNumber, String pin) {
        Account acc = null;
        String sql = "SELECT number, pin, balance FROM card WHERE number = ? AND pin = ?";
        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accNumber);
            pstmt.setString(2, pin);
            ResultSet rs  = pstmt.executeQuery();
            if (rs.next()) {
                acc = new Account(rs.getString("number"), rs.getString("pin"), rs.getInt("balance"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return acc;
    }

    public void setBalance(String accNumber, int newBalance) {
        String sql = "UPDATE card SET balance = balance + ? WHERE number = ?";
        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newBalance);
            pstmt.setString(2, accNumber);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteAcc(Account account) {
        String sql = "DELETE FROM card WHERE number = ?";
        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, account.getAccNumber());
            pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}
