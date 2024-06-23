package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connectDB {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/chatapp"; // Thay chat_app bằng tên cơ sở dữ liệu của bạn
    private static final String USER = "root"; // Tên người dùng mặc định của XAMPP là root
    private static final String PASSWORD = ""; // Mật khẩu mặc định là rỗng trừ khi bạn đã đặt mật khẩu

    private Connection connection;

    // Constructor
    public connectDB() throws SQLException {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Thiết lập kết nối
            this.connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Kết nối tới cơ sở dữ liệu thành công.");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Không tìm thấy MySQL JDBC Driver.", e);
        } catch (SQLException e) {
            throw new SQLException("Kết nối tới cơ sở dữ liệu thất bại.", e);
        }
    }

    // Lấy đối tượng kết nối
    public Connection getConnection() {
        return this.connection;
    }

    // Đóng kết nối
    public void close() {
        if (this.connection != null) {
            try {
                this.connection.close();
                System.out.println("Đã đóng kết nối cơ sở dữ liệu.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Phương thức main để kiểm tra
    public static void main(String[] args) {
        try {
            connectDB dbConnection = new connectDB();
            // Thực hiện một số thao tác cơ sở dữ liệu
            // ...

            // Đóng kết nối
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

