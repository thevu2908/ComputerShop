package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    private static String url = "jdbc:mysql://localhost:3306/computershop";
    private static String user = "root";
    private static String password = "";
    private Connection connection = null;

    public MyConnection() {
    }

    public static Connection getConnect() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, password);
    }
    public void closeConnection()  {
        try {
            if (connection != null) {
                connection.close();
            }
            else {
                System.out.println("conn is not null!! \n");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

}