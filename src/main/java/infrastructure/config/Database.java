package infrastructure.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {


    private static Database instance;
    private final Connection connection;


    private Database() throws IOException, SQLException {
        try (InputStream input = Database.class.getClassLoader().getResourceAsStream("application.properties")) {
            Properties props = new Properties();
            props.load(input);

            String host = System.getenv().getOrDefault("DB_HOST", props.getProperty("db.host"));
            String port = System.getenv().getOrDefault("DB_PORT", props.getProperty("db.port"));
            String dbName = System.getenv().getOrDefault("DB_NAME", props.getProperty("db.dbname"));
            String user = System.getenv().getOrDefault("DB_USER", props.getProperty("db.user"));
            String password = System.getenv().getOrDefault("DB_PASSWORD", props.getProperty("db.password"));

            String url = String.format("jdbc:postgresql://%s:%s/%s", host, port, dbName);



            this.connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new SQLException("Failed to connect to database", e);
        }
    }


    public static synchronized Database getInstance() throws SQLException {
        try {
            if(instance == null || instance.getConnection().isClosed()) {
                instance = new Database();
            }
        } catch (SQLException | IOException e) {
            throw new SQLException("couldn't connect to database : ", e);
        }
        return instance;
    }

    public Connection getConnection() {
        return this.connection;
    }
}



