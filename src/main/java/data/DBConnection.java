package data;

import configuration.constants.App;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static Connection connection;
    private static App app = null;

    static {
        try {
            app = new App();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            synchronized (DBConnection.class) {
                connection = DriverManager.getConnection(app.getDbUrl());
            }
        }
        return connection;
    }
}
