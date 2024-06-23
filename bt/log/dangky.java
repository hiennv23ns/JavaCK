package log;

import client.Client;
import server.Server;
import sql.connectDB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class dangky extends JFrame implements ActionListener {
    // Model
    private connectDB dbConnection;

    // View
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton loginButton;

    public dangky() {
        setTitle("Đăng Ký / Đăng Nhập");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Ensure content pane uses BorderLayout
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        registerButton = new JButton("Đăng Ký");
        loginButton = new JButton("Đăng Nhập");

        registerButton.addActionListener(this);
        loginButton.addActionListener(this);

        panel.add(new JLabel("Tên Đăng Nhập:"));
        panel.add(usernameField);
        panel.add(new JLabel("Mật Khẩu:"));
        panel.add(passwordField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);
        panel.add(new JLabel());
        panel.add(buttonPanel);

        add(panel, BorderLayout.CENTER);

        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Đăng Ký / Đăng Nhập", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        pack();
        setLocationRelativeTo(null);

        // Kết nối tới cơ sở dữ liệu
        try {
            dbConnection = new connectDB();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể kết nối tới cơ sở dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            try {
                if (isUserExists(usernameField.getText())) {
                    JOptionPane.showMessageDialog(this, "Tên đăng nhập đã tồn tại. Vui lòng chọn tên đăng nhập khác!");
                    return;
                }

                if (registerUser(usernameField.getText(), new String(passwordField.getPassword()))) {
                    JOptionPane.showMessageDialog(this, "Đăng Ký Thành Công!");
                    usernameField.setText("");
                    passwordField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Đăng Ký Thất Bại!");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra trong quá trình đăng ký!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == loginButton) {
            try {
                if (!isUserValid(usernameField.getText(), new String(passwordField.getPassword()))) {
                    JOptionPane.showMessageDialog(this, "Tên đăng nhập hoặc mật khẩu không đúng!");
                    return;
                }

                JOptionPane.showMessageDialog(this, "Đăng Nhập Thành Công!");
                usernameField.setText("");
                passwordField.setText("");



                new Thread(() -> {
                    SwingUtilities.invokeLater(() -> {
                        new Client().setVisible(true);
                    });
                }).start();

                dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra trong quá trình đăng nhập!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Kiểm tra xem tên đăng nhập đã tồn tại trong cơ sở dữ liệu hay không
    private boolean isUserExists(String username) throws SQLException {
        PreparedStatement statement = dbConnection.getConnection().prepareStatement("SELECT * FROM users WHERE username = ?");
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }

    // Đăng ký người dùng mới
    private boolean registerUser(String username, String password) throws SQLException {
        PreparedStatement statement = dbConnection.getConnection().prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
        statement.setString(1, username);
        statement.setString(2, password);
        int rowsInserted = statement.executeUpdate();
        return rowsInserted > 0;
    }

    // Kiểm tra xem tên đăng nhập và mật khẩu có hợp lệ hay không
    private boolean isUserValid(String username, String password) throws SQLException {
        PreparedStatement statement = dbConnection.getConnection().prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }

    public static void main(String[] args) {
        // Khởi động giao diện đăng ký/đăng nhập
        SwingUtilities.invokeLater(() -> {
            new dangky().setVisible(true);
        });
    }
}
