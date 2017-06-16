import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Shmidt on 15.06.2017.
 */
public class DBController {

    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public Connection connection;

    public DBController(){
        try{
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
