package data;

import configuration.constants.AppPrm;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static volatile Connection connection;
    private static AppPrm appPrm = null;

    static {
        try {
            appPrm = new AppPrm();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            synchronized (DBConnection.class) {
                connection = DriverManager.getConnection(appPrm.getDbUrl());
            }
        }
        return connection;
    }
}
