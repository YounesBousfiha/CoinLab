package infrastructure.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {


    private static Database instance;
    private final Connection connection;


    private Database() throws SQLException {
        try {
            String url = GlobalValue.DB_URL;
            String user = GlobalValue.DB_USER;
            String password = GlobalValue.PASSWORD;
            this.connection = DriverManager.getConnection(url, user, password);
        }  catch (SQLException e) {
            throw new SQLException("couldn't connect to Database : ", e);
        }
    }


    public static synchronized Database getInstance() throws SQLException {
        try {
            if(instance == null || instance.getConnection().isClosed()) {
                instance = new Database();
            }
        } catch (SQLException e) {
            throw new SQLException("couldn't connect to database : ", e);
        }
        return instance;
    }

    public Connection getConnection() {
        return this.connection;
    }
}
